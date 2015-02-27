import java.util.function.BiFunction;


public class MyStringBiFunction implements BiFunction<String,String,String> {	
	public String apply(String x, String y){
		return x+y;
	}
}
