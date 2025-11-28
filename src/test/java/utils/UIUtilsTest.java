package utils;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import static org.junit.jupiter.api.Assertions.*;

public class UIUtilsTest {

    private final InputStream standardIn = System.in;

    // Método para simular la entrada del usuario (System.in)
    private void setSimulatedInput(String data) {
        System.setIn(new ByteArrayInputStream(data.getBytes()));
    }

    @AfterEach
    public void tearDown() {
        // Restaurar System.in después de cada prueba
        System.setIn(standardIn);
    }

    @Test
    void readInt_ValidInput_ReturnsCorrectInt() {
        setSimulatedInput("42\n");
        assertEquals(42, UIUtils.readInt("Enter: "));
    }

    @Test
    void readInt_InvalidThenValidInput_ReturnsCorrectInt() {
        // Simula introducir "abc", luego "5". UIUtils maneja el error y vuelve a pedir.
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
        // Usamos un delta para comparar doubles
        assertEquals(3.1415, UIUtils.readDouble("Enter: "), 0.0001);
    }

    @Test
    void readDouble_InvalidThenValidInput_ReturnsCorrectDouble() {
        // Simula introducir "not_a_double", luego "1.23"
        setSimulatedInput("not_a_double\n1.23\n");
        assertEquals(1.23, UIUtils.readDouble("Enter: "), 0.0001);
    }
}