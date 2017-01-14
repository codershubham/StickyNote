package com.shubham.navinote;

public class Notes {
    private String name;
    private String Date;

    public Notes() {
    }

    public Notes(String name, String date) {
        this.name = name;
        Date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }


}
