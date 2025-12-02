package tests;

import network.DoctorConnection;

public class TestSocketsMain {

    public static void main(String[] args) {
        System.out.println("SOCKET COMMUNICATION TEST");

        DoctorConnection doctorConnection = new DoctorConnection();

        try {
            boolean connected = doctorConnection.connect("localhost", 9000);

            if (!connected) {
                System.out.println("Failed to connect to server.");
                return;
            }

            System.out.println("Connected to server!");

            String username = "AlfredoJimenez";
            String password = "doctor123";

            String command = "LOGIN_USER|" + username + "|" + password;

            doctorConnection.sendCommand(command);
            System.out.println("Sent: " + command);

            String response = doctorConnection.receiveResponse();
            System.out.println("Received: " + response);

            doctorConnection.disconnect();

            System.out.println("Doctor socket test completed.");

        } catch (Exception e) {
            System.out.println("Doctor socket test failed: " + e.getMessage());
        }
    }
}
