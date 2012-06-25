package com.onb.srs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Curriculum {
	
	private final String name;
	private List<Subject> subjects;
	
	public Curriculum(String name){
		this.name = name;
		subjects = new ArrayList<Subject>();
	}
	
	public Curriculum(String name, Subject...subjects){
		this(name);
		this.subjects.addAll(Arrays.asList(subjects));
	}
	
	public int getTotalUnits(){
		int totalUnits = 0;
		for(Subject subject: subjects){
			totalUnits += subject.getUnits();
		}
		return totalUnits;
	}
}
