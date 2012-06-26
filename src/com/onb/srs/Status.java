package com.onb.srs;

public enum Status {
	
	NEW {
		int getMinUnits(){ return 15; }
		int getMaxUnits(){ return 18; }
		boolean isEligible(){ return true; }
		Status next(Grade prevAverageGrade, int remainingUnits){
			return (prevAverageGrade.compareTo(PASSING_AVERAGE_GRADE)>=0)? CONTINUING : PROBATIONARY;
		}
	},
	
	CONTINUING {
		int getMinUnits(){ return 18; }
		int getMaxUnits(){ return 24; }
		boolean isEligible(){ return true; }
		Status next(Grade prevAverageGrade, int remainingUnits){
			return ((prevAverageGrade.compareTo(PASSING_AVERAGE_GRADE)>=0)?
					( (remainingUnits<=GRADUATING_MIN_UNITS)?
							(remainingUnits==0?GRADUATE:GRADUATING) : CONTINUING )
					: PROBATIONARY);
		}
	},

	PROBATIONARY{
		int getMinUnits(){ return 15; }
		int getMaxUnits(){ return 18; }
		boolean isEligible(){ return true; }
		Status next(Grade prevAverageGrade, int remainingUnits){
			Status temp = CONTINUING.next(prevAverageGrade, remainingUnits);
			return (temp == PROBATIONARY)? INELIGIBLE: temp;
		}
	},
	
	GRADUATING{
		int getMinUnits(){ return 0; }
		int getMaxUnits(){ return 999; }
		boolean isEligible(){ return true; }
		Status next(Grade prevAverageGrade, int remainingUnits){
			return ((prevAverageGrade.compareTo(PASSING_AVERAGE_GRADE)>=0)?
					(remainingUnits==0?GRADUATE:GRADUATING): PROBATIONARY);
		}
	}, 
	
	
	INELIGIBLE{
		int getMinUnits(){ return -1; }
		int getMaxUnits(){ return -1; }
		boolean isEligible(){ return false; }
		Status next(Grade prevAverageGrade, int remainingUnits) {
			return INELIGIBLE;
		}
	}, 
	
	GRADUATE{
		int getMinUnits(){ return -1; }
		int getMaxUnits(){ return -1; }
		boolean isEligible(){ return false; }
		Status next(Grade prevAverageGrade, int remainingUnits){
			return GRADUATE;
		}
	};

	abstract int getMinUnits();
	abstract int getMaxUnits();
	abstract boolean isEligible();
	abstract Status next(Grade averageGrade, int remainingUnits);
	
	private final static Grade PASSING_AVERAGE_GRADE = Grade.C; 
	private final static int GRADUATING_MIN_UNITS = 18;
	
}