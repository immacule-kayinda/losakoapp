package com.example.losako.models;

public class VocalNote {
    private int id;
    private int idPatient;
    private int idPersonnel;
    private String filePath;
    private String author;
    private String dateSent;

    public VocalNote(int idPatient, int idPersonnel, String filePath, String author, String dateSent) {
        this.idPatient = idPatient;
        this.idPersonnel = idPersonnel;
        this.filePath = filePath;
        this.author = author;
        this.dateSent = dateSent;
    }

    public VocalNote(int id, int idPatient, int idPersonnel, String filePath, String author, String dateSent) {
        this.id = id;
        this.idPatient = idPatient;
        this.idPersonnel = idPersonnel;
        this.filePath = filePath;
        this.author = author;
        this.dateSent = dateSent;
    }

    public VocalNote() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdPatient() {
        return idPatient;
    }

    public void setIdPatient(int idPatient) {
        this.idPatient = idPatient;
    }

    public int getIdPersonnel() {
        return idPersonnel;
    }

    public void setIdPersonnel(int idPersonnel) {
        this.idPersonnel = idPersonnel;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDateSent() {
        return dateSent;
    }

    public void setDateSent(String dateSent) {
        this.dateSent = dateSent;
    }
}


