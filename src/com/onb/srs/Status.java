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
			(prevAverageGrade.compareTo(PASSING_AVERAGE_GRADE)>=0)? CONTINUING : PROBATIONARY;
			
		}
	},
	
	GRADUATING{
		int getMinUnits(){ return 0; }
		int getMaxUnits(){ return 999; }
		boolean isEligible(){ return true; }
	}, 
	
	PROBATIONARY{
		int getMinUnits(){ return 15; }
		int getMaxUnits(){ return 18; }
		boolean isEligible(){ return true; }
	},
	
	INELIGIBLE{
		int getMinUnits(){ return -1; }
		int getMaxUnits(){ return -1; }
		boolean isEligible(){ return false; }
	}, 
	
	GRADUATE{
		int getMinUnits(){ return -1; }
		int getMaxUnits(){ return -1; }
		boolean isEligible(){ return false; }
	};

	abstract int getMinUnits();
	abstract int getMaxUnits();
	abstract boolean isEligible();
	abstract Status next(Grade averageGrade, int remainingUnits);
	
	private final static Grade PASSING_AVERAGE_GRADE = Grade.C; 
	private final static int GRADUATING_MIN_UNITS = 18;
	
	public static Status changeStatus(Student student){
		if(student.getNumberOfEnrollmentForms() == 0){
			return Status.NEW;
		}
		else {
			return changeOldStatus(student);
		}
	}

	private static Status changeOldStatus(Student student){
		
		if(student.getRemainingUnits() == 0){
			return Status.GRADUATE;
		} else if(student.getRemainingUnits() <= GRADUATING_MIN_UNITS){
			return Status.GRADUATING;
		}
				
		Grade prevAverageGrade = student.calculatePreviousAverageGrade();
		//if student got less than passing average grade
		if(prevAverageGrade.compareTo(PASSING_AVERAGE_GRADE) < 0){
			if(student.getStatus() == Status.PROBATIONARY){
				return Status.INELIGIBLE;
			}
			else {
				return Status.PROBATIONARY;
			}	
		}
		
		return Status.CONTINUING;
	}
	
}