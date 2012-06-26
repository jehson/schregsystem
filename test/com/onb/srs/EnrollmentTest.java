package com.onb.srs;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.onb.srs.exceptions.DuplicateClassCardException;
import com.onb.srs.exceptions.DuplicateSectionException;
import com.onb.srs.exceptions.IneligibleStudentException;
import com.onb.srs.exceptions.InsufficientPrerequisitesException;
import com.onb.srs.exceptions.NoClassCardException;
import com.onb.srs.exceptions.OverloadException;
import com.onb.srs.exceptions.ScheduleConflictException;
import com.onb.srs.exceptions.SectionLimitExceededException;
import com.onb.srs.exceptions.UnderloadException;

public class EnrollmentTest {
	private Schedule mondayAtEightThirty = new Schedule(DaySlot.MonThu, TimeSlot.EightThirtyToTen);
	private Schedule mondayAtTen = new Schedule(DaySlot.MonThu, TimeSlot.TenToElevenThirty);
	private Schedule mondayAtElevenThirty = new Schedule(DaySlot.MonThu, TimeSlot.ElevenThirtyToOne);
	private Schedule mondayAtOne = new Schedule(DaySlot.MonThu, TimeSlot.OneToTwoThirty);
	private Schedule mondayAtTwoThirty = new Schedule(DaySlot.MonThu, TimeSlot.TwoThirtyToFour);
	private Schedule mondayAtFour = new Schedule(DaySlot.MonThu, TimeSlot.FourToFiveThirty);
	
	private Schedule tuesdayAtEightThirty = new Schedule(DaySlot.TueFri, TimeSlot.EightThirtyToTen);
	private Schedule tuesdayAtTen = new Schedule(DaySlot.TueFri, TimeSlot.TenToElevenThirty);
	private Schedule tuesdayAtElevenThirty = new Schedule(DaySlot.TueFri, TimeSlot.ElevenThirtyToOne);
	private Schedule tuesdayAtOne = new Schedule(DaySlot.TueFri, TimeSlot.OneToTwoThirty);
	private Schedule tuesdayAtTwoThirty = new Schedule(DaySlot.TueFri, TimeSlot.TwoThirtyToFour);
	private Schedule tuesdayAtFour = new Schedule(DaySlot.TueFri, TimeSlot.FourToFiveThirty);
	
	
	private Teacher mrNarwhal;
	private Teacher mrOcelot;
	
	private Subject math1;
	private Subject math2;
	private Subject eng1;
	
	
	private Curriculum bsMath;
	private Student student;

	private EnrollmentForm firstTermEnrollmentForm;
	
	@Before
	public void setUp() throws DuplicateSectionException, ScheduleConflictException {
		bsMath = new Curriculum("BS Math", math1, math2);
		student = new Student(1, bsMath);
		
		mrNarwhal = new Teacher(001);
		mrOcelot = new Teacher(002);
		
		math1 = new Subject("Math 1");
		eng1 = new Subject("English 1");
	}
	
	@Test (expected = ScheduleConflictException.class)
	public void ScheduleConflict() throws IneligibleStudentException, NoClassCardException, DuplicateClassCardException, ScheduleConflictException, DuplicateSectionException, InsufficientPrerequisitesException {		
		firstTermEnrollmentForm = student.startEnrollment();
		
		Section math1Section = new Section(1, math1, mondayAtTen, mrNarwhal);
		Section eng1Section = new Section(2, eng1, mondayAtTen, mrOcelot);
		
		ClassCard cc1 = new ClassCard(1, student, math1Section);
		ClassCard cc2 = new ClassCard(2, student, eng1Section);
		
		firstTermEnrollmentForm.addClassCard(cc1);
		firstTermEnrollmentForm.addClassCard(cc2);
	}
	
	@Test (expected = SectionLimitExceededException.class)
	public void tooManyStudentsInSection() throws SectionLimitExceededException, DuplicateClassCardException, IneligibleStudentException, DuplicateSectionException, ScheduleConflictException{
		Section math1Section = new Section(1, math1, mondayAtTen, mrNarwhal);
		fillUpSection(math1Section);
		
		Student extraStudent = new Student(41, bsMath);
		extraStudent.startEnrollment();
		ClassCard extraClassCard = new ClassCard(41);
		math1Section.addClassCard(extraClassCard);
	}
	
	private void fillUpSection(Section section) throws SectionLimitExceededException, DuplicateClassCardException, IneligibleStudentException {
		Student anotherStudent;
		ClassCard anotherClassCard;
		
		for (int a = 0; a < 40; a++) {
			anotherStudent = new Student(a, bsMath);
			anotherStudent.startEnrollment();
			anotherClassCard = new ClassCard(a, anotherStudent, section);
			section.addClassCard(anotherClassCard);
		}
	}
	
	@Test (expected = UnderloadException.class)
	public void underloadIsNotAllowed() throws IneligibleStudentException, NoClassCardException, UnderloadException, OverloadException, DuplicateClassCardException, ScheduleConflictException, DuplicateSectionException, InsufficientPrerequisitesException{
		Section math1Section = new Section(1, math1, mondayAtTen, mrNarwhal);
		ClassCard cc1 = new ClassCard(1, student, math1Section);
		
		firstTermEnrollmentForm = student.startEnrollment();
		firstTermEnrollmentForm.addClassCard(cc1);
		student.addNewEnrollmentForm(firstTermEnrollmentForm);
	}
	
	@Test (expected = OverloadException.class)
	public void overloadIsNotAllowed() throws IneligibleStudentException, DuplicateClassCardException, ScheduleConflictException, NoClassCardException, UnderloadException, OverloadException, DuplicateSectionException, InsufficientPrerequisitesException {
		firstTermEnrollmentForm = student.startEnrollment();
		addTooManyClassCardsToEnrollmentForm(firstTermEnrollmentForm);
		student.addNewEnrollmentForm(firstTermEnrollmentForm);
	}
	
	private void addTooManyClassCardsToEnrollmentForm(EnrollmentForm form) throws DuplicateSectionException, ScheduleConflictException, IneligibleStudentException, DuplicateClassCardException, NoClassCardException, UnderloadException, OverloadException, InsufficientPrerequisitesException {
		add15UnitsToEnrollmentForm(form);
		
		Subject hist1 = new Subject("History 1");
		Section hist1Section = new Section(14, hist1, tuesdayAtTwoThirty, mrNarwhal);
		ClassCard cc7 = new ClassCard(7, student, hist1Section);
		
		Subject frch1 = new Subject("French 1");
		Section frch1Section = new Section(15, frch1, tuesdayAtFour, mrNarwhal);
		ClassCard cc8 = new ClassCard(8, student, frch1Section);
		
		form.addClassCard(cc7);
		form.addClassCard(cc8);
	}
	
	private void add15UnitsToEnrollmentForm(EnrollmentForm form) throws IneligibleStudentException, DuplicateClassCardException, ScheduleConflictException, NoClassCardException, UnderloadException, OverloadException, DuplicateSectionException, InsufficientPrerequisitesException {
		
		Subject psy1 = new Subject("Psychology 1");
		Subject phlo1 = new Subject("Philosophy 1");
		Subject eng1 = new Subject("English 1");
		Subject bio1 = new Subject("Biology 1");
		
		Section math1Section = new Section(1, math1, mondayAtTen, mrNarwhal);
		Section psy1Section = new Section(10, psy1, tuesdayAtEightThirty, mrNarwhal);
		Section phlo1Section = new Section(11, phlo1, tuesdayAtTen, mrNarwhal);
		Section eng1Section = new Section(12, eng1, tuesdayAtElevenThirty, mrNarwhal);
		Section bio1Section = new Section(13, bio1, tuesdayAtOne, mrNarwhal);
		
		ClassCard cc1 = new ClassCard(1, student, math1Section);
		ClassCard cc3 = new ClassCard(3, student, psy1Section);
		ClassCard cc4 = new ClassCard(4, student, phlo1Section);
		ClassCard cc5 = new ClassCard(5, student, eng1Section);
		ClassCard cc6 = new ClassCard(6, student, bio1Section);
		
		form.addClassCard(cc1);
		form.addClassCard(cc3);
		form.addClassCard(cc4);
		form.addClassCard(cc5);
		form.addClassCard(cc6);
	}
	
	@Test (expected = InsufficientPrerequisitesException.class)
	public void enrollWithoutMeetingPrerequisites() throws IneligibleStudentException, DuplicateClassCardException, ScheduleConflictException, NoClassCardException, UnderloadException, OverloadException, DuplicateSectionException, InsufficientPrerequisitesException {
		firstTermEnrollmentForm = student.startEnrollment();
		add15UnitsToEnrollmentForm(firstTermEnrollmentForm);
		
		Subject math2 = new Subject("Math 2", math1);
		Section math2Section = new Section(1, math2, mondayAtTwoThirty, mrNarwhal);
		ClassCard math2cc = new ClassCard(1, student, math2Section);
		
		firstTermEnrollmentForm.addClassCard(math2cc);
	}
	
	private void setUpCSCurriculum() {
		Subject eng1 = new Subject("English 1");
		Subject math1 = new Subject("Math 1");
		Subject pe1 = new Subject("PE 1");
		Subject bio1 = new Subject("Biology 1");
		Subject psy1 = new Subject("Psychology 1");
		Subject hist1 = new Subject("History 1");
		
		Subject eng2 = new Subject("English 1", eng1);
		Subject calc1 = new Subject("Calculus 1", math1);
		Subject cs11 = new Subject("Computer Science 11");
		Subject cs12 = new Subject("Computer Science 12");
		Subject cs13 = new Subject("Computer Science 13");
		Subject astr1 = new Subject("Astronomy 1");
		
		Subject stat1 = new Subject("Statistics 1", calc1);
		Subject calc2 = new Subject("Calculus 2");
		Subject cs21 = new Subject("Computer Science 12", cs11);
		Subject cs22 = new Subject("Computer Science 22", cs12);
		Subject cs23 = new Subject("Computer Science 57", cs13);
		Subject phys1 = new Subject("Physics 1");
		
		Subject micro1 = new Subject("Microbiology 1");
		Subject calc3 = new Subject("Calculus 3");
		Subject cs31 = new Subject("Computer Science 13", cs21);
		Subject cs32 = new Subject("Computer Science 23", cs22);
		Subject cs33 = new Subject("Computer Science", cs23);
		Subject phys2 = new Subject("Physics 2", phys1);
		
		Subject cs41 = new Subject("Computer Science 41", cs32);
		Subject cs42 = new Subject("Computer Science 51", math1, cs13);
		Subject cs43 = new Subject("Computer Science 61", cs12);
		Subject cs44 = new Subject("Computer Science 34", cs33);
		Subject lit1 = new Subject("Literature 1");
		Subject sosc1 = new Subject("Social Sciences 1");
		
		Subject cs51 = new Subject("Computer Science 14", cs13);
		Subject cs52 = new Subject("Computer Science 15", cs13);
		Subject cs53 = new Subject("Computer Science 35", cs43, math1);
		Subject cs54 = new Subject("Computer Science 71", cs13, cs32);
		Subject phlo1 = new Subject("Philosopy 1");
		Subject spch1 = new Subject("Speech 1");
		
		Subject cs61 = new Subject("Computer Science 61", calc1, cs31);
		Subject cs62 = new Subject("Computer Science 62", calc1, cs31);
		Subject cs63 = new Subject("Computer Science 63", cs54);
		Subject cs64 = new Subject("Computer Science 64", cs41);
		Subject eng3 = new Subject("English 3", eng2);
		Subject frch1 = new Subject("French 1");
		
		Subject cs71 = new Subject("Computer Science 71", cs61);
		Subject cs72 = new Subject("Computer Science 72", cs62, math1);
		Subject cs73 = new Subject("Computer Science 73", cs63);
		Subject hist2 = new Subject("History 2", hist1);
		Subject frch2 = new Subject("French 2", frch1);
		Subject rzl1 = new Subject("Rizal 1");
		
		
		
		Curriculum computerScience = new Curriculum("Computer Science");
		
		
	}
	
}
