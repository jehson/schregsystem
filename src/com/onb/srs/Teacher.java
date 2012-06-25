package com.onb.srs;

import java.util.List;

import com.onb.srs.exceptions.DuplicateSectionException;
import com.onb.srs.exceptions.ScheduleConflictException;

public class Teacher {
	private int id;
	private List<Section> sections;
	
	public Teacher(int id){
		this.id = id;
	}
	
	public void addSection(Section sectionToAdd) throws DuplicateSectionException, ScheduleConflictException{
		if(sections.contains(sectionToAdd)){
			throw new DuplicateSectionException("Teacher already has this section.");
		}
		else{
			for(Section currentSection: sections){
				if(currentSection.equalsSchedule(sectionToAdd)){
					throw new ScheduleConflictException("Schedule of teacher is in conflict.");
				}
				sections.add(sectionToAdd);
			}
		}
	}
	
	public void removeSection(Section section){
		sections.remove(section);
	}
}
