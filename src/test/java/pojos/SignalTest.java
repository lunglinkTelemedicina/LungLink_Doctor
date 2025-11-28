package pojos;

import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class SignalTest {

    @Test
    void toByteArray_PositiveValues_ConvertsCorrectly() {
        // Valor 1: 257 (0x0101)
        // Valor 2: 1024 (0x0400)
        List<Integer> values = Arrays.asList(257, 1024);
        Signal signal = new Signal();
        signal.setValues(values);

        byte[] expected = new byte[] {
                (byte) 0x01, (byte) 0x01,
                (byte) 0x04, (byte) 0x00
        };

        byte[] actual = signal.toByteArray();

        assertArrayEquals(expected, actual, "La conversión a byte[] debe ser correcta (2 bytes por int, Big-endian).");
        assertEquals(values.size() * 2, actual.length, "El tamaño del array de bytes debe ser el doble de las muestras.");
    }

    @Test
    void toByteArray_EmptyList_ReturnsEmptyArray() {
        List<Integer> values = List.of();
        Signal signal = new Signal();
        signal.setValues(values);

        byte[] expected = new byte[] {};
        assertArrayEquals(expected, signal.toByteArray());
    }
}