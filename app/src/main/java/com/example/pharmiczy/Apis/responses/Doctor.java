package com.example.pharmiczy.Apis.responses;

import java.io.Serializable;

public class Doctor implements Serializable {
    private String _id;
    private String name;
    private String email;
    private String role;
    private String specialization;
    private String contact;


    public String getEmail() {
        return email;
    }

    public String get_id() {
        return _id;
    }

    public String getContact() {
        return contact;
    }

    public String getName() {
        return name;
    }

    public String getSpecialization() {
        return specialization;
    }
}

