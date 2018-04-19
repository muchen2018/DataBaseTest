package com.framework.xx.threadtest.blockqueue;

public class Consumer implements Runnable{
	
	@Override
	public void run() {
		while (true) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			
			if(0==Storage.list.size()) {
				System.out.println("队列已空");
			}
			
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			try {
				Storage.list.take();
				
				System.out.println("消费者已消费 size:"+Storage.list.size());
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
