package test.java;

import main.java.*;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

public class AppDriverTest {


/////////////////////////////////////////// helper function tests ///////////////////////////////////////////
    @org.junit.Test
    public void hasDisqualifyingGPA() {

        //basic test
        assertTrue(AppDriver.hasDisqualifyingGPA(0, 4.0));
        assertFalse(AppDriver.hasDisqualifyingGPA(4.5, 4.5));

        //Project specs state: "High School GPA below 70% of scale provided on application. For example, 2.8 on a 4.0 scale."
        //However this results in exactly 70%. Technically this is not BELOW 70% so a 2.8 out of 4 should pass but a 2.79 out of 4 should fail.
        //Provided from example on pdf. Example of edge case testing.
        assertTrue(AppDriver.hasDisqualifyingGPA(2.79, 4.0));
        assertFalse(AppDriver.hasDisqualifyingGPA(2.8, 4.0));


    }

    @org.junit.Test
    public void formattedCorrectly() {
        assertTrue(AppDriver.formattedCorrectly("Alan"));
        assertFalse(AppDriver.formattedCorrectly("mark"));
        assertFalse(AppDriver.formattedCorrectly("NATHAN"));
        assertTrue(AppDriver.formattedCorrectly("L"));
    }


    @Test
    public void hasExceptionalGPA() {

        //Basic Excellent Scores
        assertTrue(AppDriver.hasExceptionalGPA(4.0, 4.0));
        assertTrue(AppDriver.hasExceptionalGPA(5.0, 5.0));

        //Basic Poor Scores
        assertFalse(AppDriver.hasExceptionalGPA(0.0, 4.0));
        assertFalse(AppDriver.hasExceptionalGPA(0.0, 5.0));

        //Edge Excellent Scores
        assertTrue(AppDriver.hasExceptionalGPA(3.6, 4.0));
        assertTrue(AppDriver.hasExceptionalGPA(4.5, 5.0));

        //Edge Poor Scores
        assertFalse(AppDriver.hasExceptionalGPA(3.59, 4.0));
        assertFalse(AppDriver.hasExceptionalGPA(4.49, 5.0));

    }

    @Test
    public void hasExceptionalTestScore() {

        //Basic Excellent Scores
        assertTrue(AppDriver.hasExceptionalTestScore(2400, 36));
        assertTrue(AppDriver.hasExceptionalTestScore(2400, null));
        assertTrue(AppDriver.hasExceptionalTestScore(null, 36));


        //Basic Poor Scores
        assertFalse(AppDriver.hasExceptionalTestScore(0, 0));
        assertFalse(AppDriver.hasExceptionalTestScore(0, null));
        assertFalse(AppDriver.hasExceptionalTestScore(null, 0));


        //Edge Excellent Scores
        assertTrue(AppDriver.hasExceptionalTestScore(1930, 28));
        assertTrue(AppDriver.hasExceptionalTestScore(1930, null));
        assertTrue(AppDriver.hasExceptionalTestScore(null, 28));

        //Edge Poor Scores
        assertFalse(AppDriver.hasExceptionalTestScore(1920, 27));
        assertFalse(AppDriver.hasExceptionalTestScore(1920, null));
        assertFalse(AppDriver.hasExceptionalTestScore(null, 27));

        //SAT is not null and fails but ACT passes
        assertTrue(AppDriver.hasExceptionalTestScore(1400, 28));

        //ACT is not null and fails but SAT passes
        assertTrue(AppDriver.hasExceptionalTestScore(2000, 22));
    }

    @Test
    public void meetsStateAgeRequirement() {

        //Test acceptable ages in California
        assertTrue(AppDriver.meetsStateAgeRequirement("California", 17));
        assertTrue(AppDriver.meetsStateAgeRequirement("California", 25));
        assertTrue(AppDriver.meetsStateAgeRequirement("California", 81));

        //Test unacceptable ages in California
        assertFalse(AppDriver.meetsStateAgeRequirement("California", 16));
        assertFalse(AppDriver.meetsStateAgeRequirement("California", 26));
        assertFalse(AppDriver.meetsStateAgeRequirement("California", 80));

        //Test acceptable ages in other states
        assertTrue(AppDriver.meetsStateAgeRequirement("Idaho", 81));

        //Test unacceptable ages in other states
        assertFalse(AppDriver.meetsStateAgeRequirement("Idaho", 21));
        assertFalse(AppDriver.meetsStateAgeRequirement("Idaho", 80));

    }

    @Test
    //test an empty list of felonies
    public void hasDisqualifyingCriminalRecord1() {

        //first test empty criminal record
        ArrayList<Felony> criminalRecord = new ArrayList<Felony>();
        assertFalse(AppDriver.hasDisqualifyingCriminalRecord(criminalRecord));
    }



    @Test
    //test a list with one disqualifying felony
    public void hasDisqualifyingCriminalRecord2() {
        Calendar cal = Calendar.getInstance();

        //calendar set method format is year, month, date
        cal.set(2018, 1, 1);
        Date felonyDate = cal.getTime();

        Felony unacceptable = new Felony(felonyDate);
        ArrayList<Felony> criminalRecord = new ArrayList<Felony>();
        criminalRecord.add(unacceptable);

        assertTrue(AppDriver.hasDisqualifyingCriminalRecord(criminalRecord));

    }

    @Test
    //test a list with one old felony (non-disqualifying)
    public void hasDisqualifyingCriminalRecord3() {
        Calendar cal = Calendar.getInstance();

        //calendar set method format is year, month, date
        cal.set(1999, 1, 1);
        Date felonyDate = cal.getTime();

        Felony unacceptable = new Felony(felonyDate);
        ArrayList<Felony> criminalRecord = new ArrayList<Felony>();
        criminalRecord.add(unacceptable);

        assertFalse(AppDriver.hasDisqualifyingCriminalRecord(criminalRecord));

    }

    @Test
    //test a list with one felony nearly 5 years old (date of testing is April 28, 2018)
    public void hasDisqualifyingCriminalRecord4() {
        Calendar cal = Calendar.getInstance();

        //calendar set method format is year, month, date
        cal.set(2013, 6, 1);
        Date felonyDate = cal.getTime();

        Felony unacceptable = new Felony(felonyDate);
        ArrayList<Felony> criminalRecord = new ArrayList<Felony>();
        criminalRecord.add(unacceptable);

        assertTrue(AppDriver.hasDisqualifyingCriminalRecord(criminalRecord));

    }

    /////////////////////////////////////////// Application main tests ///////////////////////////////////////////

    @Test
    //compute a list with one applicant who should be rejected
    public void isolatedRejectionTest1() {
        List<Applicant> allApplicants = new ArrayList<Applicant>();

        //has sub-par gpa
        Applicant shouldReject = new Applicant("Randy", "Johnson", "California", 18, 2.0, 5.0, 1800, 25, new ArrayList<Felony>());
        allApplicants.add(shouldReject);
        allApplicants = AppDriver.computeApplications(allApplicants);

        //check rejection
        assertEquals(Applicant.ApplicationStatus.INSTANT_REJECT, allApplicants.get(0).getStatus());

        //check valid reason for rejection
        assertEquals(AppDriver.BELOW_GPA, allApplicants.get(0).getReasonForRejection());
    }

    @Test
    //compute a list with one applicant who should be rejected
    public void isolatedRejectionTest2() {
        List<Applicant> allApplicants = new ArrayList<Applicant>();

        //has negative age
        Applicant shouldReject = new Applicant("Randy", "Johnson", "California", -20, 5.0, 5.0, 1800, 25, new ArrayList<Felony>());
        allApplicants.add(shouldReject);
        allApplicants = AppDriver.computeApplications(allApplicants);

        //check rejection
        assertEquals(Applicant.ApplicationStatus.INSTANT_REJECT, allApplicants.get(0).getStatus());

        //check valid reason for rejection
        assertEquals(AppDriver.NEGATIVE_AGE, allApplicants.get(0).getReasonForRejection());
    }

    @Test
    //compute a list with one applicant who should be rejected
    public void isolatedRejectionTest3() {
        List<Applicant> allApplicants = new ArrayList<Applicant>();

        //has incorrectly formatted name
        Applicant shouldReject = new Applicant("randy", "johnson", "California", 18, 5.0, 5.0, 1800, 25, new ArrayList<Felony>());
        allApplicants.add(shouldReject);
        allApplicants = AppDriver.computeApplications(allApplicants);

        //check rejection
        assertEquals(Applicant.ApplicationStatus.INSTANT_REJECT, allApplicants.get(0).getStatus());

        //check valid reason for rejection
        assertEquals(AppDriver.INCORRECT_FORMAT, allApplicants.get(0).getReasonForRejection());
    }

    @Test
    //compute a list with one applicant who should be rejected
    public void isolatedRejectionTest4() {
        List<Applicant> allApplicants = new ArrayList<Applicant>();
        Calendar cal = Calendar.getInstance();

        //calendar set method format is year, month, date
        cal.set(2018, 1, 1);
        Date felonyDate = cal.getTime();

        Felony unacceptable = new Felony(felonyDate);
        ArrayList<Felony> criminalRecord = new ArrayList<Felony>();
        criminalRecord.add(unacceptable);

        //has criminal record
        Applicant shouldReject = new Applicant("Randy", "Johnson", "California", 18, 5.0, 5.0, 1800, 25, criminalRecord);
        allApplicants.add(shouldReject);
        allApplicants = AppDriver.computeApplications(allApplicants);

        //check rejection
        assertEquals(Applicant.ApplicationStatus.INSTANT_REJECT, allApplicants.get(0).getStatus());

        //check valid reason for rejection
        assertEquals(AppDriver.HAS_FELONY, allApplicants.get(0).getReasonForRejection());
    }

    @Test
    //compute a list with one applicant who should be accepted
    public void instantAcceptTest() {
        List<Applicant> allApplicants = new ArrayList<Applicant>();

        //has excellent marks no disqualifications.
        Applicant shouldReject = new Applicant("Will", "Forman", "California", 17, 3.9, 4.0, 1980, 25, new ArrayList<Felony>());
        allApplicants.add(shouldReject);
        allApplicants = AppDriver.computeApplications(allApplicants);

        assertEquals(Applicant.ApplicationStatus.INSTANT_ACCEPT, allApplicants.get(0).getStatus());
    }

    @Test
    //compute a list with one applicant who should be marked as needing further review
    public void furtherReviewTest() {
        List<Applicant> allApplicants = new ArrayList<Applicant>();

        //has adequate marks with no instant rejections.
        Applicant shouldReject = new Applicant("Jill", "Gallagher", "California", 18, 3.2, 4.0, 1880, 25, new ArrayList<Felony>());
        allApplicants.add(shouldReject);
        allApplicants = AppDriver.computeApplications(allApplicants);

        assertEquals(Applicant.ApplicationStatus.FURTHER_REVIEW, allApplicants.get(0).getStatus());
    }

}