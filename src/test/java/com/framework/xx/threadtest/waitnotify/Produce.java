package com.framework.xx.threadtest.waitnotify;

public class Produce extends Thread{
	
	
	private Storage s;
	
	public Produce(Storage s) {
		this.s=s;
	}
	
	@Override
	public void run() {
		while (true) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			s.pop();
		}
		
	}

}
