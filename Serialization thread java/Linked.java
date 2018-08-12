/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication55;

/**
 *
 * @author pc
 */


import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Linked {
	public static void main(String [] N) throws InterruptedException {
		
		linkedList l = new linkedList();
		new Consumer(l,"ConsC1").start();
		new Del(l,"x").start();
		new Producer(l,"c").start();
		
		new Consumer(l,"ConsC16").start();
		new Del(l,"x6").start();
		new Producer(l,"c6").start();
		new Consumer(l,"ConsC71").start();
		
		new Producer(l,"c7").start();
		
		new Consumer(l,"ConsC16").start();
		

		 
		 
		
		 

		
	
	}
}

class Consumer extends Thread {
	private linkedList T;
	
	public Consumer(linkedList l, String m) {
		setDaemon(true);
		setName(m);
		T=l;
	}
	public void run() {
		while(true)
			try { T.insert();   } catch (InterruptedException e) { e.printStackTrace(); }
	}
}
class Producer extends Thread {
	private linkedList T;
	public Producer(linkedList l, String m) {
		setDaemon(true);
		setName(m);
		T=l;
	}
	public void run() {
		while(true)
			try { T.display();    } catch (InterruptedException e) { e.printStackTrace(); }		
	}
}
class Del extends Thread {
	private linkedList T;
	public Del(linkedList l, String m){
		setDaemon(true);
		setName(m);
		T=l;
	}
	public void run() {
		while(true) {
			try { T.deleteIndex();     } catch (InterruptedException e) { e.printStackTrace(); }
		}
	}
}


//********************************************

class Node {
    private int Num;
    private Node link;
    
    public Node()
    {
        link = null;
        Num = 0;
    }    
    public Node(int d,Node n) {
        Num = d;
        link = n;
    }    
    public void setLink(Node n) { link = n; }
    public void setNum(int d) {   Num = d; }
    public Node getLink() { return link;}
    public int getNum() {  return Num; }
}

class linkedList {
    private Node start;
    private Node end ;
    public int size ;
    private int count;
    private Lock l;
    private Condition can;
    private Random r = null;

    public linkedList() {
        start = null;
        end = null;
        size = 0;
        count=0;
        r = new Random();
        l= new ReentrantLock();
        can = l.newCondition();
    }
    public boolean isEmpty() { return start == null; }
    public int getSize() { return size; }  
    
    public void insert() throws InterruptedException
    {
    	l.lock();
    	while(count == 10) { can.await(); }
    	count++;
    	int val = r.nextInt(100);
    	System.out.println("value added: " + val);
        Node nptr = new Node(val,null);    
        size++ ;    
        if(start == null) 
        {
            start = nptr;
            end = start;
        }
        else 
        {
            end.setLink(nptr);
            end = nptr;
        }
        can.signalAll();
        l.unlock();
    }

    public void deleteIndex() throws InterruptedException
    { 
    	l.lock();
   	while(count == 0) {can.await();}
    	int ind = r.nextInt(10);
    	
        if (ind == 1) 
        {
        	 System.out.println("Node  at index deleted: index " + ind );
            start = start.getLink();
           
            size--; 
            count--;
            return ;
            
        }
        if (ind == size) 
        {
            Node s = start;
            Node t = start;
            while (s != end)
            {
                t = s;
                
                s = s.getLink();
            }
            System.out.println("Node  at index deleted: index " + ind );
            count--;
            end = t;
            end.setLink(null);
            size --;
            return;
        }
        Node ptr = start;
       
        ind--;
        for (int i = 1; i < size - 1; i++) 
        {
            if (i == ind) 
            {
                Node tmp = ptr.getLink();
                int t = ind+1;
                System.out.println("Node at index deleted: index " + t );
                count--;
                tmp = tmp.getLink();
                ptr.setLink(tmp);
                break;
            }
            ptr = ptr.getLink();
        }
        size-- ;
        can.signalAll();
        l.unlock();
    }    
   
    public void display() throws InterruptedException {
    	l.lock();
        while (count == 0) {can.await(); } 
    	  System.out.println("elements inside list: ");
        if (start.getLink() == null) 
        {
            System.out.println(start.getNum() );
            return;
        }
      
        Node ptr = start;
        System.out.print(start.getNum()+ " ");
        ptr = start.getLink();
        while (ptr.getLink() != null)
        {
            System.out.print(ptr.getNum()+ " ");
            ptr = ptr.getLink();
        }
        System.out.print(ptr.getNum()+ " \n");
        can.signalAll();
        l.unlock();
    }
}