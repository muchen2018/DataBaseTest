package com.framework;

public class SpringThread implements Runnable{
	
	private int parameter;

    public SpringThread(int parameter){
        this.parameter = parameter;
    }
	
	@Override
	public void run() {
		
		System.out.println(Thread.currentThread().getName() + ":执行了..." + parameter);
		try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
	}

}
