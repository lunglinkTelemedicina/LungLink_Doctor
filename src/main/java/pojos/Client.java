package pojos;

import java.time.LocalDate;
import java.util.List;

/**
 * Represents a client/patient in the medical system.
 * Contains personal information, medical history, and physical measurements.
 */
public class Client {


    private int clientId;
    private String name;
    private String surname;
    private LocalDate dob;
    private String mail;
    private Sex sex;
    private List<MedicalHistory> medicalHistory;
    private double weight;
    private double height;
    private int doctorId;
    private int  userId;

    /**
     * Default constructor for Client.
     */
    public Client(){

    }

    /**
     * Constructs a fully initialized Client object.
     *
     * @param clientId       Unique identifier for the client
     * @param name           Client's first name
     * @param surname        Client's last name
     * @param dob            Client's date of birth
     * @param mail           Client's email address
     * @param sex            Client's biological sex
     * @param medicalHistory List of client's medical history records
     * @param weight         Client's weight in kilograms
     * @param height         Client's height in meters
     * @param doctorId       ID of the assigned doctor
     * @param userId         Associated user account ID
     */
    public Client(int clientId, String name, String surname, LocalDate dob, String mail, Sex sex, List<MedicalHistory> medicalHistory, double weight, double height, int doctorId, int userId) {
        this.clientId = clientId;
        this.name = name;
        this.surname = surname;
        this.dob = dob;
        this.mail = mail;
        this.sex = sex;
        this.medicalHistory = medicalHistory;
        this.weight = weight;
        this.height = height;
        this.doctorId = doctorId;
        this.userId = userId;
    }

    /**
     * Constructs a Client object with basic information.
     *
     * @param clientId Unique identifier for the client
     * @param name     Client's first name
     * @param surname  Client's last name
     * @param dob      Client's date of birth
     * @param mail     Client's email address
     * @param sex      Client's biological sex
     * @param o        Additional object parameter
     */
    public Client(int clientId, String name, String surname, LocalDate dob, String mail, Sex sex, Object o) {

    }

    /**
     * Gets the client's unique identifier.
     *
     * @return The client ID
     */
    public int getClientId() {
        return clientId;
    }

    /**
     * Sets the client's unique identifier.
     *
     * @param clientId The client ID to set
     */
    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public LocalDate getDob() { return dob; }

    public void setDob(LocalDate dob) { this.dob = dob; }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }

    public List<MedicalHistory> getMedicalHistory() {
        return medicalHistory;
    }

    public void setMedicalHistory(List<MedicalHistory> medicalHistory) {
        this.medicalHistory = medicalHistory;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Client{" +
                "clientId=" + clientId +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", dob=" + dob +
                ", mail='" + mail + '\'' +
                ", sex=" + sex +
                ", medicalHistory=" + medicalHistory +
                ", weight=" + weight +
                ", height=" + height +
                ", doctorId=" + doctorId +
                ", userId=" + userId +
                '}';
    }
}
