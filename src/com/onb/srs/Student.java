package com.onb.srs;


import java.util.ArrayList;
import java.util.List;
import com.onb.srs.exceptions.IneligibleStudentException;
import com.onb.srs.exceptions.NoClassCardException;
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
	
	public void startEnrollment() throws IneligibleStudentException{
		if(status.isEligible()){
			evaluateStatus();
		}
		else {
			throw new IneligibleStudentException();
		}
		//return enrollment form
	}
	
	public void evaluateStatus(){
		this.status = Status.changeStatus(this);
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
	
	public Grade calculatePreviousAverageGrade(){
		EnrollmentForm lastEnrollmentForm = enrollmentForms.get(enrollmentForms.size()-1);
		return lastEnrollmentForm.getAverageGrade();
	}
	
	public int getNumberOfEnrollmentForms(){
		return enrollmentForms.size();
	}
	
	public void addPassedSubject(Subject subject){
		passedSubjects.add(subject);
	}
}
