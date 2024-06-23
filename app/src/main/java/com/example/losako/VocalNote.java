package com.example.losako;

public class VocalNote {
    private int idVocalNote;
    private int idPatientVn;
    private String filePath;

    // Constructeur
    public VocalNote(int idVocalNote, int idPatientVn, String filePath) {
        this.idVocalNote = idVocalNote;
        this.idPatientVn = idPatientVn;
        this.filePath = filePath;
    }

    // Getters
    public int getIdVocalNote() {
        return idVocalNote;
    }

    public int getIdPatientVn() {
        return idPatientVn;
    }

    public String getFilePath() {
        return filePath;
    }

    // Setters
    public void setIdVocalNote(int idVocalNote) {
        this.idVocalNote = idVocalNote;
    }

    public void setIdPatientVn(int idPatientVn) {
        this.idPatientVn = idPatientVn;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}


