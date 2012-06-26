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
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime
				* result
				+ ((prerequisiteSubjects == null) ? 0 : prerequisiteSubjects
						.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Subject other = (Subject) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (prerequisiteSubjects == null) {
			if (other.prerequisiteSubjects != null)
				return false;
		} else if (!prerequisiteSubjects.equals(other.prerequisiteSubjects))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Subject [name=" + name + ", units=" + units
				+ ", prerequisiteSubjects=" + prerequisiteSubjects + "]";
	}
	
	
	
}
