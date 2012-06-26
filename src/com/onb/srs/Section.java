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
	
	public Section(int id){
		this.id = id;
		classCards = new ArrayList<ClassCard>();
	}
	
	public Section(int id, Subject subject, Schedule schedule, Teacher teacher) throws DuplicateSectionException, ScheduleConflictException {
		this(id);	
		if (teacher.hasClassAt(schedule)) {
			throw new ScheduleConflictException("Teacher already has class at this schedule.");
		}
		this.subject = subject;
		this.schedule = schedule;
		this.teacher = teacher;
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

	public boolean sameSchedule(Section section){
		return schedule.equals(section.getSchedule());
	}
	
	public int getNumberOfClassCards() {
		return classCards.size();
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Section)) return false;
		Section s = (Section) o;
		if (this.id == s.id) return true;
		return false;
	}
	
	@Override
	public int hashCode() {
		int result = 17;
		result = 31 * result + id;
		return result;
		
	}
	
	public void assignNewTeacher(Teacher teacher) throws ScheduleConflictException, DuplicateSectionException {
		if (teacher.getSections().contains(this)) {
			throw new DuplicateSectionException("Teacher is already assigned this section");
		} else if (teacher.hasClassAt(this.schedule)) {
			throw new ScheduleConflictException("Teacher already has class at this schedule");
		}
		
		this.teacher.removeSection(this);
		teacher.addSection(this);
		this.teacher = teacher;
	}
}

