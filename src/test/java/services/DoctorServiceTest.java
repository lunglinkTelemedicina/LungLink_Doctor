package services;

import network.DoctorConnection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import pojos.Doctor;
import pojos.DoctorSpecialty;
import pojos.User;
import utils.UIUtils;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DoctorServiceTest {

    private DoctorService service;

    @Mock
    private DoctorConnection mockConn;

    @BeforeEach
    void setUp() {
        service = new DoctorService();
    }

    // UIUtils

    @Test
    void registerUser_Success_ReturnsNewUser() {
        try (MockedStatic<UIUtils> mockedUI = mockStatic(UIUtils.class)) {
            // test static entries form the user
            mockedUI.when(() -> UIUtils.readString("New username: ")).thenReturn("testUser");
            mockedUI.when(() -> UIUtils.readString("New password: ")).thenReturn("testPass");

            // test server response (OK|)
            when(mockConn.receiveResponse()).thenReturn("OK|101");

            // Act
            User result = service.registerUser(mockConn);

            // Assert
            verify(mockConn).sendCommand("REGISTER_USER|testUser|testPass");
            assertNotNull(result);
            assertEquals(101, result.getId());
        }
    }

    @Test
    void createDoctorForUser_Success_ReturnsNewDoctor() throws IOException {
        User user = new User(10, "uname", "pass");

        try (MockedStatic<UIUtils> mockedUI = mockStatic(UIUtils.class)) {
            // test static entries form the user
            mockedUI.when(() -> UIUtils.readString("Name: ")).thenReturn("Dr");
            mockedUI.when(() -> UIUtils.readString("Surname: ")).thenReturn("Test");

            // test email validation (fail once, then succeed)
            mockedUI.when(() -> UIUtils.readString("Email: "))
                    .thenReturn("invalid_email")
                    .thenReturn("test.doctor@mail.com");

            // test speciality (1: CARDIOLOGIST)
            mockedUI.when(() -> UIUtils.readInt("Choose by typing the number: ")).thenReturn(1);

            // test server response (OK|)
            when(mockConn.receiveResponse()).thenReturn("OK|200");

            // Act
            Doctor result = service.createDoctorForUser(user, mockConn);

            // Assert
            String expectedCommand = "CREATE_DOCTOR|10|Dr|Test|CARDIOLOGIST|test.doctor@mail.com";
            verify(mockConn).sendCommand(expectedCommand);
            assertNotNull(result);
            assertEquals(200, result.getDoctorId());
            assertEquals(DoctorSpecialty.CARDIOLOGIST, result.getSpecialty());
        }
    }

    // DoctorConnection

    @Test
    void getDoctorId_DoctorExists_ReturnsId() {
        // Response: OK|userId|username|doctorId|name|surname
        when(mockConn.receiveResponse()).thenReturn("OK|1|username|456|name|surname");

        int doctorId = service.getDoctorId(mockConn, 1);

        verify(mockConn).sendCommand("CHECK_DOCTOR|1");
        assertEquals(456, doctorId);
    }

    @Test
    void getDoctorPatients_Success_ReturnsList() {
        // Response: OK|p1_id;p1_name;...#p2_id;p2_name;...
        String serverResponse = "OK|1;John;Doe;1990-01-01;MALE;john@mail.com#2;Jane;Smith;1995-05-05;FEMALE;jane@mail.com";
        when(mockConn.receiveResponse()).thenReturn(serverResponse);

        List<String> patients = service.getDoctorPatients(mockConn, 123);

        verify(mockConn).sendCommand("GET_DOCTOR_PATIENTS|123");
        assertEquals(2, patients.size());
        assertTrue(patients.get(0).startsWith("1;"));
    }

    @Test
    void getRecordIdsOfPatient_Success_ReturnsIds() {
        // Response RECORD_ID
        String serverResponse =
                "RECORD_ID: 101\nDate: 2023-01-01\n" +
                        "RECORD_ID: 102\nDate: 2023-01-15\n" +
                        "Some other line: data\n" +
                        "RECORD_ID: 103";

        when(mockConn.receiveResponse()).thenReturn(serverResponse);

        List<Integer> ids = service.getRecordIdsOfPatient(mockConn, 10, 50);

        verify(mockConn).sendCommand("GET_PATIENT_HISTORY_DOCTOR|10|50");
        assertEquals(3, ids.size());
        assertTrue(ids.contains(101));
    }

    @Test
    void addObservation_CallsServerCommand() {
        String note = "Patient seems better.";
        int recordId = 500;

        when(mockConn.receiveResponse()).thenReturn("OK|Observation added");

        service.addObservation(mockConn, recordId, note);

        // Test add observations
        verify(mockConn).sendCommand("ADD_OBSERVATIONS|" + recordId + "|" + note);
    }
}