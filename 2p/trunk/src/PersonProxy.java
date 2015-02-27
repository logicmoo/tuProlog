
/**
* Class PersonProxy. */
public class PersonProxy implements IPerson { 
	private IPerson p;
	public PersonProxy(IPerson p) {this.p = p;}
	public String getName() {
		System.out.println("PersonProxy: Begin of getName()"); 
		return p.getName();
	}
	public String getSurname(){
		System.out.println("PersonProxy: Begin of getSurname()"); 
		return p.getSurname();
	}
	public String getGender(){
		System.out.println("PersonProxy: Begin of getGender()"); 
		return p.getGender();
	}	
	public int getAge(){
		System.out.println("PersonProxy: Begin of getAge()"); 
		return p.getAge();
	}		
}