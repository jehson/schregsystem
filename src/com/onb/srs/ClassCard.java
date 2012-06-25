package com.onb.srs;

public class ClassCard {
	
	private int id;
	private Student student;
	private Section section;
	private Grade grade;
	
	protected ClassCard(int id){
		this.id = id;
	}
	
	public ClassCard(int id, Student student, Section section){
		this(id);
		this.student = student;
		this.section = section;
	}
	
	protected void setGrade(Grade grade){
		this.grade = grade;
	}
	
	public Grade getGrade(){
		return grade;
	}
	
	public Student getStudent(){
		return student;
	}
	
	public Section getSection(){
		return section;
	}
	
	public Subject getSubject() {
		return section.getSubject();
	}
	
}
