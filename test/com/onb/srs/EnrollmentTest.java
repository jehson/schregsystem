package com.onb.srs;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.onb.srs.exceptions.DuplicateClassCardException;
import com.onb.srs.exceptions.DuplicateSectionException;
import com.onb.srs.exceptions.IneligibleStudentException;
import com.onb.srs.exceptions.NoClassCardException;
import com.onb.srs.exceptions.OverloadException;
import com.onb.srs.exceptions.ScheduleConflictException;
import com.onb.srs.exceptions.SectionLimitExceededException;
import com.onb.srs.exceptions.UnderloadException;

public class EnrollmentTest {
	private Teacher mrNarwhal;
	private Teacher mrOcelot;
	
	private Subject math1;
	private Subject math2;
	private Subject eng1;
	
	
	private Curriculum bsMath;
	private Student student;
	private Schedule schedule;
	
	private Section math1SectionA;
	private Section math2SectionA;
	
	private ClassCard cc1;
	private ClassCard cc2;

	private EnrollmentForm firstTermEnrollmentForm;
	
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

		//firstTermEnrollmentForm = new EnrollmentForm(1, student);
	}
	
	@Test (expected = ScheduleConflictException.class)
	public void ScheduleConflict() throws IneligibleStudentException, NoClassCardException, DuplicateClassCardException, ScheduleConflictException, DuplicateSectionException {		
		firstTermEnrollmentForm = student.startEnrollment();
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
		ClassCard anotherClassCard;
		
		for (int a = 0; a < 40; a++) {
			anotherStudent = new Student(a, bsMath);
			anotherStudent.startEnrollment();
			anotherClassCard = new ClassCard(a);
			math1SectionA.addClassCard(anotherClassCard);
		}
	}
	
	@Test (expected = UnderloadException.class)
	public void underloadIsNotAllowed() throws IneligibleStudentException, NoClassCardException, UnderloadException, OverloadException, DuplicateClassCardException, ScheduleConflictException{
		firstTermEnrollmentForm = student.startEnrollment();
		firstTermEnrollmentForm.addClassCard(cc1);
		student.addNewEnrollmentForm(firstTermEnrollmentForm);
	}
	
	@Test (expected = OverloadException.class)
	public void overloadIsNotAllowed() throws IneligibleStudentException, DuplicateClassCardException, ScheduleConflictException, NoClassCardException, UnderloadException, OverloadException, DuplicateSectionException {
		firstTermEnrollmentForm = student.startEnrollment();
		addTooManyClasscardsToEnrollmentForm();
		student.addNewEnrollmentForm(firstTermEnrollmentForm);
	}
	
	private void addTooManyClasscardsToEnrollmentForm() throws IneligibleStudentException, DuplicateClassCardException, ScheduleConflictException, NoClassCardException, UnderloadException, OverloadException, DuplicateSectionException {
		firstTermEnrollmentForm = student.startEnrollment();
		firstTermEnrollmentForm.addClassCard(cc1); 
		
		Subject psy1 = new Subject("Psychology 1");
		Subject phlo1 = new Subject("Philosophy 1");
		Subject eng1 = new Subject("English 1");
		Subject bio1 = new Subject("Biology 1");
		Subject hist1 = new Subject("History 1");
		Subject frch1 = new Subject("French 1");
		
		Schedule tuesdayAtEightThirty = new Schedule(DaySlot.TueFri, TimeSlot.EightThirtyToTen);
		Schedule tuesdayAtTen = new Schedule(DaySlot.TueFri, TimeSlot.TenToElevenThirty);
		Schedule tuesdayAtElevenThirty = new Schedule(DaySlot.TueFri, TimeSlot.ElevenThirtyToOne);
		Schedule tuesdayAtOne = new Schedule(DaySlot.TueFri, TimeSlot.OneToTwoThirty);
		Schedule tuesdayAtTwoThirty = new Schedule(DaySlot.TueFri, TimeSlot.TwoThirtyToFour);
		Schedule tuesdayAtFour = new Schedule(DaySlot.TueFri, TimeSlot.FourToFiveThirty);
		
		Section psy1Section = new Section(10, psy1, tuesdayAtEightThirty, mrNarwhal);
		Section phlo1Section = new Section(11, phlo1, tuesdayAtTen, mrNarwhal);
		Section eng1Section = new Section(12, eng1, tuesdayAtElevenThirty, mrNarwhal);
		Section bio1Section = new Section(13, bio1, tuesdayAtOne, mrNarwhal);
		Section hist1Section = new Section(14, hist1, tuesdayAtTwoThirty, mrNarwhal);
		Section frch1Section = new Section(15, frch1, tuesdayAtFour, mrNarwhal);
		
		ClassCard cc3 = new ClassCard(3, student, psy1Section);
		ClassCard cc4 = new ClassCard(4, student, phlo1Section);
		ClassCard cc5 = new ClassCard(5, student, eng1Section);
		ClassCard cc6 = new ClassCard(6, student, bio1Section);
		ClassCard cc7 = new ClassCard(7, student, hist1Section);
		ClassCard cc8 = new ClassCard(8, student, frch1Section);
		
		firstTermEnrollmentForm.addClassCard(cc3);
		firstTermEnrollmentForm.addClassCard(cc4);
		firstTermEnrollmentForm.addClassCard(cc5);
		firstTermEnrollmentForm.addClassCard(cc6);
		firstTermEnrollmentForm.addClassCard(cc7);
		firstTermEnrollmentForm.addClassCard(cc8);
	}
}
