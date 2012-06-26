package com.onb.srs;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.onb.srs.exceptions.DuplicateClassCardException;
import com.onb.srs.exceptions.DuplicateSectionException;
import com.onb.srs.exceptions.IneligibleStudentException;
import com.onb.srs.exceptions.NoClassCardException;
import com.onb.srs.exceptions.OverloadException;
import com.onb.srs.exceptions.ScheduleConflictException;
import com.onb.srs.exceptions.UnderloadException;

public class StatusTest {
	
	@Test
	public void newStudentTriesToEnroll() throws IneligibleStudentException{
		Curriculum curriculum = new Curriculum("BSCS");
		Student student = new Student(1, curriculum);
		assertNotNull(student.startEnrollment());
		assertEquals(Status.NEW, student.getStatus());
	}
	
	@Test
	public void NewToContinuing() throws IneligibleStudentException, DuplicateClassCardException, ScheduleConflictException, NoClassCardException, UnderloadException, OverloadException{
		mock();
		Student newStudent = createStudentWithPassingAverageGrade();
		newStudent.startEnrollment();
		assertEquals(Status.CONTINUING, newStudent.getStatus());
	}
	
	@Test
	public void NewToProbationary() throws IneligibleStudentException, DuplicateClassCardException, ScheduleConflictException, NoClassCardException, UnderloadException, OverloadException {
		Student newStudent = createStudentWithFailingAverageGrade();
		newStudent.startEnrollment();
		assertEquals(Status.PROBATIONARY, newStudent.getStatus());
	}
	

	
	private Student createStudentWithPassingAverageGrade() throws IneligibleStudentException, DuplicateClassCardException, ScheduleConflictException, NoClassCardException, UnderloadException, OverloadException{
		Student newStudent = mock();
		EnrollmentForm eForm = newStudent.getLastEnrollmentForm();	
		List<ClassCard> classCards = eForm.getClassCards();
		for(ClassCard classCard: classCards){
			classCard.setGrade(Grade.A);
		}
		eForm.updateClassCardGrades(classCards);
		return newStudent;
	}
	
	private Student createStudentWithFailingAverageGrade() throws IneligibleStudentException, DuplicateClassCardException, ScheduleConflictException, NoClassCardException, UnderloadException, OverloadException{
		Student newStudent = mock();
		EnrollmentForm eForm = newStudent.getLastEnrollmentForm();	
		List<ClassCard> classCards = eForm.getClassCards();
		for(ClassCard classCard: classCards){
			classCard.setGrade(Grade.F);
		}
		eForm.updateClassCardGrades(classCards);
		return newStudent;
	}
	
	private Student mock() throws IneligibleStudentException, DuplicateClassCardException, ScheduleConflictException, NoClassCardException, UnderloadException, OverloadException{
		Subject[] subjects = new Subject[18];
		for(int i=0; i<18; i++){
			subjects[i] = new Subject(String.valueOf(i));
		}
		Schedule[] monThuScheds = new Schedule[6];
		for(int i=0; i<6; i++){
			monThuScheds[i] = new Schedule(DaySlot.MonThu, TimeSlot.values()[i]);
		}
		
		Schedule[] tueFriScheds = new Schedule[6];
		for(int i=0; i<6; i++){
			tueFriScheds[i] = new Schedule(DaySlot.TueFri, TimeSlot.values()[i]);
		}
		
		Schedule[] wedSatScheds = new Schedule[6];
		for(int i=0; i<6; i++){
			wedSatScheds[i] = new Schedule(DaySlot.WedSat, TimeSlot.values()[i]);
		}
		
		Section[] sections = new Section[18];
		for(int i=0; i<6; i++){
			sections[i] = new Section(i, subjects[i], monThuScheds[i%6]);
		}
		for(int i=6; i<12; i++){
			sections[i] = new Section(i, subjects[i], tueFriScheds[i%6]);
		}
		for(int i=12; i<18; i++){
			sections[i] = new Section(i, subjects[i], wedSatScheds[i%6]);
		}
		
		Curriculum bsmath = new Curriculum("BS Math", subjects);
		Student newStudent = new Student(1, bsmath);
		

		ClassCard[] cards = new ClassCard[18];
		for(int i=0; i<18; i++){
			cards[i] = new ClassCard(i, newStudent, sections[i]);
		}
		
		EnrollmentForm eForm = newStudent.startEnrollment();

		for(int i=0; i<5; i++){
			eForm.addClassCard(cards[i]);
		}

		eForm.validate();
		newStudent.addNewEnrollmentForm(eForm);
		return newStudent;
	}
	
	
	
}
