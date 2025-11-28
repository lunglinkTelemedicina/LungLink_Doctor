package main;

import network.DoctorConnection;
import pojos.Doctor;
import services.DoctorService;
import utils.UIUtils;

import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DoctorMenu {

    private final DoctorConnection conn;
    private final DoctorService service;
    private final Doctor doctor;

    public DoctorMenu(DoctorConnection conn, Doctor doctor) {
        this.conn = conn;
        this.service = new DoctorService();
        this.doctor = doctor;
    }

    public void displayMenu() {

        boolean exit = false;

        while (!exit) {
            System.out.println("\nDOCTOR MENU\n");
            System.out.println("1. View my patients");
            System.out.println("2. View patient history");
            System.out.println("3. View patient signals");
            System.out.println("4. Add observation");
            System.out.println("5. Disconnect\n");

            int opt = UIUtils.readInt("Choose option: ");

            switch (opt) {
                case 1:
                    viewPatients();
                    break;
                case 2:
                    viewHistory();
                    break;
                case 3:
                    viewSignals();
                    break;
                case 4:
                    addObservation();
                    break;
                case 5:
                    conn.disconnect();
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    private void viewPatients() {

        List<String> patients = service.getDoctorPatients(conn, doctor.getDoctorId());

        if (patients.isEmpty()) {
            System.out.println("No patients assigned.");
            return;
        }

        System.out.println("\nPATIENT LIST\n");

        for (String p : patients) {
            String[] f = p.split(";");

            System.out.println(
                    "ID: " + f[0] +
                            " | Name: " + f[1] + " " + f[2] +
                            " | DOB: " + f[3] +
                            " | Sex: " + f[4] +
                            " | Mail: " + f[5]
            );
        }
    }


private void viewHistory() {

    List<String> patients = service.getDoctorPatients(conn, doctor.getDoctorId());

    if (patients.isEmpty()) {
        System.out.println("No patients assigned.");
        return;
    }

    System.out.println("\nPATIENT LIST\n");
    for (String p : patients) {
        String[] f = p.split(";");
        // Asegúrate de que el código aquí dentro imprime la lista de pacientes
        System.out.println(
                "ID: " + f[0] +
                        " | Name: " + f[1] + " " + f[2] +
                        " | DOB: " + f[3] +
                        " | Sex: " + f[4] +
                        " | Mail: " + f[5]
        );
    }

    // ask again until the doctor chooses a valid assigned patient

    String result = "ERROR"; // Initialize to enter the loop
    int clientId = -1;

    while (result.startsWith("ERROR")) {

        clientId = UIUtils.readInt("\nEnter the ID of the patient you want to see (or 0 to cancel): ");

        if (clientId == 0) {
            System.out.println("History search cancelled. Returning to menu.");
            return;
        }

        //call the server and gets the history or error
        result = service.getPatientHistory(conn, doctor.getDoctorId(), clientId);

        if (result.startsWith("ERROR")) {
            System.err.println(result);
        }
    }
    // Loop exits successfully here

    //The result (patients history) prints if it's not an error
    System.out.println(result);
}

    private void viewSignals() {

        List<String> patients = service.getDoctorPatients(conn, doctor.getDoctorId());

        if (patients.isEmpty()) {
            System.out.println("No patients assigned.");
            return;
        }

        System.out.println("\nPATIENT LIST\n");
        for (String p : patients) {
            String[] f = p.split(";");

            System.out.println(
                    "ID: " + f[0] +
                            " | Name: " + f[1] + " " + f[2] +
                            " | DOB: " + f[3] +
                            " | Sex: " + f[4] +
                            " | Mail: " + f[5]
            );
        }

        String result = "ERROR";
        int clientId = -1;

        while (result.startsWith("ERROR")) {

            clientId = UIUtils.readInt("\nEnter the ID of the patient you want to see (or 0 to cancel): ");

            if (clientId == 0) {
                System.out.println("Signal search cancelled. Returning to menu.");
                return;
            }
            result = service.getPatientSignals(conn, doctor.getDoctorId(), clientId);

            if (result.startsWith("ERROR")) {
                System.err.println(result);
            } else {
                System.out.println(result);
            }
        }
        int signalId = -1;
        File f = null;

        while (f == null) {
            signalId = UIUtils.readInt("\nEnter SIGNAL_ID to download/open (or 0 to cancel): ");

            if (signalId == 0) {
                System.out.println("Signal download cancelled. Returning to menu.");
                return;
            }

            f = conn.requestSignalFile(signalId,clientId);

            if (f == null) {
                System.err.println("ERROR: Signal ID " + signalId + " not found or download failed. Please try a valid ID.");
            }
        }

        System.out.println("File downloaded at: " + f.getAbsolutePath());

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line = br.readLine();
            if (line == null) {
                System.out.println("Empty signal file.");
                return;
            }

            String[] parts = line.split(",");
            List<Integer> samples = new ArrayList<>();

            for (String p : parts) {
                if (!p.isBlank())
                    samples.add(Integer.parseInt(p.trim()));
            }

            System.out.println("\nRAW SIGNAL VALUES\n");
            System.out.println(samples);

            printGraph(samples);


        } catch (Exception e) {
            System.out.println("Error reading downloaded file: " + e.getMessage());
        }

    }

    private void printGraph(List<Integer> values) {

        int max = values.stream().max(Integer::compareTo).orElse(1);
        int min = values.stream().min(Integer::compareTo).orElse(0);
        int height = 20;

        // Escala
        double scale = (double) (max - min) / height;

        for (int row = height; row >= 0; row--) {
            double threshold = min + row * scale;

            StringBuilder line = new StringBuilder();

            for (int v : values) {
                if (v >= threshold)
                    line.append("*");
                else
                    line.append(" ");
            }

            System.out.println(line.toString());
        }
    }

    private void addObservation() {

        List<String> patients = service.getDoctorPatients(conn, doctor.getDoctorId());

        if (patients.isEmpty()) {
            System.out.println("No patients assigned.");
            return;
        }

        System.out.println("\nPATIENT LIST\n");
        Set<Integer> validClientIds = new HashSet<>();

        for (String p : patients) {
            String[] f = p.split(";");
            int patientId = Integer.parseInt(f[0]);
            validClientIds.add(patientId);

            System.out.println(
                    "ID: " + patientId +
                            " | Name: " + f[1] + " " + f[2] +
                            " | DOB: " + f[3] +
                            " | Sex: " + f[4] +
                            " | Mail: " + f[5]
            );
        }

        int clientId = -1;
        boolean idIsValid = false;

        while (!idIsValid) {
            clientId = UIUtils.readInt("\nEnter patient ID to observe: ");

            if (validClientIds.contains(clientId)) {
                idIsValid = true;
            } else {
                System.err.println("ERROR: You do not have a client with ID " + clientId + " assigned. Please choose an ID from the list.");
                // El bucle while continuará.
            }
        }

        List<Integer> recordIds = service.getRecordIdsOfPatient(conn, doctor.getDoctorId(), clientId);

        if (recordIds.isEmpty()) {
            System.out.println("Client ID " + clientId + " has no medical history records.");
            return;
        }

        System.out.println("\nAvailable medical histories for this patient:");
        for (int r : recordIds) {
            System.out.println("RECORD_ID: " + r);
        }

        int recordId;
        while (true) {
            recordId = UIUtils.readInt("\nType the RECORD_ID in which you want to make any observation: ");

            if (recordIds.contains(recordId)) {
                break;
            }

            System.err.println("ERROR: Invalid RECORD_ID " + recordId + ". Please choose an ID from the list above.");
        }


        String observation = UIUtils.readString("Observation to add: ");
        service.addObservation(conn, recordId, observation);

        System.out.println("Observation added successfully.");
    }
}