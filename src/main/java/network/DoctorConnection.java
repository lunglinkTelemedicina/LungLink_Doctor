package network;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;

public class DoctorConnection {

    private Socket socket;
    private DataOutputStream dataOut;
    private DataInputStream dataIn;

    public boolean connect(String ip, int port){
        try{
            socket = new Socket(ip, port);
            dataOut = new DataOutputStream(socket.getOutputStream());
            dataIn = new DataInputStream(socket.getInputStream());
            return true;

        }catch(IOException e){
            System.out.println("Connection failed: " + e.getMessage());
            return false;
        }
    }

    public void sendCommand(String msg){
        try{
            dataOut.writeUTF(msg);
            dataOut.flush();
        }catch(IOException e){
            System.out.println("Send failed: " + e.getMessage());
        }
    }

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

    public void sendBytes(byte[] data){
        try{
            dataOut.writeInt(data.length);
            dataOut.write(data);
            dataOut.flush();
        }catch(IOException e){
            System.out.println("Error sending bytes: " + e.getMessage());
        }
    }

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
