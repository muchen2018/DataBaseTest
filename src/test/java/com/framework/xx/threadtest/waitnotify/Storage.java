package com.framework.xx.threadtest.waitnotify;

public class Storage {
	
	public static final int count=10;
	
	private int current=0;
	
	public synchronized void pop() {
		
		while(current==count) {
			try {
				wait();
				System.out.println("产品已满,请稍候再生产");
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		current++;
		notifyAll();
		System.out.println("生产者生产了第"+this.current+"个产品");
	}
	
	public synchronized void consumer() {
		
		while(current==0) {
			try {
				wait();
				System.out.println("产品已空,请稍候再消费");
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("消费者消费了第" + this.current + "个产品"); 
		current--;
		notifyAll();
		       
	}

}
