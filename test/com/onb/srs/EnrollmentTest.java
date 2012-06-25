package com.onb.srs;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.onb.srs.exceptions.DuplicateClassCardException;
import com.onb.srs.exceptions.DuplicateSectionException;
import com.onb.srs.exceptions.IneligibleStudentException;
import com.onb.srs.exceptions.NoClassCardException;
import com.onb.srs.exceptions.ScheduleConflictException;
import com.onb.srs.exceptions.SectionLimitExceededException;

public class EnrollmentTest {
	Teacher mrNarwhal;
	Teacher mrOcelot;
	
	Subject math1;
	Subject math2;
	Subject eng1;
	
	
	Curriculum bsMath;
	Student student;
	Schedule schedule;
	
	Section math1SectionA;
	Section math2SectionA;
	
	ClassCard cc1;
	ClassCard cc2;

	EnrollmentForm firstTermEnrollmentForm;
	
	@Before
	public void setUp() throws DuplicateSectionException, ScheduleConflictException {
		bsMath = new Curriculum("BS Math", math1, math2);
		student = new Student(1, bsMath);
		
		mrNarwhal = new Teacher(001);
		mrOcelot = new Teacher(002);
		
		math1 = new Subject("Math 1");
		math2 = new Subject("Math 2", math1);
		eng1 = new Subject("English 1");
		
		schedule = new Schedule(DaySlot.MonThu, TimeSlot.EightThirtyToTen);
		
		math1SectionA = new Section(1, math1, schedule, mrNarwhal);
		math2SectionA = new Section(2, eng1, schedule, mrOcelot);
		
		cc1 = new ClassCard(1, student, math1SectionA);
		cc2 = new ClassCard(2, student, math2SectionA);

		firstTermEnrollmentForm = new EnrollmentForm(1, student);
	}
	
	@Test (expected = ScheduleConflictException.class)
	public void ScheduleConflict() throws IneligibleStudentException, NoClassCardException, DuplicateClassCardException, ScheduleConflictException, DuplicateSectionException {		
		student.startEnrollment();
		firstTermEnrollmentForm.addClassCard(cc1);
		firstTermEnrollmentForm.addClassCard(cc2); //throws ScheduleConflictException
	}
	
	@Test (expected = SectionLimitExceededException.class)
	public void tooManyStudentsInSection() throws SectionLimitExceededException, DuplicateClassCardException, IneligibleStudentException{
		fillUpSection();
		
		Student extraStudent = new Student(41, bsMath);
		extraStudent.startEnrollment();
		ClassCard extraClassCard = new ClassCard(41);
		math1SectionA.addClassCard(extraClassCard);
	}
	
	private void fillUpSection() throws SectionLimitExceededException, DuplicateClassCardException, IneligibleStudentException {
		Student anotherStudent;
		EnrollmentForm anotherFirstTermEnrollmentForm;
		ClassCard anotherClassCard;
		
			for (int a = 0; a < 40; a++) {
				anotherStudent = new Student(a, bsMath);
				anotherStudent.startEnrollment();
				anotherFirstTermEnrollmentForm = new EnrollmentForm(a, anotherStudent);
				anotherClassCard = new ClassCard(a);
				math1SectionA.addClassCard(anotherClassCard);
			}
	}
	
}
