package main;

import network.DoctorConnection;
import pojos.Doctor;
import pojos.TypeSignal;
import services.DoctorService;
import utils.UIUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;


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
        if(p == null || p.isBlank()) continue; //avoid blank prints

        String[] f = p.split(";");
        System.out.println("ID: " + f[0] +
                        " | Name: " + f[1] + " " + f[2] +
                        " | DOB: " + f[3] +
                        " | Sex: " + f[4] +
                        " | Mail: " + f[5]);
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
            System.out.println(result);
        }
    }

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
            if (p == null || p.isBlank()) continue;

            String[] f = p.split(";");
            System.out.println(
                    "ID: " + f[0] +
                            " | Name: " + f[1] + " " + f[2] +
                            " | DOB: " + f[3] +
                            " | Sex: " + f[4] +
                            " | Mail: " + f[5]
            );
        }

        //choose patient
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
                System.out.println(result);
            } else {
                System.out.println("\nAvailable signals for this patient: \n");
                System.out.println(result);
            }
        }


        // choose the signal of the patient
        int signalId = -1;
        File f = null;
        TypeSignal typeEnum = null;

        while (f == null) {
            signalId = UIUtils.readInt("\nEnter SIGNAL_ID to download/open (or 0 to cancel): ");

            if (signalId == 0) {
                System.out.println("Signal download cancelled. Returning to menu.");
                return;
            }

            f = conn.requestSignalFile(signalId, clientId);

            if (f == null) {
                System.out.println("ERROR: Signal ID " + signalId +
                        " not found or download failed. Please try a valid ID.");
            }
        }

        System.out.println("File downloaded at: " + f.getAbsolutePath());

        String[] lines = result.split("\n");
        for (String line : lines) {
            if (line.contains("SIGNAL_ID: " + signalId))
                continue;

                String[] parts2 = line.split("\\|");
                for (String m : parts2) {
                    m = m.trim();
                    if (m.startsWith("TYPE:")) {
                        String raw = m.substring("TYPE:".length()).trim();
                        try {
                            typeEnum = TypeSignal.valueOf(raw);
                        } catch (Exception e) {
                            System.out.println("ERROR: No puedo convertir '" + raw + "' a TypeSignal");
                        }
                    }
                }

        }


        // get patient details
        String patientName = "";
        String patientSurname = "";

        for (String p : patients) {
            if (p == null || p.isBlank()) continue;

            String[] f2 = p.split(";");
            int id = Integer.parseInt(f2[0]);

            if (id == clientId) {
                patientName = f2[1];
                patientSurname = f2[2];
                break;
            }
        }


        // read file
        List<Integer> samples = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line = br.readLine();

            if (line == null) {
                System.out.println("Empty signal file.");
                return;
            }

            String[] parts = line.split(",");

            for (String p : parts) {
                if (!p.isBlank()) {
                    try {
                        samples.add(Integer.parseInt(p.trim()));
                    } catch (Exception e) {
                        System.out.println("Invalid number \"" + p + "\". Skipping.");
                    }
                }
            }

            if (samples.isEmpty()) {
                System.out.println("No valid samples found.");
                return;
            }

        } catch (Exception e) {
            System.out.println("Error reading downloaded file: " + e.getMessage());
            return;
        }


        // title for png
        String title =
                "Signal " + signalId +
                        " | " + typeEnum +
                        " | Patient: " + patientName + " " + patientSurname +
                        " (ID " + clientId + ")";


        // generate png
        try {
            File png = generatePngGraph(samples, title, typeEnum);
            System.out.println("PNG generated at: " + png.getAbsolutePath());
            Desktop.getDesktop().open(png);

        } catch (Exception e) {
            System.out.println("Error generating PNG: " + e.getMessage());
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
                System.out.println("ERROR: You do not have a client with ID " + clientId + " assigned. Please choose an ID from the list.");
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

            System.out.println("ERROR: Invalid RECORD_ID " + recordId + ". Please choose an ID from the list above.");
        }


        String observation = UIUtils.readString("Observation to add: ");
        service.addObservation(conn, recordId, observation);

        System.out.println("Observation added successfully.");
    }

    private File generatePngGraph(List<Integer> values, String title, TypeSignal typeEnum) throws IOException {

        int width = 900;
        int height = 500;

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();

        // fondo
        g.setColor(new Color(200, 200, 200));
        g.fillRect(0, 0, width, height);

        // lineas finas
        g.setColor(new Color(170, 170, 170));
        for (int x = 0; x < width; x += 10) g.drawLine(x, 0, x, height);
        for (int y = 0; y < height; y += 10) g.drawLine(0, y, width, y);

        // lineas gruesas
        g.setColor(new Color(140, 140, 140));
        for (int x = 0; x < width; x += 50) g.drawLine(x, 0, x, height);
        for (int y = 0; y < height; y += 50) g.drawLine(0, y, width, y);


        // title
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString(title, 40, 30);


        // signal
        int max = values.stream().max(Integer::compareTo).orElse(1);
        int min = values.stream().min(Integer::compareTo).orElse(0);

        int graphHeight = height - 100;
        double scaleY = (double) graphHeight / (max - min == 0 ? 1 : max - min);
        double scaleX = (double) (width - 80) / values.size();


        // signal color
        if (typeEnum == TypeSignal.ECG)
            g.setColor(new Color(0,200,0));
        else
            g.setColor(new Color(220,0,0));



        // plot signal
        int base = height - 50;

        for (int i = 0; i < values.size() - 1; i++) {
            int x1 = (int) (40 + i * scaleX);
            int y1 = (int) (base - (values.get(i) - min) * scaleY);

            int x2 = (int) (40 + (i + 1) * scaleX);
            int y2 = (int) (base - (values.get(i + 1) - min) * scaleY);

            g.drawLine(x1, y1, x2, y2);
        }

        g.dispose();

        // temporary png
        File png = File.createTempFile("signal_", ".png");
        ImageIO.write(image, "png", png);

        return png;
    }


}