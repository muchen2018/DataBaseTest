package com.framework.modules.mq;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Test {

	public static void main(String[] a) {
		new Thread(() -> {
			System.out.println(2);
		}).start();

		Map<String, Object> map = new ConcurrentHashMap<>();
		
		map.put("12", null);

	}

}
