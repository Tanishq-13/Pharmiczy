package com.example.pharmiczy.Apis.responses;

public class appresp {
    private String _id;
    private String patientId;
    private Doctor doctorId;
    private String date;
    private String time;
    private String status;

    public String get_id() { return _id; }
    public String getPatientId() { return patientId; }
    public Doctor getDoctorId() { return doctorId; }
    public String getDate() { return date; }
    public String getTime() { return time; }
    public String getStatus() { return status; }
}
