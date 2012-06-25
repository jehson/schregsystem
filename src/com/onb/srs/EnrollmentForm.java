package com.onb.srs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import com.onb.srs.exceptions.DuplicateClassCardException;
import com.onb.srs.exceptions.NoClassCardException;
import com.onb.srs.exceptions.ScheduleConflictException;

public class EnrollmentForm {
	private int id;
	Student student;
	List<ClassCard> classCards;
	String term;
	
	public EnrollmentForm(int id){
		this.id = id;
		classCards = new ArrayList<ClassCard>();
	}
	
	public EnrollmentForm(int id, Student student){
		this(id);
		this.student = student;
		//term = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
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
	
	public void validate() throws NoClassCardException{
		if (classCards.size()==0){
			throw new NoClassCardException();
		}
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