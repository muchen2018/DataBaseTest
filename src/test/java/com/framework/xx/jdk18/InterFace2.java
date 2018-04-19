package com.framework.xx.jdk18;

public interface InterFace2 {
	
	static void test() {
		System.out.println("InterFace2-->test");
	}
	
	default void test2() {
		System.out.println("InterFace2-->test2");
	}

}
