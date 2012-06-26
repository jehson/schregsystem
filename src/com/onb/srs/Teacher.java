package com.onb.srs;

import java.util.ArrayList;
import java.util.List;

import com.onb.srs.exceptions.DuplicateSectionException;
import com.onb.srs.exceptions.ScheduleConflictException;

public class Teacher {
	private int id;
	private List<Section> sections;
	
	public Teacher(int id){
		this.id = id;
		this.sections = new ArrayList<Section>();
	}
	
	public void addSection(Section sectionToAdd) {
		this.sections.add(sectionToAdd);
	}
	
	public boolean hasClassAt(Schedule sched) {
		for (Section s : sections) {
			if (s.getSchedule().equals(sched)) return true;
		}
		return false;
	}
	
	public boolean hasSection(Section section) {
		if (this.sections.contains(section)) return true;
		else return false;
	}
	
	public void removeSection(Section section){
		sections.remove(section);
	}
	
	public List<Section> getSections() {
		return sections;
	}
}
