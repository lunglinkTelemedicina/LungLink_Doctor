package main;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class DoctorMenuTest {

    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    private DoctorMenu dummyMenu;

    @BeforeEach
    public void setUp() {
        // Capturar System.out
        System.setOut(new PrintStream(outputStreamCaptor));
        // Instancia dummy (no necesitamos conexión ni doctor reales)
        dummyMenu = new DoctorMenu(null, null);
    }

    @AfterEach
    public void tearDown() {
        // Restaurar System.out
        System.setOut(standardOut);
    }

    // Helper para invocar el método privado printGraph
    private void invokePrintGraph(List<Integer> values) throws Exception {
        Method method = DoctorMenu.class.getDeclaredMethod("printGraph", List.class);
        method.setAccessible(true);
        method.invoke(dummyMenu, values);
    }

    @Test
    void printGraph_FlatLine_PrintsRepeatedAsterisks() throws Exception {
        // Valores idénticos deben generar una línea plana
        List<Integer> values = List.of(10, 10, 10, 10, 10);
        invokePrintGraph(values);

        String output = outputStreamCaptor.toString().trim();
        // Verifica el mensaje específico de línea plana
        assertTrue(output.contains("Signal graph (flat line):"));
        // Verifica la línea de 5 asteriscos
        assertTrue(output.contains("*****"));
    }

    @Test
    void printGraph_EmptyList_PrintsErrorMessage() throws Exception {
        List<Integer> values = List.of();
        invokePrintGraph(values);

        String output = outputStreamCaptor.toString().trim();
        assertTrue(output.contains("Cannot draw graph: no signal values."));
    }

    @Test
    void printGraph_AscendingSignal_PrintsShapeCorrectly() throws Exception {
        // 0 (Min), 10 (Med), 20 (Max). Altura=20.
        List<Integer> values = List.of(0, 10, 20);
        invokePrintGraph(values);

        String output = outputStreamCaptor.toString();

        // Verifica que la salida contenga el patrón básico de la escala
        assertTrue(output.contains("SIGNAL GRAPH"));

        // Verificar un patrón en una de las líneas, por ejemplo, el pico:
        // Buscamos una línea con asterisco solo en la última posición (valor 20)
        assertTrue(output.contains("  *")); // En alguna línea
    }
}