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
import com.onb.srs.exceptions.UnderloadException;

public class StatusTest {
	Student newStudent;
	
	@Before
	public void setUp() throws IneligibleStudentException{
		Subject[] subjects = new Subject[20];
		for(int i=0; i<20; i++){
			subjects[i] = new Subject(String.valueOf(i));
		}
		
		Section[] sections = new Sections[20];
		for(int i=0; i<20; i++){
			sections[i] = new Section[i];
		}
		Curriculum bsmath = new Curriculum("BS Math", subjects);
		newStudent = new Student(1, bsmath);
		EnrollmentForm eForm = newStudent.startEnrollment();
		
		
	}
	@Test
	public void NewToContinuing() throws IneligibleStudentException{
		
		
		
	}
	
}
