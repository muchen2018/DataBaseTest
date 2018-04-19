package com.framework.xx.jdk18;

public interface InterFace1 {
	
	static void test() {
		System.out.println("InterFace1-->test");
	}
	
	default void test2() {
		System.out.println("InterFace1-->test2");
	}

}
