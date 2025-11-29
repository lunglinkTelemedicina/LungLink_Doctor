package network;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;

/**
 * Manages network communication between the doctor client and server.
 * Handles connection establishment, data transfer, and resource cleanup.
 */
public class DoctorConnection {

    private Socket socket;
    private DataOutputStream dataOut;
    private DataInputStream dataIn;

    /**
     * Establishes a connection to the server.
     *
     * @param ip   The IP address of the server
     * @param port The port number to connect to
     * @return true if connection successful, false otherwise
     */
    /**
     * Establishes a connection to the server.
     * Creates socket and initializes input/output streams.
     *
     * @param ip   The IP address of the server
     * @param port The port number to connect to
     * @return true if connection successful, false otherwise
     */
    public boolean connect(String ip, int port) {
        try {
            socket = new Socket(ip, port);
            dataOut = new DataOutputStream(socket.getOutputStream());
            dataIn = new DataInputStream(socket.getInputStream());
            return true;

        } catch (IOException e) {
            System.out.println("Connection failed: " + e.getMessage());
            return false;
        }
    }

    /**
     * Sends a command string to the server.
     *
     * @param msg The command message to send
     */
    public void sendCommand(String msg){
        try{
            dataOut.writeUTF(msg);
            dataOut.flush();
        }catch(IOException e){
            System.out.println("Send failed: " + e.getMessage());
        }
    }

    /**
     * Receives a response from the server.
     * Handles server shutdown and connection loss scenarios.
     *
     * @return The response string from server, or null if error occurs
     */
    public String receiveResponse() {
        try {
            String reply = dataIn.readUTF();

            if (reply.equals("SERVER_SHUTDOWN")) {
                System.out.println("Server is shutting down. Closing doctor client...");
                releaseResources();
                System.exit(0);
                //return "SERVER_SHUTDOWN";
            }

            return reply;

        } catch (IOException e) {
            System.out.println("Lost connection to server. Exiting doctor client.");
            releaseResources();
            System.exit(0);
            //return "CONNECTION_LOST";
        }
        return null;
    }

    /**
     * Sends binary data to the server.
     *
     * @param data The byte array to send
     */
    public void sendBytes(byte[] data){
        try{
            dataOut.writeInt(data.length);
            dataOut.write(data);
            dataOut.flush();
        }catch(IOException e){
            System.out.println("Error sending bytes: " + e.getMessage());
        }
    }

    /**
     * Disconnects from the server and releases resources.
     * Sends a disconnect command before closing the connection.
     */
    public void disconnect() {
        try {
            sendCommand("DISCONNECT");
        } catch (Exception ignored) {}

        releaseResources();
        System.out.println("Doctor disconnected.");
    }

    private void releaseResources() {
        try {
            if (dataIn != null) dataIn.close();
        } catch (IOException ignored) {}

        try {
            if (dataOut != null) dataOut.close();
        } catch (IOException ignored) {}

        try {
            if (socket != null) socket.close();
        } catch (IOException ignored) {}
    }

    /**
     * Requests a signal file from the server for a specific signal and client.
     *
     * @param signalId The ID of the signal to request
     * @param clientId The ID of the client the signal belongs to
     * @return The received signal file, or null if request fails
     */
    public File requestSignalFile(int signalId, int clientId) {
        try {
            sendCommand("GET_SIGNAL_FILE|" + signalId + "|" + clientId);

            String header = receiveResponse();
            if (header == null || !header.startsWith("OK|SENDING_FILE")) {
                return null;
            }

            String[] parts = header.split("\\|");
            int length = Integer.parseInt(parts[2]);

            byte[] data = receiveBytesSignal(length);

            File temp = File.createTempFile("signal_", ".csv");
            Files.write(temp.toPath(), data);

            return temp;

        } catch (Exception e) {
            System.out.println("Error receiving file: " + e.getMessage());
            return null;
        }
    }

    /**
     * Receives binary data of specified size from the server.
     *
     * @param size The number of bytes to receive
     * @return The received byte array, or null if reception fails
     */
    public byte[] receiveBytesSignal(int size) {
        try {
            byte[] buffer = new byte[size];
            dataIn.readFully(buffer);   // <-- ESTA ES LA CLAVE
            return buffer;

        } catch (IOException e) {
            System.out.println("Error receiving bytes: " + e.getMessage());
            return null;
        }
    }
}
