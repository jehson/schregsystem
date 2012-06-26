package com.onb.srs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import com.onb.srs.exceptions.DuplicateClassCardException;
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
	
	protected void addClassCard(ClassCard classCard) throws DuplicateClassCardException, ScheduleConflictException{
		if(classCards.contains(classCard)){
			throw new DuplicateClassCardException();
		}
		else{
			if(hasConflict(classCard.getSection())){
				throw new ScheduleConflictException();
			}
			else{
				classCards.add(classCard);
			}
		}
	}
		
	protected List<ClassCard> getClassCards(){
		return classCards;
	}
	
	private boolean hasConflict(Section section1){
		for(ClassCard c: classCards){
			Section section2 = c.getSection(); 
			if(section2.equalsSchedule(section1)){
				return true;
			}
		}
		return false;
	}
	
	public void validate() throws NoClassCardException, UnderloadException, OverloadException {
		if (classCards.size() == 0){
			throw new NoClassCardException();
		} else if (studentIsUnderloaded()) {
			throw new UnderloadException();
		} else if (studentIsOverloaded()) {
			throw new OverloadException();
		}
	}	
	
	private boolean studentIsUnderloaded() {
		if (getTotalUnits() < student.getStatus().getMinUnits()) return true;
		else return false;
	}

	private boolean studentIsOverloaded() {
		if (getTotalUnits() > student.getStatus().getMaxUnits()) return true;
		else return false;
	}
	
	private int getTotalUnits() {
		int totalUnits = 0;
		for (ClassCard cc : classCards) {
			totalUnits += cc.getSubject().getUnits();
		}
		return totalUnits;
	}

	public void setGrade(ClassCard classCard, Grade grade){
		classCards.remove(classCard);
		classCard.setGrade(grade);
		classCards.add(classCard);
		if(!(grade.equals(Grade.F))){
			student.addPassedSubject(classCard.getSection().getSubject());
		}
	}
	
	public Grade getAverageGrade() {
		int total = 0;
		for(ClassCard classCard: classCards){
			total += classCard.getGrade().getValue();
		}
		return Grade.toLetterGrade(total/classCards.size());
	}
}