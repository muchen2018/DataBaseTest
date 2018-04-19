package com.framework.xx.serialize;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * 父类 子类 private 可以被序列化
 * transient 不可被序列化
 * static 不可被序列化
 * 
 * 如果 父类未实现 serializable 接口,是子类实现的,那么 父类的属性 不会保持值.而且 父类必须有无参构造函数
 * 
 * @author yuan
 *
 */
public class SerializeTest {
	
	public static void main(String [] a){
		
		try {
			SerializeTest.seriazed();
			SerializeTest.seriazed2();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void seriazed() throws IOException {
		
		FileOutputStream fos=new FileOutputStream("D:\\user.txt");
		ObjectOutputStream oos=new ObjectOutputStream(fos);
		
		User user=new User();
		user.setPassword("123");
		user.setUsername("us");
		user.setSex("man");
		
		oos.writeObject(user);
		oos.close();
	}
	
	public static void seriazed2() throws IOException, ClassNotFoundException {
		
		FileInputStream fis=new FileInputStream("D:\\user.txt");
		
		ObjectInputStream ois=new ObjectInputStream(fis);
		
		User user=(User)ois.readObject();
		
		System.out.println("password:"+user.getPassword());
		System.out.println("username:"+user.getUsername());
		System.out.println("sex:"+user.getSex());
		System.out.println("static:"+BaseUser.type);
	}

}
