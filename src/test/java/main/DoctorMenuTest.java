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
        // Capture System.out
        System.setOut(new PrintStream(outputStreamCaptor));
        // instance dummy
        dummyMenu = new DoctorMenu(null, null);
    }

    @AfterEach
    public void tearDown() {
        // System.out
        System.setOut(standardOut);
    }
    // generatePngGraph
    private File invokeGeneratePngGraph(List<Integer> values, String title, TypeSignal type) throws Exception {
        // private File generatePngGraph(List<Integer> values, String title, TypeSignal typeEnum)
        Method method = DoctorMenu.class.getDeclaredMethod("generatePngGraph", List.class, String.class, TypeSignal.class);
        method.setAccessible(true);
        File tempFile = (File) method.invoke(dummyMenu, values, title, type);
        return tempFile;
    }

    @Test
    void generatePngGraph_ECGValues_CreatesNonEmptyPngFile() throws Exception {
        // Test PNG with typical values of ECG
        List<Integer> values = List.of(10, 50, 100, 50, 10);
        String title = "ECG Test Signal";

        File resultFile = invokeGeneratePngGraph(values, title, TypeSignal.ECG);

        assertNotNull(resultFile, "It must return an object file.");
        assertTrue(resultFile.exists(), "PNG must been created.");
        assertTrue(resultFile.length() > 0, "PNG cant be null or empty.");
        assertTrue(resultFile.getName().endsWith(".png"), "The file must be a PNG.");
    }

    @Test
    void generatePngGraph_EMGFlatLine_CreatesNonEmptyPngFile() throws Exception {
        // Test PNG with typical values of ECG
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
        // Test if a PNG file is created even if its empty
        // (max=1, min=0).
        List<Integer> values = List.of();
        String title = "Empty Signal";

        File resultFile = invokeGeneratePngGraph(values, title, TypeSignal.ECG);

        assertNotNull(resultFile);
        assertTrue(resultFile.exists());
        assertTrue(resultFile.length() > 0, "PNG file must be created even if its empty.");
    }
}
