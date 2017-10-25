package com.lottery.search;

public enum Operator {
	
	Eq("=",1), Gt(">",2), Ge(">=",3), Lt("<",4), Le("<=",5), Like("like",6);
	
	private String name;
	private int index ;
    
	private Operator( String name , int index ){
        this.name = name ;
        this.index = index ;
    }
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public static Operator index2Enum(int index){
		Operator[] arr=Operator.values();
		Operator result=null;
		for (Operator operator : arr) {
			if(operator.getIndex()==index){
				result=operator;
				break;
			}
		}
		return result;
	}
}
	
