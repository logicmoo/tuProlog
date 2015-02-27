
/** Class Client 1.
* Interacts with a Student person directly. */
//public class Client {
//
//	public static void main(String[] args) {
//			IPerson p = new Student("Harry","Potter"); 
//			System.out.println(p.getName());
//			System.out.println(p.getSurname());
//} }


///**
//* Class Client 2.
//* Interacts with a Student person through a PersonProxy. */
//public class Client {
//	public static void main(String[] args) {
//		IPerson s = new Student("Harry","Potter"); 
//		IPerson p = new PersonProxy(s); 
//		System.out.println(p.getName());
//		System.out.println(p.getSurname());
//	}
//}

import java.lang.reflect.*; 
/**
* Class Client 3.
* Interacts with a Student person through a dynamically 
* generated PersonProxy.
*/
public class Client {
	public static void main(String[] args) {
		IPerson s = new Student("Harry","Potter");
		ClassLoader cl = IPerson.class.getClassLoader(); 
		IPerson p = (IPerson) Proxy.newProxyInstance(cl,
				new Class[] {IPerson.class}, new PersonHandler(s)); 
		System.out.println(p.getName());
		System.out.println(p.getSurname());
	}
}
