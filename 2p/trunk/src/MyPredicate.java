
public class MyPredicate implements java.util.function.Predicate<String> {
	public boolean test(String s){
		return s.length()>4;
	}
}
