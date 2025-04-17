package com.example.pharmiczy.Apis.requests;
//package com.example.pharmiczcy.Apis.requesets;

public class AppointmentRequest {
    private String patientId;
    private String doctorId;
    private String date;
    private String time;

    public AppointmentRequest(String patientId, String doctorId, String date, String time) {
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.date = date;
        this.time = time;
    }

    // getters and setters if needed,k
}
