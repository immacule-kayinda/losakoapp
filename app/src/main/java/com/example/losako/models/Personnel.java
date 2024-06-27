package com.example.losako.models;

public class Personnel {
    private long idPersonnel;
    private String nomPersonnel;
    private String postnomPersonnel;
    private String prenomPersonnel;
    private String adressPersonel;
    private String phoneNumberPersonnel;
    private String specialisationPersonnel;
    private String passwordPersonnel;

    public Personnel(String nomPersonnel, String postnomPersonnel, String prenomPersonnel, String adressPersonel, String phoneNumberPersonnel, String specialisationPersonnel, String passwordPersonnel) {
        this.nomPersonnel = nomPersonnel;
        this.postnomPersonnel = postnomPersonnel;
        this.prenomPersonnel = prenomPersonnel;
        this.adressPersonel = adressPersonel;
        this.phoneNumberPersonnel = phoneNumberPersonnel;
        this.specialisationPersonnel = specialisationPersonnel;
        this.passwordPersonnel = passwordPersonnel;
    }

    public Personnel() {
    }

    public Personnel(long idPersonnel, String nomPersonnel, String postnomPersonnel, String prenomPersonnel, String adressPersonel, String phoneNumberPersonnel, String specialisationPersonnel, String passwordPersonnel) {
        this.idPersonnel = idPersonnel;
        this.nomPersonnel = nomPersonnel;
        this.postnomPersonnel = postnomPersonnel;
        this.prenomPersonnel = prenomPersonnel;
        this.adressPersonel = adressPersonel;
        this.phoneNumberPersonnel = phoneNumberPersonnel;
        this.specialisationPersonnel = specialisationPersonnel;
        this.passwordPersonnel = passwordPersonnel;
    }

    public long getIdPersonnel() {
        return idPersonnel;
    }

    public void setIdPersonnel(long idPersonnel) {
        this.idPersonnel = idPersonnel;
    }

    public String getNomPersonnel() {
        return nomPersonnel;
    }

    public void setNomPersonnel(String nomPersonnel) {
        this.nomPersonnel = nomPersonnel;
    }

    public String getPostnomPersonnel() {
        return postnomPersonnel;
    }

    public void setPostnomPersonnel(String postnomPersonnel) {
        this.postnomPersonnel = postnomPersonnel;
    }

    public String getPrenomPersonnel() {
        return prenomPersonnel;
    }

    public void setPrenomPersonnel(String prenomPersonnel) {
        this.prenomPersonnel = prenomPersonnel;
    }

    public String getAdressPersonel() {
        return adressPersonel;
    }

    public void setAdressPersonel(String adressPersonel) {
        this.adressPersonel = adressPersonel;
    }

    public String getPhoneNumberPersonnel() {
        return phoneNumberPersonnel;
    }

    public void setPhoneNumberPersonnel(String phoneNumberPersonnel) {
        this.phoneNumberPersonnel = phoneNumberPersonnel;
    }

    public String getSpecialisationPersonnel() {
        return specialisationPersonnel;
    }

    public void setSpecialisationPersonnel(String specialisationPersonnel) {
        this.specialisationPersonnel = specialisationPersonnel;
    }

    public String getPasswordPersonnel() {
        return passwordPersonnel;
    }

    public void setPasswordPersonnel(String passwordPersonnel) {
        this.passwordPersonnel = passwordPersonnel;
    }
}
