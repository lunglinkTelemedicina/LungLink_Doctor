package main;


import network.DoctorConnection;
import pojos.Client;
import pojos.Doctor;
import services.DoctorService;
import pojos.User;
import utils.UIUtils;

public class Main {



    public static void main(String[] args) {

        DoctorConnection conn = new DoctorConnection();
        DoctorService service = new DoctorService();

        if (!conn.connect("localhost", 9000)) {
            System.out.println("Cannot connect to server.");
            return;
        }

        System.out.println("\nConnected to server.\n");

        User user = null;
        Doctor doctor = null;

        while (user == null) {
            System.out.println("\nWELCOME TO LUNGLINK");
            System.out.println("1. Register user");
            System.out.println("2. Login user");
            System.out.println("3. Exit");

            int option = UIUtils.readInt("Choose an option: ");
            try {
                switch (option) {
                    case 1:
                        user = service.registerUser(conn);
                        break;
                    case 2:
                        user = service.loginUser(conn);
                        break;
                    case 3:
                        conn.disconnect();
                        return;
                    default:
                        System.out.println("Invalid option.");
                }
            }catch(Exception e) {
                System.err.println("Operation failed"+e.getMessage());
            }
        }

        // checking if the client exists
        System.out.println("\nChecking if you already have a doctor profile...");
        conn.sendCommand("CHECK_DOCTOR|" + user.getId());
        String response = conn.receiveResponse();

        if (response == null) {
            System.out.println("Server did not respond.");
            conn.disconnect();
            return;
        }

        try {

            if (response.contains("NO_DOCTOR")) {

                System.out.println("No doctor profile found. Let's create one.");

                doctor = service.createDoctorForUser(user, conn);

                if (doctor == null) {
                    System.out.println("Error creating doctor profile.");
                    conn.disconnect();
                    return;
                }

            } else if (response.startsWith("OK|")) {

                String[] parts = response.split("\\|");

                int doctorId = Integer.parseInt(parts[3]);
                String name = parts[4];
                String surname = parts[5];

                doctor = new Doctor();
                doctor.setDoctorId(doctorId);
                doctor.setName(name);
                doctor.setSurname(surname);
                doctor.setUserId(user.getId());

                System.out.println("Loaded doctor profile for: Dr. " + name + " " + surname);

            } else {
                System.out.println("Unexpected server response: " + response);
                conn.disconnect();
                return;
            }

        } catch (Exception e) {
            System.err.println("Error loading doctor profile: " + e.getMessage());
            conn.disconnect();
            return;
        }



        DoctorMenu menu = new DoctorMenu(conn, doctor);
        menu.displayMenu();
    }


}
