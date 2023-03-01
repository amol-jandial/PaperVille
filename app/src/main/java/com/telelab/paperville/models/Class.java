package com.telelab.paperville.models;

import com.telelab.paperville.SubjectGetter;

import java.util.ArrayList;

public class Class {
    private String branch;
    private String section;
    private String semester;
    private ArrayList<Student> students;
    private ArrayList<Subject> subjects;

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }



    public Class(){

    }

    public void makeSubjects(){
        subjects = new ArrayList<>();
        SubjectGetter subjectGetter = new SubjectGetter();
        subjects = subjectGetter.getSubject(branch+semester);
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public void setStudents(ArrayList<Student> students) {
        this.students = students;
    }


    public String getBranch() {
        return branch;
    }

    public String getSection() {
        return section;
    }

    public ArrayList<Student> getStudents() {
        return students;
    }

    public ArrayList<Subject> getSubjects() {
        return subjects;
    }
}
