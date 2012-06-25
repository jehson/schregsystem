package com.onb.srs;

public enum DaySlot {
	MonThu("Monday/Thursday"),
	TueFri("Tuesday/Friday"),
	WedSat("Wednesday/Saturday");
	
	private String legibleFormat;
	DaySlot(String legibleFormat){
		this.legibleFormat = legibleFormat;
	}
	
	@Override
	public String toString(){
		return legibleFormat;
	}
	
}
