package com.example.losako.models;

public class PatientWithNotes {
    private Patient patient;
    private String filePath;
    private String author;
    private final long dateSent;

    public PatientWithNotes(Patient patient, String filePath, String author, long dateSent) {
        this.patient = patient;
        this.filePath = filePath;
        this.author = author;
        this.dateSent = dateSent;
    }

    public long getDateSent() {
        return dateSent;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
