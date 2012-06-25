package com.onb.srs;

public enum TimeSlot {
	EightThirtyToTen("8:30am-10:00am"),
	TenToElevenThirty("10:00am-11:30am"), 
	ElevenThirtyToOne("11:30am-1:00pm"), 
	OneToTwoThirty("1:00pm-2:30pm"), 
	TwoThirtyToFour("2:30pm-4:00pm"), 
	FourToFiveThirty("4:00pm-5:30pm");
	
	private String legibleFormat;
	TimeSlot(String legibleFormat){
		this.legibleFormat = legibleFormat;
	}
	
	@Override
	public String toString(){
		return legibleFormat;
	}
}
