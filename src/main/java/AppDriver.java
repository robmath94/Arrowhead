package main.java;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AppDriver {

    public static final String INCORRECT_FORMAT = "Incorrect name formatting. ";
    public static final String NEGATIVE_AGE = "Negative age detected. ";
    public static final String BELOW_GPA = "GPA is below 70%. ";
    public static final String HAS_FELONY = "Applicant has 1 or more felony within the last 5 years. ";

    public static List<Applicant> computeApplications(List<Applicant> applicants) {

        applicants = checkInstantReject(applicants);
        applicants = checkInstantAccept(applicants);
        return applicants;

    }

    public static List<Applicant> checkInstantAccept(List<Applicant> applicants) {
        for (Applicant currentApplicant : applicants) {

            //rule out all already rejects applicants
            if (currentApplicant.getStatus() != Applicant.ApplicationStatus.INSTANT_REJECT) {

                //check instant accept criteria
                if (meetsStateAgeRequirement(currentApplicant.getState(), currentApplicant.getAge()) && 
                        hasExceptionalTestScore(currentApplicant.getSatScore(), currentApplicant.getActScore()) && 
                        hasExceptionalGPA(currentApplicant.getGpa(), currentApplicant.getGpaMax())) {
                    currentApplicant.setStatus(Applicant.ApplicationStatus.INSTANT_ACCEPT);
                    
                }
                else {

                    //applicant is not an instant reject nor are they an instant accept. Mark as needing further review.
                    currentApplicant.setStatus(Applicant.ApplicationStatus.FURTHER_REVIEW);
                }
            }
        }
        return applicants;
    }


    public static List<Applicant> checkInstantReject(List<Applicant> applicants) {

        for (Applicant currentApplicant : applicants) {

            //check criminal record
            if (hasDisqualifyingCriminalRecord(currentApplicant.getCriminalRecord())) {
                currentApplicant.setStatus(Applicant.ApplicationStatus.INSTANT_REJECT);
                String rejectionReason = currentApplicant.getReasonForRejection();
                rejectionReason += HAS_FELONY;
                currentApplicant.setReasonForRejection(rejectionReason);
            }

            //check disqualifying GPA
            if (hasDisqualifyingGPA(currentApplicant.getGpa(), currentApplicant.getGpaMax())) {
                currentApplicant.setStatus(Applicant.ApplicationStatus.INSTANT_REJECT);
                String rejectionReason = currentApplicant.getReasonForRejection();
                rejectionReason += BELOW_GPA;
                currentApplicant.setReasonForRejection(rejectionReason);
            }

            //check age is not negative
            if (currentApplicant.getAge() < 0) {
                currentApplicant.setStatus(Applicant.ApplicationStatus.INSTANT_REJECT);
                String rejectionReason = currentApplicant.getReasonForRejection();
                rejectionReason += NEGATIVE_AGE;
                currentApplicant.setReasonForRejection(rejectionReason);
            }

            //check name formatting
            if (!formattedCorrectly(currentApplicant.getFirstName())
                    || !formattedCorrectly(currentApplicant.getLastName())) {
                currentApplicant.setStatus(Applicant.ApplicationStatus.INSTANT_REJECT);
                String rejectionReason = currentApplicant.getReasonForRejection();
                rejectionReason += INCORRECT_FORMAT;
                currentApplicant.setReasonForRejection(rejectionReason);
            }
        }
        return applicants;
    }

    public static boolean hasExceptionalGPA(double gpa, double gpaMax) {
        return gpa / gpaMax * 100 >= 90;
    }

    public static boolean hasExceptionalTestScore(Integer satScore, Integer actScore) {

        //one or both scores may be provided. Must check for null before computation.
        if(satScore != null && satScore > 1920 ) {
            return true;
        }

        //Assess ACT score.
        else {
            return actScore != null && actScore > 27;
        }
    }

    public static boolean meetsStateAgeRequirement(String state, int age) {

        // In-state (California) age 17 or older, and younger than 26.
        if(state.toLowerCase().equals("california")) {

            return (age >= 17 && age < 26) || age > 80;
        }

        // Older than 80 from any state.
        else {
            return age > 80;
        }
    }

    public static boolean hasDisqualifyingCriminalRecord(List<Felony> criminalRecord) {
        for (Felony currentFelony : criminalRecord) {

            //assuming the application is being compared to present date.
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.YEAR, -5);
            Date fiveYearsBefore = cal.getTime();

            //the felony happened within the last 5 years. Applicant is disqualified.
            if (currentFelony.getFelonyDate().after(fiveYearsBefore)) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasDisqualifyingGPA(double gpa, double gpaMax) {
        return gpa / gpaMax * 100 < 70;
    }

    public static boolean formattedCorrectly(String name) {
        boolean acceptableFormat = false;

        if (Character.isUpperCase(name.charAt(0))) {
            char[] letters = name.toCharArray();
            acceptableFormat = true;

            for (int index = 1; index < letters.length; index++) {
                if (!Character.isLowerCase(name.charAt(index))) {
                    acceptableFormat = false;
                }
            }
        }
        return acceptableFormat;
    }

    public static void main(String args[]) {

    }
}
