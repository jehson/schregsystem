package com.onb.srs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import com.onb.srs.exceptions.DuplicateClassCardException;
import com.onb.srs.exceptions.InsufficientPrerequisitesException;
import com.onb.srs.exceptions.NoClassCardException;
import com.onb.srs.exceptions.OverloadException;
import com.onb.srs.exceptions.ScheduleConflictException;
import com.onb.srs.exceptions.UnderloadException;

public class EnrollmentForm {
	Student student;
	List<ClassCard> classCards;
	String term;
	
	public EnrollmentForm(Student student){
		this.student = student;
		classCards = new ArrayList<ClassCard>();
	}
	
	protected void addClassCard(ClassCard classCard) throws DuplicateClassCardException, ScheduleConflictException, InsufficientPrerequisitesException{
		if (classCards.contains(classCard)){
			throw new DuplicateClassCardException();
		}
		if (hasConflict(classCard.getSection())) {
			throw new ScheduleConflictException();
		}
		if (!student.hasPassedPrerequisitesOf(classCard.getSubject())) {
			throw new InsufficientPrerequisitesException();
		}
		classCards.add(classCard);
	}
		
	protected List<ClassCard> getClassCards(){
		return classCards;
	}
	
	private boolean hasConflict(Section section1){
		for(ClassCard c: classCards){
			Section section2 = c.getSection(); 
			if(section2.sameSchedule(section1)){
				return true;
			}
		}
		return false;
	}
	
	public void validate() throws NoClassCardException, UnderloadException, OverloadException {
		if (classCards.size() == 0) {
			throw new NoClassCardException();
		} 
		else if (studentIsUnderloaded()) {
			throw new UnderloadException();
		} 
		else if (studentIsOverloaded()) {
			throw new OverloadException();
		}
	}	
	
	private boolean studentIsUnderloaded() {
		return getTotalUnits() < student.getStatus().getMinUnits();
	}
	
	private boolean studentIsOverloaded() {
		return getTotalUnits() > student.getStatus().getMaxUnits();
	}
	
	private int getTotalUnits() {
		int totalUnits = 0;
		for (ClassCard cc : classCards) {
			totalUnits += cc.getSubject().getUnits();
		}
		return totalUnits;
	}
	
	public Grade getAverageGrade() {
		int total = 0;
		for(ClassCard classCard: classCards){
			total += classCard.getGrade().getValue();
		}
		return Grade.toLetterGrade(total/classCards.size());
	}
}