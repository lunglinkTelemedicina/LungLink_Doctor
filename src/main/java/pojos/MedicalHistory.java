package pojos;

import java.time.LocalDate;
import java.util.List;

/**
 * Represents a medical history record containing information about a patient's visit.
 * This class stores details such as observations, symptoms, and identifiers for both
 * the client and doctor involved.
 */
public class MedicalHistory {

    /**
     * Unique identifier for the medical record
     */
    private int recordId;
    /**
     * Date when the medical record was created
     */
    private LocalDate date;
    /**
     * List of medical observations made during the visit
     */
    private List<String> observations;
    /**
     * List of symptoms reported by the patient
     */
    private List<String> symptomsList;
    /**
     * Identifier of the client/patient
     */
    private int clientId;
    /**
     * Identifier of the doctor who created the record
     */
    private int doctorId;

    /**
     * Default constructor for MedicalHistory.
     */
    public MedicalHistory() {
        //constructor vacio
    }

    /**
     * Creates a new medical history record with all fields.
     *
     * @param recordId     Unique identifier for the record
     * @param date         Date of the medical record
     * @param observations List of medical observations
     * @param symptomsList List of patient symptoms
     * @param clientId     Identifier of the patient
     * @param doctorId     Identifier of the doctor
     */
    public MedicalHistory(int recordId, LocalDate date, List <String> observations, List<String> symptomsList, int clientId,  int doctorId) {
        this.recordId = recordId;
        this.date = date;
        this.observations = observations;
        this.symptomsList = symptomsList;
        this.clientId = clientId;
        this.doctorId = doctorId;
    }

    /**
     * @return The record's unique identifier
     */
    public int getRecordId() {
        return recordId;
    }

    /**
     * @param recordId The record identifier to set
     */
    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }

    /**
     * @return The date of the medical record
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * @param date The date to set
     */
    public void setDate(LocalDate date) {
        this.date = date;
    }

    /**
     * @return The client's identifier
     */
    public int getClientId() {
        return clientId;
    }

    /**
     * @param clientId The client identifier to set
     */
    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    /**
     * @return List of medical observations
     */
    public List<String> getObservations() {
        return observations;
    }

    /**
     * @param observations The list of observations to set
     */
    public void setObservations(List<String> observations) {
        this.observations = observations;
    }

    /**
     * @return List of patient symptoms
     */
    public List<String> getSymptomsList() {
        return symptomsList;
    }

    /**
     * @param symptomList The list of symptoms to set
     */
    public void setSymptomsList(List<String> symptomList) {
        this.symptomsList = symptomList;
    }

    /**
     * @return The doctor's identifier
     */
    public int getDoctorId() {
        return doctorId;
    }

    /**
     * @param doctorId The doctor identifier to set
     */
    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;}

    @Override
    public String toString() {
        return "MedicalHistory{" +
                "recordId=" + recordId +
                ", date=" + date +
                ", observations='" + observations + '\'' +
                ", symptomsList=" + symptomsList +
                ", clientId=" + clientId +
                ", doctorId=" + doctorId +
                '}';
    }

}
