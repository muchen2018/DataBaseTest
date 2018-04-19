package com.framework.xx.threadtest;

/**
 *
 * volatile 只保证 可见性和有序性 不保证操作的原子性
 *
 */
public class ThreadTest implements Runnable {

	private static int a = 0;

	public static synchronized void add() {
		a++;
	}

	@Override
	public void run() {

		for (int i = 0; i < 1000; i++) {
			ThreadTest.add();
		}

	}

	public static void main(String[] a) {
		
		ThreadTest tt =new ThreadTest();

		for (int i = 0; i < 10; i++) {
			Thread t= new Thread(tt);
			
			t.setName("ThreadName"+i);
			
		}
		
		while(Thread.activeCount()>1) {
			Thread.yield();
		}
		
		System.out.println(ThreadTest.a);

	}

}
