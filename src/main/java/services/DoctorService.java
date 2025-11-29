package services;
import pojos.*;
import network.DoctorConnection;
import utils.UIUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Service class for handling doctor-related operations including authentication,
 * profile management, and patient data access.
 * Provides methods for doctor registration, login, and accessing patient information.
 */
public class DoctorService {

    /**
     * Retrieves the doctor ID associated with a user ID.
     *
     * @param conn   The connection to the server
     * @param userId The user ID to check
     * @return The doctor ID if found, -1 if no doctor exists for the user
     */
    public int getDoctorId(DoctorConnection conn, int userId) {

        conn.sendCommand("CHECK_DOCTOR|" + userId);
        String response = conn.receiveResponse();
        if (response == null) return -1;

        if (response.contains("NO_DOCTOR")) {
            return -1;
        }

        String[] p = response.split("\\|");
        return Integer.parseInt(p[3]);
    }

    /**
     * Retrieves a list of patients associated with a doctor.
     *
     * @param conn     The connection to the server
     * @param doctorId The ID of the doctor
     * @return List of patient information strings, empty list if no patients found
     */
    public List<String> getDoctorPatients(DoctorConnection conn, int doctorId) {

        conn.sendCommand("GET_DOCTOR_PATIENTS|" + doctorId);
        String response = conn.receiveResponse();

        List<String> result = new ArrayList<>();

        if (response == null) return result;
        if (!response.startsWith("OK|")) return result;

        String payload = response.substring(3);
        String[] patientBlocks = payload.split("#");

        for (String p : patientBlocks) {
            result.add(p);
        }

        return result;
    }

    /**
     * Retrieves the medical history of a specific patient.
     *
     * @param conn     The connection to the server
     * @param doctorId The ID of the requesting doctor
     * @param clientId The ID of the patient
     * @return Patient history as a string, "No response." if server doesn't respond
     */
    public String getPatientHistory(DoctorConnection conn,int doctorId, int clientId) {

        conn.sendCommand("GET_PATIENT_HISTORY_DOCTOR|" + doctorId + "|" + clientId);
        String response = conn.receiveResponse();
        return response == null ? "No response." : response;
    }

    /**
     * Retrieves the medical signals data for a specific patient.
     *
     * @param conn     The connection to the server
     * @param doctorId The ID of the requesting doctor
     * @param clientId The ID of the patient
     * @return Patient signals data as a string, "No response." if server doesn't respond
     */
    public String getPatientSignals(DoctorConnection conn, int doctorId, int clientId) {

        conn.sendCommand("GET_PATIENT_SIGNALS_DOCTOR|" + doctorId + "|" + clientId);
        String response = conn.receiveResponse();
        return response == null ? "No response." : response;
    }


    /**
     * Adds a medical observation to a patient record.
     *
     * @param conn     The connection to the server
     * @param recordId The ID of the medical record
     * @param note     The observation text to add
     */
    public void addObservation(DoctorConnection conn, int recordId, String note) {

        conn.sendCommand("ADD_OBSERVATIONS|" + recordId + "|" + note);
        String response = conn.receiveResponse();

        if (response != null) {
            System.out.println("SERVER: " + response);
        }
    }

    /**
     * Retrieves all record IDs associated with a specific patient.
     *
     * @param conn     The connection to the server
     * @param doctorId The ID of the requesting doctor
     * @param clientId The ID of the patient
     * @return List of record IDs, empty list if no records found
     */
    public List<Integer> getRecordIdsOfPatient(DoctorConnection conn, int doctorId, int clientId) {

        conn.sendCommand("GET_PATIENT_HISTORY_DOCTOR|" + doctorId + "|" + clientId);
        String response = conn.receiveResponse();

        List<Integer> ids = new ArrayList<>();

        if (response == null) return ids;
        if (response.startsWith("ERROR")) return ids;


        String[] lines = response.split("\n");

        for (String line : lines) {
            line = line.trim();
            if (line.startsWith("RECORD_ID:")) {
                String num = line.substring("RECORD_ID:".length()).trim();
                ids.add(Integer.parseInt(num));
            }
        }

        return ids;
    }

    /**
     * Registers a new user in the system.
     *
     * @param conn The connection to the server
     * @return The created User object if successful, null if registration fails
     */
    public User registerUser(DoctorConnection conn) {
        System.out.println("\nREGISTER USER");

        String username = UIUtils.readString("New username: ");
        String password = UIUtils.readString("New password: ");

        String cmd = "REGISTER_USER|" + username + "|" + password;
        conn.sendCommand(cmd);

        String response = conn.receiveResponse();
        if (response == null) {
            System.out.println("No server response");
            return null;
        }

        if (response.startsWith("OK|")) {
            int id = Integer.parseInt(response.split("\\|")[1]);
            System.out.println("User created. ID = " + id);
            return new User(id, username, password);
        }

        System.out.println("Registration failed: " + response);
        return null;
    }

    /**
     * Creates a doctor profile for an existing user.
     *
     * @param user The user to create the doctor profile for
     * @param conn The connection to the server
     * @return The created Doctor object
     * @throws IOException If there's an error during profile creation or server communication
     */
    public Doctor createDoctorForUser(User user, DoctorConnection conn) throws IOException {
        System.out.println("\nCREATE DOCTOR PROFILE");

        String name = UIUtils.readString("Name: ");
        String surname = UIUtils.readString("Surname: ");
        String email = null;//UIUtils.readString("Email: ");
        boolean emailOK = false;

        while (!emailOK) {
            email = UIUtils.readString("Email: ");

            if (!email.contains("@") || !(email.endsWith(".com") || email.endsWith(".es"))) {
                System.out.println("ERROR: Invalid email. Please enter a valid address");
                continue;
            }

            emailOK = true;
        }

        DoctorSpecialty specialty = null;
        while (specialty == null) {
            System.out.println("Specialty:");
            System.out.println("1. CARDIOLOGIST");
            System.out.println("2. NEUROPHYSIOLOGIST");
            System.out.println("3. GENERAL MEDICINE");

            int s = UIUtils.readInt("Choose by typing the number: ");
            switch (s) {
                case 1:
                    specialty = DoctorSpecialty.CARDIOLOGIST;
                    break;
                case 2:
                    specialty = DoctorSpecialty.NEUROPHYSIOLOGIST;
                    break;
                case 3:
                    specialty = DoctorSpecialty.GENERAL_MEDICINE;
                default:
                    System.out.println("\nInvalid option. Please try again. \n");
                    break;
            };
        }


        String cmd = "CREATE_DOCTOR|" +
                user.getId() + "|" +
                name + "|" +
                surname + "|" +
                specialty.name() + "|" +
                email;

        conn.sendCommand(cmd);

        String response = conn.receiveResponse();
        if (response == null) {
            throw new IOException("Server not responding during creation");
        }

        if (response.startsWith("OK|")) {
            try {
                String[]parts = response.split("\\|");
                int doctorId = Integer.parseInt(parts[1]);

                Doctor d = new Doctor();
                d.setDoctorId(doctorId);
                d.setName(name);
                d.setSurname(surname);
                d.setEmail(email);
                d.setSpecialty(specialty);
                d.setUserId(user.getId());

                System.out.println("Doctor profile created. ID = " + doctorId);

                return d;
        }catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            throw new IOException("Invalid server response format during creation: " + response + ". " + e.getMessage(), e);
        }
    }
        throw new IOException("Error creating profile:" + response);
    }

    /**
     * Authenticates a user in the system.
     *
     * @param conn The connection to the server
     * @return The authenticated User object if successful, null if login fails
     */
    public User loginUser(DoctorConnection conn) {
        System.out.println("\nLOGIN USER");

        String username = UIUtils.readString("Username: ");
        String password = UIUtils.readString("Password: ");

        String cmd = "LOGIN_USER|" + username + "|" + password;
        conn.sendCommand(cmd);

        String response = conn.receiveResponse();
        if (response == null) {
            System.out.println("No response from server.");
            return null;
        }

        if (response.startsWith("OK|")) {
            String[] parts = response.split("\\|");
            int id = Integer.parseInt(parts[1]);
            String uname = parts[2];
            return new User(id, uname, password);
        }

        System.out.println("User login failed: " + response);
        return null;
    }
}