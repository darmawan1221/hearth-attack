package com.example.newproject;

public class Register_con {
    private String age, name, marriage, date, position, gender, nik;

    public Register_con(String age, String name, String marriage, String date, String position, String gender, String nik) {
        this.age = age;
        this.name = name;
        this.marriage = marriage;
        this.date = date;
        this.position = position;
        this.gender = gender;
        this.nik = nik;
    }

    public String getAge() {
        return age;
    }

    public String getName() {
        return name;
    }

    public String getMarriage() {
        return marriage;
    }
    public String getDate() {
        return date;
    }
    public String getPosition() {
        return position;
    }
    public String getGender() {
        return gender;
    }
    public String getNik() {
        return nik;
    }
}