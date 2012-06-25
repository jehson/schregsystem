package com.onb.srs;

import static org.junit.Assert.*;

import org.junit.Test;

import com.onb.srs.exceptions.DuplicateClassCardException;
import com.onb.srs.exceptions.IneligibleStudentException;
import com.onb.srs.exceptions.NoClassCardException;
import com.onb.srs.exceptions.ScheduleConflictException;

public class StatusTest {

	Subject math1 = new Subject("Math 1");
	Subject math2 = new Subject("Math 2", math1);
	Subject math3 = new Subject("Math 3", math1);
	Subject math4 = new Subject("Math 4", math3);
	Subject math5 = new Subject("Math 5", math3);
	Subject math6 = new Subject("Math 6", math3);
	Subject math7 = new Subject("Math 7", math6);
	Subject phys1 = new Subject("Physics 1", math1);
	Subject phys2 = new Subject("Physics 2", math2, phys1);


	Teacher teacher = new Teacher(1);
	Curriculum bsmath = new Curriculum("BS Math", math1, math2, phys1, phys2, math3, math4, math5, math6, math7);
	Student student = new Student(1, bsmath);
	
	Schedule schedule = new Schedule(DaySlot.MonThu, TimeSlot.EightThirtyToTen);
	Schedule schedule1 = new Schedule(DaySlot.MonThu, TimeSlot.TwoThirtyToFour);
	Schedule schedule2 = new Schedule(DaySlot.MonThu, TimeSlot.ElevenThirtyToOne);
	Schedule schedule3 = new Schedule(DaySlot.MonThu, TimeSlot.FourToFiveThirty);
	Schedule schedule4 = new Schedule(DaySlot.MonThu, TimeSlot.OneToTwoThirty);
	Schedule schedule5 = new Schedule(DaySlot.MonThu, TimeSlot.TenToElevenThirty);
	
	Section math1SectionA = new Section(1, math1, schedule);
	Section math2SectionA = new Section(2, math2, schedule2);
	Section math3SectionA = new Section(3, math3, schedule);
	Section math4SectionA = new Section(4, math4, schedule);
	Section math5SectionA = new Section(5, math1, schedule2);
	Section math6SectionA = new Section(6, math2, schedule3);
	Section math7SectionA = new Section(7, math3, schedule4);
	Section phys1SectionA = new Section(8, math4, schedule5);
	Section phys2SectionA = new Section(8, math4, schedule1);
	
	
	EnrollmentForm enrollmentForm1 = new EnrollmentForm(1, student);
	EnrollmentForm enrollmentForm2 = new EnrollmentForm(2, student);
	EnrollmentForm enrollmentForm3 = new EnrollmentForm(3, student);
	EnrollmentForm enrollmentForm4 = new EnrollmentForm(4, student);
	EnrollmentForm enrollmentForm5 = new EnrollmentForm(5, student);
	
	ClassCard cc1 = new ClassCard(1, student, math1SectionA);
	ClassCard cc2 = new ClassCard(2, student, math2SectionA);
	ClassCard cc3 = new ClassCard(3, student, math3SectionA);
	ClassCard cc4 = new ClassCard(4, student, math4SectionA);
	ClassCard cc5 = new ClassCard(5, student, math5SectionA);
	ClassCard cc6 = new ClassCard(6, student, math6SectionA);
	ClassCard cc7 = new ClassCard(7, student, math7SectionA);
	ClassCard cc8 = new ClassCard(8, student, phys1SectionA);
	ClassCard cc9 = new ClassCard(9, student, phys2SectionA);
	
	@Test
	public void newStudentEnrollment() throws IneligibleStudentException, NoClassCardException, DuplicateClassCardException, ScheduleConflictException {
		student.startEnrollment();
		enrollmentForm1.addClassCard(cc1);
		student.addNewEnrollmentForm(enrollmentForm1);
		assertEquals(Status.NEW, student.getStatus());
		assertEquals(1, student.getNumberOfEnrollmentForms());
	}
	
	@Test 
	public void continuingStudentEnrollment() throws IneligibleStudentException, NoClassCardException, DuplicateClassCardException, ScheduleConflictException{
		student.startEnrollment();
		enrollmentForm1.addClassCard(cc1);
		student.addNewEnrollmentForm(enrollmentForm1);
		EnrollmentForm eForm = student.getLastEnrollmentForm();
		eForm.setGrade(cc1, Grade.AM);
		student.startEnrollment();
		enrollmentForm2.addClassCard(cc2);
		student.addNewEnrollmentForm(enrollmentForm2);
		assertEquals(Status.CONTINUING, student.getStatus());
		assertEquals(2, student.getNumberOfEnrollmentForms());
	}
	
	@Test
	public void probationaryStudentEnrollment() throws DuplicateClassCardException, ScheduleConflictException, IneligibleStudentException, NoClassCardException {
		student.startEnrollment();
		enrollmentForm1.addClassCard(cc1);
		student.addNewEnrollmentForm(enrollmentForm1);
		EnrollmentForm eForm = student.getLastEnrollmentForm();
		eForm.setGrade(cc1, Grade.F);
		student.startEnrollment();
		enrollmentForm2.addClassCard(cc2);
		student.addNewEnrollmentForm(enrollmentForm2);
		assertEquals(Status.PROBATIONARY, student.getStatus());
		assertEquals(2, student.getNumberOfEnrollmentForms());
	}
	
	@Test (expected = IneligibleStudentException.class)
	public void probationaryToIneligibleStudentEnrollment() throws DuplicateClassCardException, ScheduleConflictException, IneligibleStudentException, NoClassCardException {
		student.startEnrollment();
		enrollmentForm1.addClassCard(cc1);
		student.addNewEnrollmentForm(enrollmentForm1);
		EnrollmentForm eForm = student.getLastEnrollmentForm();
		eForm.setGrade(cc1, Grade.F);
		
		student.startEnrollment();
		enrollmentForm2.addClassCard(cc2);
		student.addNewEnrollmentForm(enrollmentForm2);
		EnrollmentForm eForm2 = student.getLastEnrollmentForm();
		eForm.setGrade(cc2, Grade.F);
		
		student.startEnrollment();
		enrollmentForm3.addClassCard(cc3);
		student.addNewEnrollmentForm(enrollmentForm3); //throws IneligibleStudentException
	}
	
	@Test 
	public void probationaryToContinuingStudentEnrollment() throws DuplicateClassCardException, ScheduleConflictException, IneligibleStudentException, NoClassCardException {
		student.startEnrollment();
		enrollmentForm1.addClassCard(cc1);
		student.addNewEnrollmentForm(enrollmentForm1);
		EnrollmentForm eForm = student.getLastEnrollmentForm();
		eForm.setGrade(cc1, Grade.AM);
		
		student.startEnrollment();
		enrollmentForm2.addClassCard(cc2);
		student.addNewEnrollmentForm(enrollmentForm2);
		EnrollmentForm eForm2 = student.getLastEnrollmentForm();
		eForm2.setGrade(cc2, Grade.A);
		
		student.startEnrollment();
		enrollmentForm3.addClassCard(cc3);
		student.addNewEnrollmentForm(enrollmentForm3);
		assertEquals(Status.CONTINUING, student.getStatus());
		assertEquals(3, student.getNumberOfEnrollmentForms());
	}
	
	@Test 
	public void graduatingStudentEnrollment() throws DuplicateClassCardException, ScheduleConflictException, IneligibleStudentException, NoClassCardException {
		student.startEnrollment();
		enrollmentForm1.addClassCard(cc1);
		student.addNewEnrollmentForm(enrollmentForm1);
		EnrollmentForm eForm = student.getLastEnrollmentForm();
		eForm.setGrade(cc1, Grade.AM);
		
		student.startEnrollment();
		enrollmentForm2.addClassCard(cc2);
		student.addNewEnrollmentForm(enrollmentForm2);
		EnrollmentForm eForm2 = student.getLastEnrollmentForm();
		eForm2.setGrade(cc2, Grade.A);
		
		student.startEnrollment();
		enrollmentForm3.addClassCard(cc3);
		student.addNewEnrollmentForm(enrollmentForm3);
		EnrollmentForm eForm3 = student.getLastEnrollmentForm();
		eForm3.setGrade(cc3, Grade.A);
		
		student.startEnrollment();
		enrollmentForm4.addClassCard(cc4);
		student.addNewEnrollmentForm(enrollmentForm4);
		assertEquals(Status.GRADUATING, student.getStatus());
		assertEquals(4, student.getNumberOfEnrollmentForms());
	}
	
	@Test 
	public void graduatedStudent() throws DuplicateClassCardException, ScheduleConflictException, IneligibleStudentException, NoClassCardException {
		student.startEnrollment();
		enrollmentForm1.addClassCard(cc1);
		student.addNewEnrollmentForm(enrollmentForm1);
		EnrollmentForm eForm = student.getLastEnrollmentForm();
		eForm.setGrade(cc1, Grade.AM);
		
		student.startEnrollment();
		enrollmentForm2.addClassCard(cc2);
		student.addNewEnrollmentForm(enrollmentForm2);
		EnrollmentForm eForm2 = student.getLastEnrollmentForm();
		eForm2.setGrade(cc2, Grade.A);
		
		student.startEnrollment();
		enrollmentForm3.addClassCard(cc3);
		student.addNewEnrollmentForm(enrollmentForm3);
		EnrollmentForm eForm3 = student.getLastEnrollmentForm();
		eForm3.setGrade(cc3, Grade.A);
		
		student.startEnrollment();
		enrollmentForm4.addClassCard(cc4);
		student.addNewEnrollmentForm(enrollmentForm4);
		EnrollmentForm eForm4 = student.getLastEnrollmentForm();
		eForm4.setGrade(cc4, Grade.B);
		
		student.startEnrollment();
		enrollmentForm5.addClassCard(cc5);
		enrollmentForm5.addClassCard(cc6);
		enrollmentForm5.addClassCard(cc7);
		enrollmentForm5.addClassCard(cc8);
		enrollmentForm5.addClassCard(cc9);
		student.addNewEnrollmentForm(enrollmentForm5);
		EnrollmentForm eForm5 = student.getLastEnrollmentForm();
		eForm5.setGrade(cc5, Grade.A);
		eForm5.setGrade(cc6, Grade.A);
		eForm5.setGrade(cc7, Grade.A);
		eForm5.setGrade(cc8, Grade.A);
		eForm5.setGrade(cc9, Grade.C);
		
		student.evaluateStatus();
		assertEquals(Status.GRADUATE, student.getStatus());
	}
	
}
