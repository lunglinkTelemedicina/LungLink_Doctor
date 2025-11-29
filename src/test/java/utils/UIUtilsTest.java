package utils;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import static org.junit.jupiter.api.Assertions.*;

public class UIUtilsTest {

    private final InputStream standardIn = System.in;

    //(System.in)
    private void setSimulatedInput(String data) {
        System.setIn(new ByteArrayInputStream(data.getBytes()));
    }

    @AfterEach
    public void tearDown() {
        // System.in
        System.setIn(standardIn);
    }

    @Test
    void readInt_ValidInput_ReturnsCorrectInt() {
        setSimulatedInput("42\n");
        assertEquals(42, UIUtils.readInt("Enter: "));
    }

    @Test
    void readInt_InvalidThenValidInput_ReturnsCorrectInt() {
        // Test "abc", them "5". UIUtils handle the error and ask again.
        setSimulatedInput("abc\n5\n");
        assertEquals(5, UIUtils.readInt("Enter: "));
    }

    @Test
    void readString_ValidInput_ReturnsTrimmedString() {
        setSimulatedInput("   test string   \n");
        assertEquals("test string", UIUtils.readString("Enter: "));
    }

    @Test
    void readDouble_ValidInput_ReturnsCorrectDouble() {
        setSimulatedInput("3.1415\n");
        // Deltas to compare doubles
        assertEquals(3.1415, UIUtils.readDouble("Enter: "), 0.0001);
    }

    @Test
    void readDouble_InvalidThenValidInput_ReturnsCorrectDouble() {
        // Test introduce "not_a_double", then "1.23"
        setSimulatedInput("not_a_double\n1.23\n");
        assertEquals(1.23, UIUtils.readDouble("Enter: "), 0.0001);
    }
}