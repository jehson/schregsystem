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
	private Schedule tuesdayAtElevenThirty = new Schedule(DaySlot.TueFri, TimeSlot.ElevenThirtyToOne);
	
	private Teacher mrNarwhal;
	
	@Before
	public void setUp() {
		mrNarwhal = new Teacher(004316); 
	}
	
	@Test
	public void newSectionHasRequirements() throws DuplicateSectionException, ScheduleConflictException {
		cs11Section = new Section(001, cs11, mondayAtTen, mrNarwhal);
		assertNotNull(cs11Section.getTeacher());
		assertNotNull(cs11Section.getSubject());
		assertNotNull(cs11Section.getSchedule());
	}
	
	@Test
	public void newSectionIsAddedToTeacher() throws DuplicateSectionException, ScheduleConflictException {
		cs11Section = new Section(001, cs11, mondayAtTen, mrNarwhal);
		assertEquals(1, mrNarwhal.getSections().size());
		assertTrue(mrNarwhal.hasSection(cs11Section));
	}
	
	@Test (expected = ScheduleConflictException.class)
	public void creationOfSectionWithConflictIsAborted() throws DuplicateSectionException, ScheduleConflictException {
		cs11Section = new Section(001, cs11, mondayAtTen, mrNarwhal);
		cs56Section = new Section(002, cs56, mondayAtTen, mrNarwhal);
	}
	
	@Test
	public void sectionWithScheduleConflictIsNotAddedToTeacher() throws DuplicateSectionException, ScheduleConflictException{
		cs11Section = new Section(001, cs11, mondayAtTen, mrNarwhal);
		assertEquals(1, mrNarwhal.getSections().size());
		try {
			cs56Section = new Section(002, cs56, mondayAtTen, mrNarwhal);
		} catch (ScheduleConflictException e) {}
		catch (DuplicateSectionException e) {}
		
		assertEquals(1, mrNarwhal.getSections().size());
	}
	
	@Test (expected = ScheduleConflictException.class)
	public void assignNewTeacherWithSchedulConflict() throws DuplicateSectionException, ScheduleConflictException {
		cs11Section = new Section(001, cs11, mondayAtTen, mrNarwhal);
		Teacher mrOcelot = new Teacher(0034);
		cs56Section = new Section(002, cs56, mondayAtTen, mrOcelot);
		cs11Section.assignNewTeacher(mrOcelot);
	}
	
	@Test (expected = DuplicateSectionException.class)
	public void reassignToTheSameTeacher() throws DuplicateSectionException, ScheduleConflictException {
		cs11Section = new Section(001, cs11, mondayAtTen, mrNarwhal);
		cs11Section.assignNewTeacher(mrNarwhal);
	}
	
	@Test
	public void successfullyAssignNewTeacherToSection() throws DuplicateSectionException, ScheduleConflictException {
		Teacher mrOcelot = new Teacher(0034);
		cs11Section = new Section(001, cs11, mondayAtTen, mrNarwhal);
		
		cs11Section.assignNewTeacher(mrOcelot);
		assertTrue(mrOcelot.hasSection(cs11Section));
		assertFalse(mrNarwhal.hasSection(cs11Section));
	}
	
}


