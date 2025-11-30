LungLink_Doctor IS DESIGNED FOR:
- Registering or logging as a doctor
- Viewing assigned patients
- Viewing each patient´s medicalHistory
- Viewing signals (ECG/EMG) recieved from clients
- Adding medical observations
- Communicating with the server using TCP command-based protocol

## PROJECT STRUCTURE

```

  src/main/java/
 ├── network/
 │   ├── DoctorConnection.java
 │   ├── DataSender.java
 │   └── DataReceiver.java

 ├── services/
 │   ├── DoctorService.java
 │   └── FileService.java

 ├── pojos/
 │   ├── Doctor.java
 │   ├── Patient.java
 │   ├── MedicalHistory.java
 │   ├── Signal.java
 │   └── TypeSignal.java

 └── utils/
     ├── UIUtils.java
     └── DateUtils.java

```

## GUIDE

```

1- Open the project
2- Run: main.MainDoctor
3- Introduce the server Ip
4- Doctor authentication (log in with your username and password or register as a new doctor)
5- Menu opctions
a) View list of asssigned patients
b) View a patients medicalHistory
c) View a patient`s signal (EMG/ECG) 
d) Add medical observations
6- Disconnect

```

## AUTHORS
- Martina Zandio
- Ana Losada
- Jimena Aineto
- Paula Reyero
- Sara Menor 


