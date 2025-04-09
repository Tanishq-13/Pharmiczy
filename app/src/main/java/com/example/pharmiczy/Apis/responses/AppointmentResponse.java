package com.example.pharmiczy.Apis.responses;

public class AppointmentResponse {
    private String message;
    private Appointment appointment;

    public String getMessage() {
        return message;
    }

    public Appointment getAppointment() {
        return appointment;
    }

    public static class Appointment {
        private String _id;
        private String patientId;
        private String doctorId;
        private String date;
        private String time;
        private String status;

        public String get_id() { return _id; }
        public String getPatientId() { return patientId; }
        public String getDoctorId() { return doctorId; }
        public String getDate() { return date; }
        public String getTime() { return time; }
        public String getStatus() { return status; }
    }
}
