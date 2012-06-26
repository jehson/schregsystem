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
	
	public Curriculum getCurriculum() {
		return curriculum;
	}
	
	public Status getStatus(){
		return status;
	}
	
	public EnrollmentForm startEnrollment() throws IneligibleStudentException{
		if(status.isEligible()){
			try {
				status = status.next(calculatePreviousAverageGrade(), getRemainingUnits());
			}
			catch(NoPreviousRecordsException e){
				status = Status.NEW;
			}
				return new EnrollmentForm(this);
		}
		else {
			throw new IneligibleStudentException();
		}
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
	
	private boolean hasPreviousRecords(){
		return(enrollmentForms.size() != 0);
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
	
	public int getNumberOfEnrollmentForms(){
		return enrollmentForms.size();
	}
	
	public void addPassedSubject(Subject subject){
		passedSubjects.add(subject);
	}
	
	public boolean hasPassedPrerequisitesOf(Subject subject) {
		List<Subject> prerequisites = new ArrayList<Subject>();
		prerequisites = subject.getPrerequisites();
		boolean returnValue = true;
		
		if (prerequisites.size() == 0) return true;		
		for (Subject s : prerequisites) {
			if (!passedSubjects.contains(s)) returnValue = false;
		}
		return returnValue;			
	}
}
