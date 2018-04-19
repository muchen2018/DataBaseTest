package com.framework.xx.threadtest.linkedqueue;

public class Queue {
	
	public Node head;
	
	public Node tail;
	
	public void add(Node node) {
		
		if(head == null) {
			head = node;
		}else {
			tail.next=node;
		}
		tail = node;
	}
	
	public Object take() {
		
		if(head == null) {
			System.out.println("empty");
			return null;
		}else {
			
			Node tmp = head;
			head=head.next;
			return tmp.data;
			
		}
		
	}
	
	public void display() {
		
		Node n= head;
		
		while(n != null) {
			System.out.println(n.data);
			n=n.next;
		}
	}
	
	public static void main(String [] a) {
		
		Queue q=new Queue();
		
		q.display();
		
		q.add(new Node(1));
		q.add(new Node(2));
		q.add(new Node(3));
		
		q.display();
		
		q.take();
		
		q.display();
		
	}
}
