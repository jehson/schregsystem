package com.onb.srs;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.onb.srs.exceptions.DuplicateSectionException;
import com.onb.srs.exceptions.ScheduleConflictException;

public class SectionTests {
	private Subject cs11 = new Subject("CS 11");
	private Subject cs56 = new Subject("CS 56");
	
	private Section cs11Section;
	private Section cs56Section;
	
	private Schedule mondayAtTen = new Schedule(DaySlot.MonThu, TimeSlot.TenToElevenThirty);
	
	private Teacher teacher;
	
	@Before
	public void setUp() {
		teacher = new Teacher(004316); 
	}
	
	@Test
	public void newSectionHasRequirements() throws DuplicateSectionException, ScheduleConflictException {
		cs11Section = new Section(001, cs11, mondayAtTen, teacher);
		assertNotNull(cs11Section.getTeacher());
		assertNotNull(cs11Section.getSubject());
		assertNotNull(cs11Section.getSchedule());
	}
	
	@Test
	public void newSectionIsAddedToTeacher() throws DuplicateSectionException, ScheduleConflictException {
		cs11Section = new Section(001, cs11, mondayAtTen, teacher);
		assertEquals(1, teacher.getSections().size());
		assertTrue(teacher.hasSection(cs11Section));
	}
	
	@Test (expected = ScheduleConflictException.class)
	public void creationOfSectionWithConflictIsAborted() throws DuplicateSectionException, ScheduleConflictException {
		cs11Section = new Section(001, cs11, mondayAtTen, teacher);
		cs56Section = new Section(002, cs56, mondayAtTen, teacher);
	}
	
	@Test
	public void sectionWithScheduleConflictIsNotAddedToTeacher() throws DuplicateSectionException, ScheduleConflictException{
		cs11Section = new Section(001, cs11, mondayAtTen, teacher);
		assertEquals(1, teacher.getSections().size());
		try {
			cs56Section = new Section(002, cs56, mondayAtTen, teacher);
		} catch (ScheduleConflictException e) {}
		catch (DuplicateSectionException e) {}
		
		assertEquals(1, teacher.getSections().size());
	}
}
