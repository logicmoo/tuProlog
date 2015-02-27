import java.lang.reflect.*; 
public class PersonHandler implements InvocationHandler {
	private IPerson p;
	public PersonHandler(IPerson p) {this.p = p;}
	public Object invoke(Object proxy, Method m, Object[] args) throws Throwable {
		System.out.println("Person Handler: Invoking " + m.getName());
		return m.invoke(p, args); }
}