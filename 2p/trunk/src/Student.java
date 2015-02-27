
/**
 * Class Student
 */
public class Student implements IPerson { 
	private String name;
	private String surname;
	private String gender;
	private int age;
	
	public Student(String name, String surname) {
		this.name = name;
		this.surname=surname;
	}
	
	public String getName(){return this.name;}
		
	public String getSurname(){return this.surname;}
		
	public String getGender(){return this.gender;}
		
	public int getAge(){return this.age;}
}
