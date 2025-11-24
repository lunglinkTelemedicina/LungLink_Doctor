package main;


import network.DoctorConnection;
import services.DoctorService;
import pojos.User;

public class Main {



    public static void main(String[] args) {

        DoctorConnection conn = new DoctorConnection();
        DoctorService service = new DoctorService();

        if (!conn.connect("10.60.96.46", 9000)) {
            System.out.println("Cannot connect to server.");
            return;
        }

        User user = service.loginUser(conn);
        if (user == null) {
            conn.disconnect();
            return;
        }

        int doctorId = service.getDoctorId(conn, user.getId());
        if (doctorId == -1) {
            System.out.println("This user is NOT a doctor. Closing...");
            conn.disconnect();
            return;
        }

        DoctorMenu menu = new DoctorMenu(conn, doctorId);
        menu.displayMenu();
    }


}
