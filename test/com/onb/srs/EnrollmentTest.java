package com.onb.srs;

import static org.junit.Assert.*;

import org.junit.Test;

import com.onb.srs.exceptions.DuplicateClassCardException;
import com.onb.srs.exceptions.DuplicateSectionException;
import com.onb.srs.exceptions.IneligibleStudentException;
import com.onb.srs.exceptions.NoClassCardException;
import com.onb.srs.exceptions.ScheduleConflictException;

public class EnrollmentTest {

	@Test (expected = ScheduleConflictException.class)
	public void ScheduleConflict() throws IneligibleStudentException, NoClassCardException, DuplicateClassCardException, ScheduleConflictException, DuplicateSectionException {

		Teacher teacher = new Teacher(1);
		Subject math1 = new Subject("Math 1");
		Subject math2 = new Subject("Math 2", math1);
		
		Curriculum bsmath = new Curriculum("BS Math", math1, math2);
		Student student = new Student(1, bsmath);
		
		Schedule schedule = new Schedule(DaySlot.MonThu, TimeSlot.EightThirtyToTen);
		
		Section math1SectionA = new Section(1, math1, schedule, teacher);
		Section math2SectionA = new Section(2, math2, schedule, teacher);
		
		ClassCard cc1 = new ClassCard(1, student, math1SectionA);
		ClassCard cc2 = new ClassCard(2, student, math2SectionA);

		EnrollmentForm enrollmentForm1 = new EnrollmentForm(1, student);
		student.startEnrollment();
		enrollmentForm1.addClassCard(cc1);
		enrollmentForm1.addClassCard(cc2); //throws ScheduleConflictException
		
		//student.addNewEnrollmentForm(enrollmentForm1);
		//assertEquals(Status.NEW, student.getStatus());
		//assertEquals(1, student.getNumberOfEnrollmentForms());
	}
	
	
}
