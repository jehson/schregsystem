package com.onb.srs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Subject {
	String name;
	int units = 3;
	List<Subject> prerequisiteSubjects;
		
	public Subject(String name) {
		this.name = name;
		prerequisiteSubjects = new ArrayList<Subject>();
	}
	public Subject(String name, Subject...prerequisiteSubjects) {
		this(name);
		this.prerequisiteSubjects.addAll(Arrays.asList(prerequisiteSubjects));
	}
	
	public void addPrerequisite(Subject subject){
		prerequisiteSubjects.add(subject);
	}
	
	public void removePrerequisiste(Subject subject) {
		prerequisiteSubjects.remove(subject);
	}
			
	public int getNumberOfPrerequisites(){
		return prerequisiteSubjects.size();
	}
	
	public int getUnits(){
		return units;
	}
	
	public List<Subject> getPrerequisites() {
		return prerequisiteSubjects;
	}
	
	public boolean equals(Object o){
		if (this == o) return true;
		if (!(o instanceof Section)) return false;
		Subject s = (Subject) o;
		if (this.name == s.name) return true;
		return false;
	}
	
}
