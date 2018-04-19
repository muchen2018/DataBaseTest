package com.framework.xx.threadtest.blockqueue;

import java.util.concurrent.LinkedBlockingQueue;

public class Storage {
	
	// 仓库最大存储量
    public static  final int MAX_SIZE = 20;
	
    public static LinkedBlockingQueue<Object> list=new LinkedBlockingQueue<>(MAX_SIZE);
	
	

}
