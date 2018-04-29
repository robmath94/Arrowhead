package main.java;

import java.util.List;

public class Applicant {

    private String firstName;
    private String lastName;
    private String state;
    private int age;
    private double gpa;
    private double gpaMax;
    private Integer satScore;
    private Integer actScore;
    private List<Felony> criminalRecord;
    private ApplicationStatus status;
    private String reasonForRejection;

    public enum ApplicationStatus {
        NONE,
        INSTANT_ACCEPT,
        INSTANT_REJECT,
        FURTHER_REVIEW
    }


    public Applicant(String firstName, String lastName, String state, int age, double gpa, double gpaMax, Integer satScore, Integer actScore, List<Felony> criminalRecord) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.state = state;
        this.age = age;
        this.gpa = gpa;
        this.gpaMax = gpaMax;
        this.satScore = satScore;
        this.actScore = actScore;
        this.criminalRecord = criminalRecord;
        this.status = ApplicationStatus.NONE;
        this.reasonForRejection = "";
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public double getGpa() {
        return gpa;
    }

    public void setGpa(double gpa) {
        this.gpa = gpa;
    }

    public double getGpaMax() {
        return gpaMax;
    }

    public void setGpaMax(double gpaMax) {
        this.gpaMax = gpaMax;
    }

    public int getSatScore() {
        return satScore;
    }

    public void setSatScore(int satScore) {
        this.satScore = satScore;
    }

    public int getActScore() {
        return actScore;
    }

    public void setActScore(int actScore) {
        this.actScore = actScore;
    }

    public List<Felony> getCriminalRecord() {
        return criminalRecord;
    }

    public void setCriminalRecord(List<Felony> criminalRecord) {
        this.criminalRecord = criminalRecord;
    }

    public ApplicationStatus getStatus() {
        return status;
    }

    public void setStatus(ApplicationStatus status) {
        this.status = status;
    }

    public String getReasonForRejection() {
        return reasonForRejection;
    }

    public void setReasonForRejection(String reasonForRejection) {
        this.reasonForRejection = reasonForRejection;
    }


}

