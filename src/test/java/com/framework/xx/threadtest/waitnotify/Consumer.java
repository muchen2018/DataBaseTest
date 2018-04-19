package com.framework.xx.threadtest.waitnotify;

public class Consumer extends Thread{
	
	
	private Storage s;
	
	public Consumer(Storage s) {
		this.s=s;
	}
	
	@Override
	public void run() {
		while (true) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			s.consumer();
		}
		
	}

}
