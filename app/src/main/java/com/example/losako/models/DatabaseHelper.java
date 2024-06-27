package com.example.losako.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.losako.adapters.DetailsAdapter;
import com.example.losako.adapters.PatientAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "hospital.db";
    private static final int DATABASE_VERSION = 2;
    public static DatabaseHelper instance;
    MutableLiveData<ArrayList<Patient>> patientsLiveData = new MutableLiveData<>();
    private MutableLiveData<ArrayList<PatientWithNotes>> patientsWithNotesLiveData = new MutableLiveData<>();

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }

    public void refreshAllPatients() {
        ArrayList<Patient> currentPatients = getAllPatientsFromDB();
        setAllPatients(currentPatients);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PATIENT_TABLE_SQL = "CREATE TABLE IF NOT EXISTS `patients` (" +
                "`id_patient` INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "`nom_patient` VARCHAR(50) NOT NULL, " +
                "`postnom_patient` VARCHAR(50) NOT NULL, " +
                "`prenom_patient` VARCHAR(50) DEFAULT NULL, " +
                "`address_patient` VARCHAR(100) NOT NULL, " +
                "`phone_number_patient` VARCHAR(10) NOT NULL, " +
                "`maladie_patient` VARCHAR(100) NOT NULL, " +
                "`date_venu_patient` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP" +
                ")";

        // Création de la table vocal_notes
        String CREATE_VOCAL_NOTES_TABLE_SQL =
                "CREATE TABLE IF NOT EXISTS `vocal_notes` (" +
                        "`id_vocal_note` INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "`id_patient_vn` INTEGER NOT NULL, " +
                        "`id_personnel_vn` INTEGER NOT NULL," +
                        "`file_path` TEXT NOT NULL, " +
                        "`author` TEXT NOT NULL," +
                        "`date_sent` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP," +
                        "FOREIGN KEY(`id_patient_vn`) REFERENCES `patients`(`id_patient`) ON DELETE CASCADE," +
                        "FOREIGN KEY(`id_personnel_vn`) REFERENCES `personnel`(`id_personnel`) ON DELETE CASCADE" +
                        ")";

        String CREATE_PERSONNEL_TABLE_SQL = "CREATE TABLE IF NOT EXISTS `personnel` (" +
                "`id_personnel` INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "`nom_personnel` VARCHAR(50) NOT NULL, " +
                "`postnom_personnel` VARCHAR(50) NOT NULL, " +
                "`prenom_personnel` VARCHAR(50) NOT NULL, " +
                "`adress_personnel` VARCHAR(100) NOT NULL, " +
                "`phone_number_personnel` VARCHAR(10) NOT NULL UNIQUE, " + // Assurez-vous que le numéro de téléphone est unique
                "`specialisation` VARCHAR(50) NOT NULL, " +
                "`password` VARCHAR(255) NOT NULL)";
        // Exécution des commandes SQL
        db.execSQL(CREATE_PERSONNEL_TABLE_SQL);
        db.execSQL(CREATE_PATIENT_TABLE_SQL);
        db.execSQL(CREATE_VOCAL_NOTES_TABLE_SQL);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Logique de mise à jour ici
        db.execSQL("DROP TABLE IF EXISTS `vocal_notes`");
        db.execSQL("DROP TABLE IF EXISTS `patients`");
        onCreate(db);
    }


    public Map<Integer, PatientWithNotes> getPatientWithNotes(int idPatient) {
        Map<Integer, PatientWithNotes> patientMap = new HashMap<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT p.*, vn.file_path, vn.author, vn.date_sent " +
                "FROM patients AS p " +
                "LEFT JOIN vocal_notes AS vn ON p.id_patient = vn.id_patient_vn " +
                "WHERE p.id_patient =?", new String[]{String.valueOf(idPatient)});

        if (cursor != null) {
            while (cursor.moveToNext()) {
                Patient patient = new Patient();
                patient.setIdPatient(cursor.getInt(cursor.getColumnIndexOrThrow("id_patient")));
                patient.setNomPatient(cursor.getString(cursor.getColumnIndexOrThrow("nom_patient")));
                patient.setPostnomPatient(cursor.getString(cursor.getColumnIndexOrThrow("postnom_patient")));
                patient.setPrenomPatient(cursor.getString(cursor.getColumnIndexOrThrow("prenom_patient")));
                patient.setAddressPatient(cursor.getString(cursor.getColumnIndexOrThrow("address_patient")));
                patient.setPhoneNumberPatient(cursor.getString(cursor.getColumnIndexOrThrow("phone_number_patient")));
                patient.setMaladiePatient(cursor.getString(cursor.getColumnIndexOrThrow("maladie_patient")));
                patient.setDateVenuPatient(cursor.getString(cursor.getColumnIndexOrThrow("date_venu_patient")));

                long dateSent = cursor.getLong(cursor.getColumnIndexOrThrow("date_sent"));
                String filePath = cursor.getString(cursor.getColumnIndexOrThrow("file_path"));
                String author = cursor.getString(cursor.getColumnIndexOrThrow("author"));

                PatientWithNotes patientWithNotes = new PatientWithNotes(patient, filePath, author, dateSent);
                patientMap.put(patient.getIdPatient(), patientWithNotes);
            }
            cursor.close();
        }
        db.close();
        return patientMap;
    }

    public ArrayList<VocalNote> getVocalNotesByPatientId(int id_patient_vn) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                "vocal_notes",
                null,
                "id_patient_vn =?",
                new String[]{String.valueOf(id_patient_vn)},
                null,
                null,
                null
        );

        ArrayList<VocalNote> vocalNotes = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                VocalNote vocalNote = new VocalNote();
                vocalNote.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id_vocal_note")));
                vocalNote.setIdPatient(cursor.getInt(cursor.getColumnIndexOrThrow("id_patient_vn")));
                vocalNote.setIdPersonnel(cursor.getInt(cursor.getColumnIndexOrThrow("id_personnel_vn")));
                vocalNote.setFilePath(cursor.getString(cursor.getColumnIndexOrThrow("file_path")));
                vocalNote.setAuthor(cursor.getString(cursor.getColumnIndexOrThrow("author")));
                vocalNote.setDateSent(cursor.getString(cursor.getColumnIndexOrThrow("date_sent")));

                vocalNotes.add(vocalNote);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return vocalNotes;
    }

    // Dans DatabaseHelper.java
    public Personnel getPersonnelByPhoneNumber(String phoneNumber) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("personnel", // Table
                new String[]{"id_personnel", "nom_personnel", "postnom_personnel", "prenom_personnel", "adress_personnel",
                        "phone_number_personnel", "specialisation", "password"}, // Colonnes
                "phone_number_personnel=?", // Sélection
                new String[]{String.valueOf(phoneNumber)}, // Valeurs
                null, // Group By

                null, // HAVING
                null, // Order By
                null); // Limit

        if (cursor != null && cursor.moveToFirst()) {
            Personnel personnel = new Personnel();
            personnel.setIdPersonnel(cursor.getInt(cursor.getColumnIndexOrThrow("id_personnel")));
            personnel.setNomPersonnel(cursor.getString(cursor.getColumnIndexOrThrow("nom_personnel")));
            personnel.setPostnomPersonnel(cursor.getString(cursor.getColumnIndexOrThrow("postnom_personnel")));
            personnel.setPrenomPersonnel(cursor.getString(cursor.getColumnIndexOrThrow("prenom_personnel")));
            personnel.setAdressPersonel(cursor.getString(cursor.getColumnIndexOrThrow("adress_personnel")));
            personnel.setPhoneNumberPersonnel(cursor.getString(cursor.getColumnIndexOrThrow("phone_number_personnel")));
            personnel.setSpecialisationPersonnel(cursor.getString(cursor.getColumnIndexOrThrow("specialisation")));
            personnel.setPasswordPersonnel(cursor.getString(cursor.getColumnIndexOrThrow("password")));
            cursor.close();
            return personnel;
        } else {
            cursor.close();
            return null;
        }
    }

    public void addPatient(Patient patient) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nom_patient", patient.getNomPatient());
        values.put("postnom_patient", patient.getPostnomPatient());
        values.put("prenom_patient", patient.getPrenomPatient());
        values.put("address_patient", patient.getAddressPatient());
        values.put("phone_number_patient", patient.getPhoneNumberPatient());
        values.put("maladie_patient", patient.getMaladiePatient());
        if (values.getAsString("nom_patient") != null &&
                values.getAsString("postnom_patient") != null &&
                values.getAsString("prenom_patient") != null &&
                values.getAsString("address_patient") != null &&
                values.getAsString("phone_number_patient") != null &&
                values.getAsString("maladie_patient") != null) {

            long newRowId = db.insert("patients", null, values);
            ArrayList<Patient> currentPatients =  getAllPatientsFromDB();
            currentPatients.add(patient);
            setAllPatients(currentPatients);
        } else {
            // Gérer le cas où une valeur nécessaire est manquante
            throw new IllegalArgumentException("Toutes les informations du patient sont requises.");
        }
        db.close();

    }

    public ArrayList<Patient> getAllPatientsFromDB() {
        ArrayList<Patient> patients = new ArrayList<>();

        String selectQuery = "SELECT  * FROM `patients`";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Patient patient = new Patient();
                patient.setIdPatient(Integer.parseInt(cursor.getString(0)));
                patient.setNomPatient(cursor.getString(1));
                patient.setPostnomPatient(cursor.getString(2));
                patient.setPrenomPatient(cursor.getString(3));
                patient.setAddressPatient(cursor.getString(4));
                patient.setPhoneNumberPatient(cursor.getString(5));
                patient.setMaladiePatient(cursor.getString(6));
                patient.setDateVenuPatient(cursor.getString(7));

                patients.add(patient);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return patients;
    }


    public ArrayList<Patient> getAllPatients() {
        ArrayList<Patient> patients = new ArrayList<>();

        String selectQuery = "SELECT  * FROM `patients` ";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Patient patient = new Patient();
                patient.setIdPatient(Integer.parseInt(cursor.getString(0)));
                patient.setNomPatient(cursor.getString(1));
                patient.setPostnomPatient(cursor.getString(2));
                patient.setPrenomPatient(cursor.getString(3));
                patient.setAddressPatient(cursor.getString(4));
                patient.setPhoneNumberPatient(cursor.getString(5));
                patient.setMaladiePatient(cursor.getString(6));
                patient.setDateVenuPatient(cursor.getString(7));

                patients.add(patient);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return patients;
    }

    private void setAllPatients(ArrayList<Patient> patients) {
        this.patientsLiveData.postValue(patients);
    }

    public LiveData<ArrayList<Patient>> getPatientsLiveData() {
        return patientsLiveData;
    }

    public LiveData<ArrayList<PatientWithNotes>> getPatientsWithNotesLiveData() {
        return patientsWithNotesLiveData;
    }

    public void insertAudioRecord(int patientId, int personnelId, String audioFilePath, String author) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            // Commencez une transaction
            db.beginTransaction();
            try {
                ContentValues contentValues = new ContentValues();
                contentValues.put("id_patient_vn", patientId);
                contentValues.put("id_personnel_vn", personnelId);
                contentValues.put("file_path", audioFilePath);
                contentValues.put("author", author);

                // Insérez le nouvel enregistrement audio
                long newRowId = db.insert("vocal_notes", null, contentValues);

                // Marquez la transaction comme réussie
                db.setTransactionSuccessful();



            } finally {
                // Terminez la transaction
                db.endTransaction();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Fermez la base de données
            if (db.isOpen()) {
                db.close();
            }
        }
    }


}
