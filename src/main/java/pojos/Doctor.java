package pojos;

import java.util.List;

/**
 * Represents a medical doctor in the system.
 * Contains personal information, professional details, and associated patients.
 */
public class Doctor {
    private int doctorId;
    private String name;
    private String surname;
    private String email;
    private DoctorSpecialty specialty;
    private List<Client> patients;
    private int userId;


    /**
     * Creates a new Doctor with all required fields.
     *
     * @param name      The doctor's first name
     * @param surname   The doctor's last name
     * @param specialty The doctor's medical specialty
     * @param email     The doctor's email address
     * @param doctorId  The unique identifier for the doctor
     * @param userId    The associated user account ID
     */
    public Doctor(String name,String surname, DoctorSpecialty specialty,String email, int doctorId, int userId ) {
        this.name = name;
        this.surname=surname;
        this.specialty = specialty;
        this.email=email;
        this.doctorId = doctorId;
        this.userId = userId;
    }

    /**
     * Default constructor for Doctor.
     * Creates an empty Doctor object.
     */
    public Doctor() {

    }

    /**
     * Gets the doctor's unique identifier.
     *
     * @return The doctor's ID
     */
    public int getDoctorId() {
        return doctorId;
    }

    /**
     * Sets the doctor's unique identifier.
     *
     * @param doctorId The doctor's ID to set
     */
    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    /**
     * Gets the doctor's first name.
     *
     * @return The doctor's first name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the doctor's first name.
     *
     * @param name The first name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the doctor's last name.
     * @return The doctor's last name
     */
    public String getSurname() {
        return surname;
    }

    /**
     * Sets the doctor's last name.
     *
     * @param surname The last name to set
     */
    public void setSurname(String surname) {
        this.surname = surname;
    }

    /**
     * Gets the doctor's email address.
     * @return The doctor's email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the doctor's email address.
     *
     * @param email The email address to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the doctor's medical specialty.
     * @return The doctor's specialty
     */
    public DoctorSpecialty getSpecialty() {
        return specialty;
    }

    /**
     * Sets the doctor's medical specialty.
     *
     * @param specialty The specialty to set
     */
    public void setSpecialty(DoctorSpecialty specialty) {
        this.specialty = specialty;
    }

    /**
     * Gets the list of patients assigned to this doctor.
     * @return List of patients
     */
    public List<Client> getPatients() {
        return patients;
    }

    /**
     * Sets the list of patients for this doctor.
     *
     * @param patients List of patients to assign
     */
    public void setPatients(List<Client> patients) {
        this.patients = patients;
    }

    /**
     * Gets the associated user account ID.
     * @return The user ID
     */
    public int getUserId() {
        return userId;
    }

    /**
     * Sets the associated user account ID.
     *
     * @param userId The user ID to set
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Doctor{" +
                "doctorId=" + doctorId +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", email='" + email + '\'' +
                ", specialty=" + specialty +
                ", patients=" + patients +
                '}';
    }
}
