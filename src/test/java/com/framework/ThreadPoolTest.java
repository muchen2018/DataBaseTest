package com.framework;

import java.util.concurrent.Executor;

import org.apache.tika.exception.TikaException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.xml.sax.SAXException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ThreadPoolTest {
	
	@Autowired
	private Executor executor;

	@Test
	public void test() throws SAXException, TikaException, Exception {
		
		int n = 20;
		for (int i = 0; i < n; i++) {
			executor.execute(new SpringThread(i));
		}
		 System.out.println("main process is finish .....");
		 
		 Thread.sleep(100000);
	}
	
	public static void main(String [] a ) {
	}
}
