package services;
import pojos.*;
import network.DoctorConnection;
import utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

import static pojos.DoctorSpecialty.*;

public class DoctorService {

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

    public String getPatientHistory(DoctorConnection conn, int clientId) {

        conn.sendCommand("GET_PATIENT_HISTORY_DOCTOR|" + clientId);
        String response = conn.receiveResponse();
        return response == null ? "No response." : response;
    }

    public String getPatientSignals(DoctorConnection conn, int clientId) {

        conn.sendCommand("GET_PATIENT_SIGNALS|" + clientId);
        String response = conn.receiveResponse();
        return response == null ? "No response." : response;
    }


    public void addObservation(DoctorConnection conn, int recordId, String note) {

        conn.sendCommand("ADD_DOCTOR_NOTE|" + recordId + "|" + note);
        String response = conn.receiveResponse();

        if (response != null) {
            System.out.println("SERVER: " + response);
        }
    }

    public List<Integer> getRecordIdsOfPatient(DoctorConnection conn, int clientId) {

        conn.sendCommand("GET_PATIENT_HISTORY_DOCTOR|" + clientId);
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

    public void createDoctorForUser(User user, DoctorConnection conn) {
        System.out.println("\nCREATE DOCTOR PROFILE");

        String name = UIUtils.readString("Name: ");
        String surname = UIUtils.readString("Surname: ");
        String email = UIUtils.readString("Email: ");

        System.out.println("Specialty:");
        System.out.println("1. CARDIOLOGIST");
        System.out.println("2. RHEUMATOLOGIST");
        System.out.println("3. PNEUMATOLOGIST");

        int s = UIUtils.readInt("Choose: ");
        String specialty;
        switch (s) {
            case 1:
                specialty = "CARDIOLOGIST";
                break;
            case 2:
                specialty = "RHEUMATOLOGIST";
                break;
            case 3:
                specialty = "PNEUMATOLOGIST";
                break;
            default:
                System.out.println("Invalid option. Defaulting to GENERAL_MEDICINE.");
                specialty = "GENERAL_MEDICINE";
                break;
        };

        String cmd = "CREATE_DOCTOR|" +
                user.getId() + "|" +
                name + "|" +
                surname + "|" +
                specialty + "|" +
                email;

        conn.sendCommand(cmd);

        String response = conn.receiveResponse();
        if (response == null) {
            System.out.println("Server not responding.");
            return;
        }

        if (response.startsWith("OK|")) {
            System.out.println("Doctor profile created successfully.");
            return;
        }

        System.out.println("Error creating doctor: " + response);
    }

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

