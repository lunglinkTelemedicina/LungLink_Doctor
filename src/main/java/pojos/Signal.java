package pojos;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a biological signal (ECG or EMG) with associated metadata and sample values.
 * Used for storing and transmitting signal data between client and server.
 */
public class Signal {
    /**
     * The type of biological signal (ECG or EMG)
     */
    private TypeSignal type;
    /**
     * The ID of the client/patient this signal belongs to
     */
    private int clientId;
    /**
     * The record ID (used only by server)
     */
    private int recordId;
    /**
     * List of signal sample values
     */
    private List<Integer> values;

    /**
     * Default constructor that initializes an empty signal with no type or client ID.
     */
    public Signal() {
        this.values = new ArrayList<>();
    }

    /**
     * Creates a new signal with specified type and client ID.
     *
     * @param type     The type of signal (ECG or EMG)
     * @param clientId The ID of the client/patient
     */
    public Signal(TypeSignal type, int clientId) {
        this.type = type;
        this.clientId = clientId;
        this.values = new ArrayList<>();
    }

    /**
     * @return The type of signal
     */
    public TypeSignal getType() {
        return type;
    }

    /**
     * @param type The type of signal to set
     */
    public void setType(TypeSignal type) {
        this.type = type;
    }

    /**
     * @return The client/patient ID
     */
    public int getClientId() {
        return clientId;
    }

    /**
     * @param clientId The client/patient ID to set
     */
    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    /**
     * @return The record ID
     */
    public int getRecordId() {
        return recordId;
    }

    /**
     * @param recordId The record ID to set
     */
    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }

    /**
     * @return List of signal values
     */
    public List<Integer> getValues() {
        return values;
    }

    /**
     * @param values List of signal values to set
     */
    public void setValues(List<Integer> values) {
        this.values = values;
    }

    /**
     * Adds a single sample value to the signal.
     *
     * @param sample The sample value to add
     */
    public void addSample(int sample) {
        values.add(sample);
    }

    /**
     * Converts the signal values to a byte array representation.
     * Each sample value is converted to a 2-byte representation.
     *
     * @return Byte array containing the signal data
     */

    public byte[] toByteArray() {
        byte[] data = new byte[values.size() * 2];
        int pos = 0;

        for (int v : values) {
            short s = (short) v;
            data[pos++] = (byte) (s >> 8);   // byte alto
            data[pos++] = (byte) s;          // byte bajo
        }
        return data;
    }
}
