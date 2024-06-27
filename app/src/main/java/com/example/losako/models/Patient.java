package com.example.losako.models;

import java.io.Serializable;

public class Patient implements Serializable {
    private long idPatient;
    private String nomPatient;
    private String postnomPatient;
    private String prenomPatient;
    private String addressPatient;
    private String phoneNumberPatient;
    private String maladiePatient;
    private String dateVenuPatient;

    // Constructeur
    public Patient() {
    }

    public Patient(int idPatient, String nomPatient, String postnomPatient, String prenomPatient,
                   String addressPatient, String phoneNumberPatient, String maladiePatient,
                   String dateVenuPatient) {
        this.idPatient = idPatient;
        this.nomPatient = nomPatient;
        this.postnomPatient = postnomPatient;
        this.prenomPatient = prenomPatient;
        this.addressPatient = addressPatient;
        this.phoneNumberPatient = phoneNumberPatient;
        this.maladiePatient = maladiePatient;
        this.dateVenuPatient = dateVenuPatient;
    }

    public Patient(String nomPatient, String postnomPatient, String prenomPatient, String addressPatient, String phoneNumberPatient, String maladiePatient) {
        this.nomPatient = nomPatient;
        this.postnomPatient = postnomPatient;
        this.prenomPatient = prenomPatient;
        this.addressPatient = addressPatient;
        this.phoneNumberPatient = phoneNumberPatient;
        this.maladiePatient = maladiePatient;
    }

    public Patient(String nomPatient, String postnomPatient, String prenomPatient,
                   String addressPatient, String phoneNumberPatient, String maladiePatient,
                   String dateVenuPatient) {
        this.nomPatient = nomPatient;
        this.postnomPatient = postnomPatient;
        this.prenomPatient = prenomPatient;
        this.addressPatient = addressPatient;
        this.phoneNumberPatient = phoneNumberPatient;
        this.maladiePatient = maladiePatient;
        this.dateVenuPatient = dateVenuPatient;
    }

    public Patient clone() {
        Patient clonedPatient = new Patient();
        clonedPatient.setIdPatient((int) this.idPatient);
        clonedPatient.setNomPatient(this.nomPatient);
        clonedPatient.setPrenomPatient(this.prenomPatient);
        clonedPatient.setAddressPatient(this.addressPatient);
        clonedPatient.setPhoneNumberPatient(this.phoneNumberPatient);
        clonedPatient.setMaladiePatient(this.maladiePatient);
        return clonedPatient;
    }

    // Getters
    public int getIdPatient() {
        return (int) idPatient;
    }

    // Setters
    public void setIdPatient(int idPatient) {
        this.idPatient = idPatient;
    }

    public String getNomPatient() {
        return nomPatient;
    }

    public void setNomPatient(String nomPatient) {
        this.nomPatient = nomPatient;
    }

    public String getPostnomPatient() {
        return postnomPatient;
    }

    public void setPostnomPatient(String postnomPatient) {
        this.postnomPatient = postnomPatient;
    }

    public String getPrenomPatient() {
        return prenomPatient;
    }

    public void setPrenomPatient(String prenomPatient) {
        this.prenomPatient = prenomPatient;
    }

    public String getAddressPatient() {
        return addressPatient;
    }

    public void setAddressPatient(String addressPatient) {
        this.addressPatient = addressPatient;
    }

    public String getPhoneNumberPatient() {
        return phoneNumberPatient;
    }

    public void setPhoneNumberPatient(String phoneNumberPatient) {
        this.phoneNumberPatient = phoneNumberPatient;
    }

    public String getMaladiePatient() {
        return maladiePatient;
    }

    public void setMaladiePatient(String maladiePatient) {
        this.maladiePatient = maladiePatient;
    }

    public String getDateVenuPatient() {
        return dateVenuPatient;
    }

    public void setDateVenuPatient(String dateVenuPatient) {
        this.dateVenuPatient = dateVenuPatient;
    }


}

