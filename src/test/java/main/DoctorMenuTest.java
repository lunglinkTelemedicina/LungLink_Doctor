package main;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pojos.TypeSignal;

import java.io.ByteArrayOutputStream;
import java.io.File;
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
    // Helper para invocar el nuevo método privado generatePngGraph mediante reflexión
    private File invokeGeneratePngGraph(List<Integer> values, String title, TypeSignal type) throws Exception {
        // Firma del método: private File generatePngGraph(List<Integer> values, String title, TypeSignal typeEnum)
        Method method = DoctorMenu.class.getDeclaredMethod("generatePngGraph", List.class, String.class, TypeSignal.class);
        method.setAccessible(true);
        File tempFile = (File) method.invoke(dummyMenu, values, title, type);
        return tempFile;
    }

    @Test
    void generatePngGraph_ECGValues_CreatesNonEmptyPngFile() throws Exception {
        // Prueba la creación de un PNG con valores típicos de ECG
        List<Integer> values = List.of(10, 50, 100, 50, 10);
        String title = "ECG Test Signal";

        File resultFile = invokeGeneratePngGraph(values, title, TypeSignal.ECG);

        assertNotNull(resultFile, "Debe devolver un objeto File.");
        assertTrue(resultFile.exists(), "El archivo PNG debe haber sido creado.");
        assertTrue(resultFile.length() > 0, "El archivo PNG no debe estar vacío.");
        assertTrue(resultFile.getName().endsWith(".png"), "El archivo debe ser un PNG.");
    }

    @Test
    void generatePngGraph_EMGFlatLine_CreatesNonEmptyPngFile() throws Exception {
        // Prueba la creación de un PNG con valores planos de EMG
        List<Integer> values = List.of(20, 20, 20, 20);
        String title = "EMG Flat Test Signal";

        File resultFile = invokeGeneratePngGraph(values, title, TypeSignal.EMG);

        assertNotNull(resultFile);
        assertTrue(resultFile.exists());
        assertTrue(resultFile.length() > 0);
        assertTrue(resultFile.getName().endsWith(".png"));
    }

    @Test
    void generatePngGraph_EmptyList_CreatesNonEmptyPngFile() throws Exception {
        // Prueba que se crea un archivo PNG incluso con una lista de valores vacía,
        // ya que la implementación maneja esto asignando un rango por defecto (max=1, min=0).
        List<Integer> values = List.of();
        String title = "Empty Signal";

        File resultFile = invokeGeneratePngGraph(values, title, TypeSignal.ECG);

        assertNotNull(resultFile);
        assertTrue(resultFile.exists());
        assertTrue(resultFile.length() > 0, "El archivo PNG debe ser generado incluso con una lista vacía de muestras.");
    }
}
