package com.telelab.paperville;

import com.telelab.paperville.models.Paper;
import com.telelab.paperville.models.Subject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SubjectGetter {
    private HashMap<String, ArrayList<Subject>> branches = new HashMap<>();

    public SubjectGetter(){
        branches.put("CSE1", new ArrayList<Subject>(){
            {
                add(new Subject("Quantum Physics", new ArrayList<Paper>()));
                add(new Subject("C Language", new ArrayList<Paper>()));
                add(new Subject("Calculus", new ArrayList<Paper>()));
                add(new Subject("Subject 4", new ArrayList<Paper>()));
                add(new Subject("Subject 5", new ArrayList<Paper>()));
            }
        });

        branches.put("CSE2", new ArrayList<Subject>(){
            {
                add(new Subject("C++", new ArrayList<Paper>()));
                add(new Subject("Chemistry", new ArrayList<Paper>()));
                add(new Subject("Ethics", new ArrayList<Paper>()));
                add(new Subject("Subject 4", new ArrayList<Paper>()));
                add(new Subject("Subject 5", new ArrayList<Paper>()));
            }
        });
    }

    public ArrayList<Subject> getSubject(String key){
       return branches.get(key);
    }
}
