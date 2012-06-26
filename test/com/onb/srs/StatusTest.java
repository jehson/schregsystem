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
	public void NewToContinuing(){
		assertEquals(Status.CONTINUING, Status.NEW.next(Grade.A, 24));
		assertEquals(Status.CONTINUING, Status.NEW.next(Grade.C, 24));
	}
	
	@Test
	public void NewToProbationary() {
		assertEquals(Status.PROBATIONARY, Status.NEW.next(Grade.F, 24));
		assertFalse(Status.PROBATIONARY == Status.NEW.next(Grade.C, 24));
	}

	@Test
	public void ContinuingToGraduating(){
		assertEquals(Status.GRADUATING, Status.CONTINUING.next(Grade.A, 18));
		assertEquals(Status.GRADUATING, Status.CONTINUING.next(Grade.C, 9));
		assertFalse(Status.GRADUATING == Status.CONTINUING.next(Grade.C, 19));
	}
	
	@Test
	public void ContinuingToProbationary(){
		assertEquals(Status.PROBATIONARY, Status.CONTINUING.next(Grade.F, 18));
		assertEquals(Status.PROBATIONARY, Status.CONTINUING.next(Grade.F, 25));
		assertFalse(Status.PROBATIONARY == Status.CONTINUING.next(Grade.C, 3));
	}
	
	@Test
	public void ContinuingToGraduate(){
		assertEquals(Status.GRADUATE, Status.CONTINUING.next(Grade.A, 0));
		assertEquals(Status.GRADUATE, Status.CONTINUING.next(Grade.C, 0));
		assertFalse(Status.GRADUATE == Status.CONTINUING.next(Grade.A, 3));
	}
	
	@Test
	public void ContinuingToContinuing(){
		assertEquals(Status.CONTINUING, Status.CONTINUING.next(Grade.A, 25));
		assertEquals(Status.CONTINUING, Status.CONTINUING.next(Grade.A, 19));
		assertFalse(Status.CONTINUING == Status.CONTINUING.next(Grade.F, 18));
		assertFalse(Status.CONTINUING == Status.CONTINUING.next(Grade.B, 0));
	}
	
	@Test
	public void ProbationaryToContinuing(){
		assertEquals(Status.CONTINUING, Status.PROBATIONARY.next(Grade.C, 20));
		assertFalse(Status.CONTINUING == Status.PROBATIONARY.next(Grade.C, 18));
		assertFalse(Status.CONTINUING == Status.PROBATIONARY.next(Grade.F, 3));
		assertFalse(Status.CONTINUING == Status.PROBATIONARY.next(Grade.B, 0));
	}
	
	@Test
	public void ProbationaryToIneligible(){
		assertEquals(Status.INELIGIBLE, Status.PROBATIONARY.next(Grade.F, 20));
		assertEquals(Status.INELIGIBLE, Status.PROBATIONARY.next(Grade.F, 18));
		assertEquals(Status.INELIGIBLE, Status.PROBATIONARY.next(Grade.F, 3));
		assertFalse(Status.INELIGIBLE == Status.PROBATIONARY.next(Grade.B, 15));
		assertFalse(Status.INELIGIBLE == Status.PROBATIONARY.next(Grade.B, 0));
	}
	
	@Test
	public void ProbationaryToGraduating(){
		assertEquals(Status.GRADUATING, Status.PROBATIONARY.next(Grade.C, 18));
		assertFalse(Status.GRADUATING == Status.PROBATIONARY.next(Grade.B, 24));
		assertFalse(Status.GRADUATING == Status.PROBATIONARY.next(Grade.F, 21));
		assertFalse(Status.GRADUATING == Status.PROBATIONARY.next(Grade.F, 18));
		assertFalse(Status.GRADUATING == Status.PROBATIONARY.next(Grade.B, 0));
	}
	
	@Test
	public void ProbationaryToGraduate(){
		assertEquals(Status.GRADUATE, Status.PROBATIONARY.next(Grade.C, 0));
		assertFalse(Status.GRADUATE == Status.PROBATIONARY.next(Grade.C, 3));
	}
	
	@Test
	public void GraduatingToProbationary(){
		assertEquals(Status.PROBATIONARY, Status.GRADUATING.next(Grade.F, 18));
		assertFalse(Status.PROBATIONARY == Status.GRADUATING.next(Grade.B, 0));
	}
	
	@Test
	public void GraduatingToGraduate(){
		assertEquals(Status.GRADUATE, Status.GRADUATING.next(Grade.C, 0));
		assertEquals(Status.GRADUATE, Status.GRADUATING.next(Grade.A, 0));
	}
	
	@Test
	public void noGraduatingToContinuing(){
		assertFalse(Status.CONTINUING == Status.GRADUATING.next(Grade.B, 21));
	}
	
	@Test
	public void IneligibleToIneligible(){
		assertEquals(Status.INELIGIBLE, Status.INELIGIBLE.next(Grade.C, 0));
	}
	
	@Test
	public void GraduateToGraduate(){
		assertEquals(Status.GRADUATE, Status.GRADUATE.next(Grade.C, 0));
	}	
}
