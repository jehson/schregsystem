package com.onb.srs;

import java.util.ArrayList;
import java.util.List;

import com.onb.srs.exceptions.DuplicateClassCardException;
import com.onb.srs.exceptions.DuplicateSectionException;
import com.onb.srs.exceptions.ScheduleConflictException;
import com.onb.srs.exceptions.SectionLimitExceededException;

public class Section {
	
	private static final int MAX_CLASSCARDS = 40;
	private int id;
	private Teacher teacher;
	private Schedule schedule;
	private Subject subject;
	private List<ClassCard> classCards;
	
	public Section(int id, Subject subject, Schedule schedule, Teacher teacher) throws DuplicateSectionException, ScheduleConflictException {
		if (teacher.hasClassAt(schedule)) {
			throw new ScheduleConflictException("Teacher already has class at this schedule.");
		}
		this.subject = subject;
		this.schedule = schedule;
		this.teacher = teacher;
		this.id = id;
		classCards = new ArrayList<ClassCard>();
		teacher.addSection(this);
	}
		
	public void addClassCard(ClassCard classCard) throws SectionLimitExceededException, DuplicateClassCardException {
		if(classCards.contains(classCard)){
			throw new DuplicateClassCardException("Two same students/classCards in one section.");
		} else {
			if(classCards.size() < MAX_CLASSCARDS){
				classCards.add(classCard);
			}
			else{
				throw new SectionLimitExceededException("Limited to 40 classCards/students only.");
			}
		}
	}

	public Teacher getTeacher() {
		return teacher;
	}

	public Schedule getSchedule() {
		return schedule;
	}

	public Subject getSubject() {
		return subject;
	}

	public boolean equalsSchedule(Section section){
		return schedule.equals(section.getSchedule());
	}
	
	public int getNumberOfClassCards() {
		return classCards.size();
	}
}
