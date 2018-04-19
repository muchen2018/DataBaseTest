package com.framework;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.framework.config.RabbitConfig;
import com.framework.modules.mq.Sender;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MqTest {

	@Autowired
	private Sender sender;

	@Test
	public void send() {
		/*sender.send(RabbitConfig.TOPIC_EXCHANGE, RabbitConfig.ROUTING_KEY, "test2");

		try {
			Thread.sleep(100000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}

	public static void main(String[] aa) {

		System.out.println(Instant.now().atZone(ZoneOffset.ofHours(8)));

		LocalDateTime now = LocalDateTime.now();

		System.out.println(now);

		DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

		System.out.println(f.format(now));

		List<String> a = new ArrayList<String>();
		a.add("1");
		a.add("2");
	
		a.forEach(e->{System.out.println(e);});

	}
}
