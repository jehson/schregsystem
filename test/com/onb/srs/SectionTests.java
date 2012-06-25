package com.onb.srs;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.onb.srs.exceptions.DuplicateSectionException;
import com.onb.srs.exceptions.ScheduleConflictException;

public class SectionTests {
	Subject cs11;
	Subject cs21;
	Subject cs56;
	Subject cs100;
	
	Teacher teacher;
	
	Section cs11Section;
	Section cs56Section;
	
	Schedule mondayAtTen;
	Schedule mondayAtElevenThirty;
	Schedule tuesdayAtEightThirty;
	Schedule tuesdayAtTen;
	Schedule tuesdayAtOne;
	
	@Before
	public void setUp() {
		teacher = new Teacher(004316);
		
		cs11 = new Subject("CS 11");
		cs21 = new Subject("CS 21");
		cs56 = new Subject("CS 56");
		cs100 = new Subject("CS 100");
		
		createFirstSection();
	}
	
	private void createFirstSection() {
		try {
			mondayAtTen = new Schedule(DaySlot.MonThu, TimeSlot.TenToElevenThirty);
			cs11Section = new Section(001, cs11, mondayAtTen, teacher);
		} catch (ScheduleConflictException e) { }
		  catch (DuplicateSectionException e) { }
	}
	
	@Test
	public void newSectionHasRequirements() {
		assertNotNull(cs11Section.getTeacher());
		assertNotNull(cs11Section.getSubject());
		assertNotNull(cs11Section.getSchedule());
	}
	
	@Test
	public void newSectionIsAddedToTeacher() {
		assertEquals(1, teacher.getSections().size());
		assertTrue(teacher.hasSection(cs11Section));
	}
	
	@Test
	public void creationOfSectionWithConflictIsAborted() {
		assertNull(cs56Section);
		createSectionWithConflict();
		assertNull(cs56Section);
	}
	
	private void createSectionWithConflict(){
		try {
			cs56Section = new Section(002, cs56, mondayAtTen, teacher);
		} catch (ScheduleConflictException e) { }
		catch (DuplicateSectionException e) { }
	}
	
	@Test
	public void sectionWithScheduleConflictIsNotAddedToTeacher(){
		assertEquals(1, teacher.getSections().size());
		createSectionWithConflict();
		assertEquals(1, teacher.getSections().size());
	}
	
	
}
