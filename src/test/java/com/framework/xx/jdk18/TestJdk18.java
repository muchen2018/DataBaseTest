package com.framework.xx.jdk18;

public class TestJdk18 implements InterFace1,InterFace2{

	@Override
	public void test2() {
		InterFace1.super.test2();
	}
	
	public static void main(String [] a) {
		InterFace1.test();
		InterFace2.test();
		new TestJdk18().test2();
		
	}

}
