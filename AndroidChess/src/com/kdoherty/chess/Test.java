package com.kdoherty.chess;

import java.util.ArrayList;

public class Test {
	
	final ArrayList<String> strs;
	
	public Test(ArrayList<String> strs) {
		this.strs = strs;
	}
	
	
	
	public static void main(String [] args) {
		ArrayList<String> arg = new ArrayList<String>();
		arg.add("2");
		Test t = new Test(arg);
		System.out.println(t);
		t.strs.remove(0);
		System.out.print(t);
	}

}
