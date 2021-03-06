package com.onb.srs;


import java.util.ArrayList;
import java.util.List;
import com.onb.srs.exceptions.IneligibleStudentException;
import com.onb.srs.exceptions.NoClassCardException;
import com.onb.srs.exceptions.NoPreviousRecordsException;
import com.onb.srs.exceptions.OverloadException;
import com.onb.srs.exceptions.UnderloadException;

public class Student {

	private final int id;
	private Status status;
	private Curriculum curriculum;

	private List<EnrollmentForm> enrollmentForms;
	private List<Subject> passedSubjects;
	
	public Student(int id, Curriculum curriculum){
		this.id = id;
		this.curriculum = curriculum;
		enrollmentForms = new ArrayList<EnrollmentForm>();
		passedSubjects = new ArrayList<Subject>();
		status = Status.NEW;
	}
	
	protected Student(int id, Curriculum curriculum, Status status){
		this(id,curriculum);
		this.status = status;
	}
	public Curriculum getCurriculum() {
		return curriculum;
	}
	
	public Status getStatus(){
		return status;
	}
	
	public EnrollmentForm startEnrollment() throws IneligibleStudentException{
		evaluateStatus();
		if(!status.isEligible()){
			throw new IneligibleStudentException();
		}
		return new EnrollmentForm(this);
	}
	
	private void evaluateStatus() throws IneligibleStudentException{
		Status prevStatus = status;
		Status evaluatedStatus;
		if(!prevStatus.isEligible()){
			throw new IneligibleStudentException("No need to proceed to status evaluation.");
		}
		try {
			evaluatedStatus = prevStatus.next(calculatePreviousAverageGrade(), getRemainingUnits());
		}
		catch(NoPreviousRecordsException e){
			//previous grade was not found, student must be new
			evaluatedStatus = Status.NEW;
		}
		status = evaluatedStatus;
	}
	
	public void addNewEnrollmentForm(EnrollmentForm enrollmentForm) throws IneligibleStudentException, NoClassCardException, UnderloadException, OverloadException{
		if(status.isEligible()){
			enrollmentForm.validate();
			enrollmentForms.add(enrollmentForm);
		}
		else {
			throw new IneligibleStudentException();
		}
	}
		
	public EnrollmentForm getLastEnrollmentForm(){
		return enrollmentForms.get(enrollmentForms.size()-1);
	}
	
	public int getRemainingUnits(){
		int totalUnits = curriculum.getTotalUnits();
		int finishedUnits = 0;
		
		for(Subject passedSubject: passedSubjects){
			finishedUnits += passedSubject.getUnits();
		}
		
		return totalUnits - finishedUnits;
	}
	
	public Grade calculatePreviousAverageGrade() throws NoPreviousRecordsException{
		if(hasPreviousRecords()){
			EnrollmentForm lastEnrollmentForm = enrollmentForms.get(enrollmentForms.size()-1);
			return lastEnrollmentForm.getAverageGrade();
		}
		else {
			throw new NoPreviousRecordsException("Previous grades could not be found.");
		}
	}

	private boolean hasPreviousRecords(){
		return(enrollmentForms.size() != 0);
	}
	
	public int getNumberOfEnrollmentForms(){
		return enrollmentForms.size();
	}
	
	public void addPassedSubject(Subject subject){
		passedSubjects.add(subject);
	}
	
	public boolean hasPassedPrerequisitesOf(Subject subject) {
		List<Subject> prerequisiteSubjects = subject.getPrerequisites();
		boolean hasPassedPrereqs = true;
		for (Subject prereqSubject : prerequisiteSubjects) {
			if (!passedSubjects.contains(prereqSubject)) {
				hasPassedPrereqs = false;
				break;
			}
		}
		return hasPassedPrereqs;			
	}
}
