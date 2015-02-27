
public class MyPersonComparator implements java.util.Comparator<Person> {
	public int compare(Person p1, Person p2){
		return p1.getSurname().compareTo(p2.getSurname());
	}
}
