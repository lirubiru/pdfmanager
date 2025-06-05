package com.pdfmanager.core.entities;

// Todo
public abstract class Entry {
    protected String name;
    protected String dirname;
    protected String author;
    protected String title;

    public String getAuthor() {
        return author;
    }

    public String getDirname() {
        return dirname;
    }
     
    public String getName() {
        return name;
    }
    public String getTitle() {
        return title;
    }
    

    
}
