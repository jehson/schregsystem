package com.onb.srs;

public class Schedule {
	DaySlot daySlot;
	TimeSlot timeSlot;
	
	public Schedule(DaySlot daySlot, TimeSlot timeSlot){
		this.daySlot = daySlot;
		this.timeSlot = timeSlot;
	}
	
	@Override
	public boolean equals(Object o){
		assert(o!=null);
		
		if(this == o){ 
			return true; 
		}
		
		if (!(o instanceof Schedule)){
			return false;
		}
		
		Schedule schedule = (Schedule)o;
		return this.toString().equals(schedule.toString());
	}
	
	@Override
	public int hashCode(){
		int result = 17;
		result = 37 * result + daySlot.toString().hashCode();
		result = 37 * result + timeSlot.toString().hashCode();
		return result;
	}
	
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append(daySlot.toString());
		sb.append(" ");
		sb.append(timeSlot.toString());
		return sb.toString();
	}
}
