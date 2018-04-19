package com.framework.xx.threadtest.arrayqueue;

//目的是当head=tail的时候。通过size=0还是size=数组长度。来区分队列为空，或者队列已满。
public class QueueArray {
	Object[] a; // 对象数组，队列最多存储a.length-1个对象
	int head; // 队首下标
	int tail; // 队尾下标

	public QueueArray() {
		this(10); // 调用其他构造方法
	}

	public QueueArray(int size) {
		a = new Object[size];
		head = 0;
		tail = 0;
	}

	/**
	 * 将一个对象追加到队列尾部
	 * 
	 * @param obj
	 *            对象
	 * @return 队列满时返回false,否则返回true
	 */
	public boolean enqueue(Object obj) {
		if ((tail + 1) % a.length == head) {
			return false;
		}
		a[tail] = obj;
		tail = (tail + 1) % a.length;
		return true;
	}

	/**
	 * 队列头部的第一个对象出队
	 * 
	 * @return 出队的对象，队列空时返回null
	 */
	public Object dequeue() {
		if (tail == head) {
			return null;
		}
		Object obj = a[head];
		head = (head + 1) % a.length;
		return obj;
	}

	public static void main(String[] args) {
		QueueArray q = new QueueArray(4);
		System.out.println(q.enqueue("张三"));
		System.out.println(q.enqueue("李斯"));
		System.out.println(q.enqueue("赵五"));
		System.out.println(q.enqueue("王一"));// 无法入队列，队列满
		for (int i = 0; i < 4; i++) {
			System.out.println(q.dequeue());
		}
	}
}