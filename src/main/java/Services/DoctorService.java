package Services;
import pojos.*;
import Network.DoctorConnection;
import utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

public class DoctorService {

    public User loginUser(DoctorConnection conn) {

        System.out.println("\nLOGIN DOCTOR");
        String username = UIUtils.readString("Username: ");
        String password = UIUtils.readString("Password: ");

        conn.sendCommand("LOGIN_USER|" + username + "|" + password);
        String response = conn.receiveResponse();

        if (response == null) return null;

        if (response.startsWith("OK|")) {
            String[] p = response.split("\\|");
            int userId = Integer.parseInt(p[1]);
            String uname = p[2];
            return new User(userId, uname, password);
        }

        System.out.println("Login failed: " + response);
        return null;
    }

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

}

