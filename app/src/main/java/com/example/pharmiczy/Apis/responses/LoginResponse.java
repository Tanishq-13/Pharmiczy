package com.example.pharmiczy.Apis.responses;

import com.example.pharmiczy.DataModels.User;


import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    @SerializedName("token")
    private String token;

    @SerializedName("user")
    private User user;

    public String getToken() {
        return token;
    }

    public User getUser() {
        return user;
    }

    public static class User {
        @SerializedName("_id")
        private String _id;

        @SerializedName("name")
        private String name;

        @SerializedName("email")
        private String email;

        @SerializedName("role")
        private String role;

        @SerializedName("age")
        private int age;

        @SerializedName("gender")
        private String gender;

        @SerializedName("contact")
        private String contact;

        public String get_id() {
            return _id;
        }

        public String getName() {
            return name;
        }

        public String getEmail() {
            return email;
        }

        public String getRole() {
            return role;
        }

        public int getAge() {
            return age;
        }

        public String getGender() {
            return gender;
        }

        public String getContact() {
            return contact;
        }
    }
}


