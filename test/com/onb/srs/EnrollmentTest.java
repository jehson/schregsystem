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
	private Subject eng58;
	
	
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
		eng58 = new Subject("English 1");
	}
	
	@Test
	public void enrollmentFormForEligibleStudent() throws IneligibleStudentException{
		Student s = new Student(1, new Curriculum("BSCS"));
		assertNotNull(s.startEnrollment());
	}
	
	@Test (expected = IneligibleStudentException.class)
	public void enrollmentFormForIneligibleStudent() throws IneligibleStudentException {
		Student s = new Student(1, new Curriculum("BSCS"), Status.INELIGIBLE);
		EnrollmentForm eform = s.startEnrollment();
	}
	
	@Test (expected = IneligibleStudentException.class)
	public void enrollmentFormForGraduateStudent() throws IneligibleStudentException {
		Student s = new Student(1, new Curriculum("BSCS"), Status.GRADUATE);
		EnrollmentForm eform = s.startEnrollment();
	}

	@Test (expected = ScheduleConflictException.class)
	public void ScheduleConflict() throws IneligibleStudentException, NoClassCardException, DuplicateClassCardException, ScheduleConflictException, DuplicateSectionException, InsufficientPrerequisitesException {		
		firstTermEnrollmentForm = student.startEnrollment();
		
		Section math1Section = new Section(1, math1, mondayAtTen, mrNarwhal);
		Section eng1Section = new Section(2, eng58, mondayAtTen, mrOcelot);
		
		ClassCard cc1 = new ClassCard(1, student, math1Section);
		ClassCard cc2 = new ClassCard(2, student, eng1Section);
		
		firstTermEnrollmentForm.addClassCard(cc1);
		firstTermEnrollmentForm.addClassCard(cc2);
	}
	
	@Test (expected = SectionLimitExceededException.class)
	public void tooManyStudentsInSection() throws SectionLimitExceededException, DuplicateClassCardException, IneligibleStudentException, DuplicateSectionException, ScheduleConflictException{
		Section math1Section = new Section(1, math1, mondayAtTen, mrNarwhal);
		Student anotherStudent;
		ClassCard anotherClassCard;
		
		for (int i=0; i<40; i++) {
			anotherStudent = new Student(i, bsMath);
			anotherStudent.startEnrollment();
			anotherClassCard = new ClassCard(i, anotherStudent, math1Section);
			math1Section.addClassCard(anotherClassCard);
		}
		
		Student extraStudent = new Student(41, bsMath);
		extraStudent.startEnrollment();
		ClassCard extraClassCard = new ClassCard(41);
		math1Section.addClassCard(extraClassCard);
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
		add15UnitsToEnrollmentForm(form, student);
		
		Subject hist1 = new Subject("History 1");
		Section hist1Section = new Section(14, hist1, tuesdayAtTwoThirty, mrNarwhal);
		ClassCard cc7 = new ClassCard(7, student, hist1Section);
		
		Subject frch1 = new Subject("French 1");
		Section frch1Section = new Section(15, frch1, tuesdayAtFour, mrNarwhal);
		ClassCard cc8 = new ClassCard(8, student, frch1Section);
		
		form.addClassCard(cc7);
		form.addClassCard(cc8);
	}
	
	private void add15UnitsToEnrollmentForm(EnrollmentForm form, Student s) throws IneligibleStudentException, DuplicateClassCardException, ScheduleConflictException, NoClassCardException, UnderloadException, OverloadException, DuplicateSectionException, InsufficientPrerequisitesException {
		
		Subject psy1 = new Subject("Psychology 1");
		Subject phlo1 = new Subject("Philosophy 1");
		Subject eng1 = new Subject("English 1");
		Subject bio1 = new Subject("Biology 1");
		
		Section math1Section = new Section(1, math1, mondayAtTen, mrNarwhal);
		Section psy1Section = new Section(10, psy1, tuesdayAtEightThirty, mrNarwhal);
		Section phlo1Section = new Section(11, phlo1, tuesdayAtTen, mrNarwhal);
		Section eng1Section = new Section(12, eng1, tuesdayAtElevenThirty, mrNarwhal);
		Section bio1Section = new Section(13, bio1, tuesdayAtOne, mrNarwhal);
		
		ClassCard cc1 = new ClassCard(1, s, math1Section);
		ClassCard cc3 = new ClassCard(3, s, psy1Section);
		ClassCard cc4 = new ClassCard(4, s, phlo1Section);
		ClassCard cc5 = new ClassCard(5, s, eng1Section);
		ClassCard cc6 = new ClassCard(6, s, bio1Section);
		
		form.addClassCard(cc1);
		form.addClassCard(cc3);
		form.addClassCard(cc4);
		form.addClassCard(cc5);
		form.addClassCard(cc6);
	}
	
	@Test (expected = InsufficientPrerequisitesException.class)
	public void enrollWithoutMeetingPrerequisites() throws IneligibleStudentException, DuplicateClassCardException, ScheduleConflictException, NoClassCardException, UnderloadException, OverloadException, DuplicateSectionException, InsufficientPrerequisitesException {
		firstTermEnrollmentForm = student.startEnrollment();
		add15UnitsToEnrollmentForm(firstTermEnrollmentForm, student);
		
		Subject frch1 = new Subject("French 1");
		Subject frch2 = new Subject("French 2", frch1);
		Section frch2Section = new Section(1, frch2, mondayAtTwoThirty, mrNarwhal);
		ClassCard frch2cc = new ClassCard(1, student, frch2Section);
		
		firstTermEnrollmentForm.addClassCard(frch2cc);
	}
	
	@Test
	public void enrollToSubjectWithPrerequisites() throws IneligibleStudentException, DuplicateClassCardException, ScheduleConflictException, NoClassCardException, UnderloadException, OverloadException, DuplicateSectionException, InsufficientPrerequisitesException {
		Curriculum cs = setUpCSCurriculum();
		student = new Student(200837434, cs); 
		
		firstTermEnrollmentForm = student.startEnrollment();
		add15UnitsToEnrollmentForm(firstTermEnrollmentForm, student);
		
		Subject frch1 = new Subject("French 1");
		Section frch1Section = new Section(1, frch1, mondayAtFour);
		ClassCard frch1cc = new ClassCard(1, student, frch1Section);
		firstTermEnrollmentForm.addClassCard(frch1cc);
		
		for (ClassCard cc : firstTermEnrollmentForm.getClassCards()) {
			cc.setGrade(Grade.A);
		}
		
		EnrollmentForm secondTermEnrollmentForm = student.startEnrollment();
		
		Subject frch2 = new Subject("French 2", frch1);
		Section frch2Section = new Section(1, frch2, mondayAtTwoThirty, mrNarwhal);
		ClassCard frch2cc = new ClassCard(1, student, frch2Section);
		
		try {
			secondTermEnrollmentForm.addClassCard(frch2cc);
		} catch (InsufficientPrerequisitesException e) {
			fail("Prerequisites have already been met. InsufficientPrerequisitesException shouldn't have been thrown");
		}
	}
	
	private Curriculum setUpCSCurriculum() {
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
		
		Curriculum computerScience = new Curriculum("Computer Science", eng1, math1, pe1, bio1, psy1, hist1, 
		eng2, calc1, cs11, cs12, cs13, astr1,
		stat1, calc2, cs21, cs22, cs23, phys1, 
		micro1, calc3, cs31, cs32, cs33, phys2,
		cs41, cs42, cs43, cs44, lit1, sosc1,
		cs51, cs52, cs53, cs54, phlo1, spch1,
		cs61, cs62, cs63, cs64, eng3, frch1, 
		cs71, cs72, cs73, hist2, frch2, rzl1);
		
		return computerScience;
	}
	
}
