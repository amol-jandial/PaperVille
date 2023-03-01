package com.telelab.paperville.models;

import java.io.Serializable;
import java.util.ArrayList;

public class DataWrapper implements Serializable {
    private ArrayList<Subject> subjects;

    public DataWrapper(ArrayList<Subject> subjects){
        this.subjects = subjects;
    }

    public ArrayList<Subject> getSubjects() {
        return subjects;
    }
}
