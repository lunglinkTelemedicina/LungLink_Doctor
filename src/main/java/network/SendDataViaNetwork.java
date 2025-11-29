package network;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Handles network data transmission using DataOutputStream over a Socket connection.
 * Provides methods to send various data types including integers, strings, and byte arrays.
 */
public class SendDataViaNetwork {

    private DataOutputStream dataOutputStream;
    private Socket socket;

    /**
     * Initializes a new SendDataViaNetwork instance with the specified socket.
     *
     * @param socket The network socket to use for data transmission
     */
    public SendDataViaNetwork(Socket socket) {
        try {
            this.socket = socket;
            this.dataOutputStream = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            System.err.println("Error initializing SendDataViaNetwork: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Sends an integer value over the network connection.
     *
     * @param value The integer to send
     * @throws IOException If an I/O error occurs during transmission
     */
    public void sendInt(int value) throws IOException {
        dataOutputStream.writeInt(value);
        dataOutputStream.flush();
    }

    /**
     * Sends a string message over the network connection.
     *
     * @param message The string to send
     */
    public void sendString(String message) {
        try {
            dataOutputStream.writeUTF(message);
            dataOutputStream.flush();
        } catch (IOException e) {
            System.err.println("Error sending string: " + e.getMessage());
        }
    }

    /**
     * Sends a byte array over the network connection.
     * Transmits the array length followed by the actual data.
     *
     * @param data The byte array to send
     * @throws IOException If an I/O error occurs during transmission
     */
    public void sendBytes(byte[] data) throws IOException {
        dataOutputStream.writeInt(data.length);
        dataOutputStream.write(data);
        dataOutputStream.flush();

    }

    /**
     * Closes and releases all network resources.
     * Handles IOException internally and prints stack trace if error occurs.
     */
    public void releaseResources() {
        try {
            if (dataOutputStream != null) {
                dataOutputStream.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException ex) {
            System.err.println("Error with resources: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    /**
     * Closes the data output stream and socket connection.
     *
     * @throws IOException If an error occurs while closing the resources
     */
    public void close() throws IOException {
        if (dataOutputStream != null) dataOutputStream.close();
        if (socket != null) socket.close();
    }

    // TODO: SEND MedicalHistory cuando este claro como se guardan las signals


}
