package com.telelab.paperville.models;

import java.io.Serializable;
import java.util.ArrayList;

public class Subject implements Serializable {
    private String name;
    private ArrayList<Paper> paper;

    public Subject(String name, ArrayList<Paper> paper) {
        this.name = name;
        this.paper = paper;
    }

    public Subject(){

    }

    public String getName() {
        return name;
    }

    public ArrayList<Paper> getPaper() {
        return paper;
    }
}

