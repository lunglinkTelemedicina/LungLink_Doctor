package Main;

import Network.DoctorConnection;
import Services.DoctorService;
import pojos.ClientView;
import utils.UIUtils;

import java.util.List;

public class DoctorMenu {


    private final DoctorConnection conn;
    private final DoctorService service;
    private final int doctorId;

    public DoctorMenu(DoctorConnection conn, int doctorId) {
        this.conn = conn;
        this.service = new DoctorService();
        this.doctorId = doctorId;
    }

    public void displayMenu() {

        boolean exit = false;

        while (!exit) {
            System.out.println("\nDOCTOR MENU\n");
            System.out.println("1. View my patients\n");
            System.out.println("2. View patient history\n");
            System.out.println("3. View patient signals\n");
            System.out.println("4. Add observation\n");
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

        List<String> patients = service.getDoctorPatients(conn, doctorId);

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
        viewPatients();
        int clientId = UIUtils.readInt("\nEnter the ID of the patient you want to see: ");
        String result = service.getPatientHistory(conn, clientId);
        System.out.println(result);
    }

    private void viewSignals() {
        viewPatients();
        int clientId = UIUtils.readInt("\nEnter the ID of the patient you want to see: ");
        String result = service.getPatientSignals(conn, clientId);
        System.out.println(result);
    }

    private void addObservation() {


        viewPatients();
        int clientId = UIUtils.readInt("\nEnter patient ID to annotate: ");
        List<Integer> recordIds = service.getRecordIdsOfPatient(conn, clientId);

        if (recordIds.isEmpty()) {
            System.out.println("This patient has no medical history.");
            return;
        }

        System.out.println("\nAvailable medical histories for this patient:");
        for (int r : recordIds) {
            System.out.println(" - " + r);
        }

        int recordId = UIUtils.readInt("\nChoose the record you want to see (type its id): ");

        if (!recordIds.contains(recordId)) {
            System.out.println("Invalid RECORD_ID.");
            return;
        }

        String observation = UIUtils.readString("Observation to add: ");
        service.addObservation(conn, recordId, observation);
    }
}
