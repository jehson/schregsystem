package com.onb.srs;

import static org.junit.Assert.*;

import org.junit.Test;

public class GradeTest {
	@Test
	public void gradeConversion(){
		assertEquals(Grade.A, Grade.toLetterGrade(360));
		assertEquals(Grade.AM, Grade.toLetterGrade(320));
		assertEquals(Grade.B, Grade.toLetterGrade(290));
		assertEquals(Grade.BM, Grade.toLetterGrade(240));
		assertEquals(Grade.C, Grade.toLetterGrade(200));
		assertEquals(Grade.C, Grade.toLetterGrade(151));
		assertEquals(Grade.F, Grade.toLetterGrade(150));
		assertEquals(Grade.F, Grade.toLetterGrade(149));
	}
}
