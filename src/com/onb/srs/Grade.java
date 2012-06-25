package com.onb.srs;

public enum Grade {

	F {
		public String toString(){ return "F"; }
		int getPointLimit() { return 150; }
		int getValue() { return 0; }
	},
	
	C {
		public String toString(){ return "C"; }
		int getPointLimit() { return 200; }
		int getValue() { return 200; }
	},
	
	BM {
		public String toString(){ return "B-"; }
		int getPointLimit() { return 250; }
		int getValue() { return 250; }
	},

	B {
		public String toString(){ return "B"; }
		int getPointLimit() { return 300; }
		int getValue() { return 300; }
	},
	
	AM {
		public String toString(){ return "A-"; }
		int getPointLimit() { return 350; }
		int getValue() { return 350; }
	},
	
	A {
		public String toString(){ return "A"; }
		int getPointLimit() { return 400; }
		int getValue() { return 400; }
	};
	
	abstract int getPointLimit();
	abstract int getValue();
	
	public static Grade toLetterGrade(int numberGrade){

		Grade letterGrade = F;
		
		if(numberGrade > AM.getPointLimit()){
			letterGrade = A;
		}
		else if(numberGrade > B.getPointLimit()){
			letterGrade = AM;
		}
		else if(numberGrade > BM.getPointLimit()){
			letterGrade = B;
		}
		else if(numberGrade > C.getPointLimit()){
			letterGrade = BM;
		}
		else if(numberGrade > F.getPointLimit()){
			letterGrade = C;
		}
		else if(numberGrade <= F.getPointLimit()){
			letterGrade = F;
		}
		
		return letterGrade;
	}
}
