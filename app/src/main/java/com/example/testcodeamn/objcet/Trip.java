package com.example.testcodeamn.objcet;

public class Trip {
    String id = null;
    String user_id = null;
    String employee = null;
    String visit_date = null;
    String destination = null;


    public Trip(String id, String user_id, String employee, String visit_date, String destination){
        this.id = id;
        this.user_id = user_id;
        this.employee = employee;
        this.visit_date = visit_date;
        this.destination = destination;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getEmployee() {
        return employee;
    }

    public void setEmployee(String employee) {
        this.employee = employee;
    }

    public String getVisit_date() {
        return visit_date;
    }

    public void setVisit_date(String visit_date) {
        this.visit_date = visit_date;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }
}
