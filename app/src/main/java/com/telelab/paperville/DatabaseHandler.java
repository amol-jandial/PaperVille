package com.telelab.paperville;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.FileUtils;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.util.FileUtil;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.telelab.paperville.models.Class;
import com.telelab.paperville.models.Paper;
import com.telelab.paperville.models.Student;
import com.telelab.paperville.models.Subject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DatabaseHandler {
    private static final String TAG = "DatabaseHandler";

    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private Activity activity;
    private Context context;

    public DatabaseHandler() {

    }

    public DatabaseHandler(Context context) {
        this.context = context;
    }

    public DatabaseHandler(Activity activity) {

        this.activity = activity;
    }

    private static void convertToPDF(ArrayList<Uri> data, Context context, Paper paper) {
        Log.d(TAG, "convertToPDF: "+data);
        File pdfFile = new File(context.getExternalCacheDir().getAbsolutePath()
                + File.separator + "TemperoryPDF_" + System.currentTimeMillis() + ".pdf");

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                PdfDocument document = new PdfDocument();
                try {
                    for(Uri image : data){
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(
                                context.getContentResolver(), image
                        );
                        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(bitmap.getWidth(),
                                bitmap.getHeight(), 1).create();
                        PdfDocument.Page page = document.startPage(pageInfo);
                        Canvas canvas = page.getCanvas();
                        Paint paint = new Paint();
                        paint.setColor(Color.parseColor("#ffffff"));
                        canvas.drawPaint(paint);
                        canvas.drawBitmap(bitmap, 0, 0, null);
                        document.finishPage(page);
                    }
                    document.writeTo(new FileOutputStream(pdfFile));
                }catch(IOException e){
                    e.printStackTrace();
                }finally {
                    document.close();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void unused) {
                super.onPostExecute(unused);
                if (pdfFile.exists() && pdfFile.length() > 0) {
                    FirebaseStorage storage = FirebaseStorage.getInstance();
                    Uri imageUri = Uri.fromFile(pdfFile);
                    StorageReference reference = storage.getReference();
                    final String messagePushID = "" + System.currentTimeMillis();
                    SharedPreferences sharedPreferences =
                            context.getSharedPreferences(context.getString(R.string.preference_file_key),
                                    Context.MODE_PRIVATE);
                    String branch = sharedPreferences.getString("userBranch", "");
                    final StorageReference filePath = reference.child("papers/" + branch);
                    UploadTask uploadTask = filePath.putFile(imageUri);

                    Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if(!task.isSuccessful()){
                                throw task.getException();
                            }
                            return filePath.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if(task.isSuccessful()){
                                paper.setDownloadPath(task.getResult().toString());
                                FirebaseFirestore firestore = FirebaseFirestore.getInstance();
                                Map<String, Object> paperMap = new HashMap<>();
                                paperMap.put("name", paper.getName());
                                paperMap.put("isValid", paper.isValid());
                                paperMap.put("type", paper.getType());
                                paperMap.put("path", paper.getDownloadPath());
                                firestore.collection("Classes").document(branch)
                                        .collection("Subjects").document(paper.getName())
                                        .set(paperMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    Toast.makeText(context, "File uploaded", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            }

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "onFailure: pdf upload failed"+e.getMessage());
                        }
                    });


                }
            }
        }.execute();

    }

    public void initialize(Student student, Class mClass) {
        Map<String, Object> userClass = new HashMap<>();
        userClass.put("branch", mClass.getBranch());
        userClass.put("semester", mClass.getSemester());
        Map<String, Object> studentMap = new HashMap<>();
        studentMap.put("username", student.getUsername());
        studentMap.put("email", student.getEmail());
        studentMap.put("phone", student.getPhone());
        Map<String, Object> subjectMap = new HashMap<>();
        Log.d(TAG, "initialize: " + userClass);
        Log.d(TAG, "initialize: " + studentMap);
        String docName = mClass.getBranch() + mClass.getSemester();
        firestore.collection("Classes").document(docName).set(userClass)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "onComplete: Class created");
                            firestore.collection("Classes").document(docName)
                                    .collection("Students").document(student.getUsername())
                                    .set(studentMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {

                                                Log.d(TAG, "onComplete: Student added" + studentMap);
                                            } else {
                                                Log.e(TAG, "onComplete: db" + task.getException());
                                            }
                                        }
                                    });
                            for (Subject subject : mClass.getSubjects()) {
                                subjectMap.put("name", subject.getName());

                                firestore.collection("Classes").document(docName).collection("Subjects")
                                        .document(subject.getName()).set(subjectMap, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {

                                                } else {
                                                    Log.e(TAG, "onComplete: " + task.getException());
                                                }
                                            }
                                        });
                            }
                        } else {
                            Log.e(TAG, "onComplete: db" + task.getException());
                        }
                    }
                });
    }

    public void getUserData() {
        SharedPreferences sharedPreferences =
                activity.getSharedPreferences(activity.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        String branch = sharedPreferences.getString("userBranch", "");
        getBranch(branch);
    }

    private void getBranch(String branch) {
        DocumentReference docRef = firestore.collection("Classes").document(branch);

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot snapshot = task.getResult();
                    if (snapshot.exists()) {
                        getSubjects(snapshot.toObject(Class.class));
                    }
                } else {
                    Log.e(TAG, "onComplete: error getting branch" + task.getException());
                }
            }
        });

    }

    public void getSubjects(Class mClass) {
        ArrayList<Subject> subjects = new ArrayList<>();
        DocumentReference docRef = firestore.collection("Classes")
                .document(mClass.getBranch() + mClass.getSemester());


        docRef.collection("Subjects").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        subjects.add(documentSnapshot.toObject(Subject.class));
                    }
                    HelperMethods.startNewActivityWithData(activity, MainActivity.class, subjects);
                } else {
                    Log.e(TAG, "onComplete: error in subject retrieval" + task.getException());
                }
            }
        });

    }

    public void uploadImages(ArrayList<Uri> images, Paper paper) {

        convertToPDF(images, context, paper);
    }


}
