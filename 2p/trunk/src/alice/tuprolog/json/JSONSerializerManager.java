package alice.tuprolog.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import alice.tuprolog.Term;

//Alberto
public class JSONSerializerManager {
	
	static Gson gson = new GsonBuilder()
			.registerTypeAdapter(Term.class, new JSONMarshaller()) //Mandatory for serializing query!
	        .create();
	
	public static String toJSON(Object o){
		return gson.toJson(o);
	}
	
	public static <T> T fromJSON(String jsonString, Class<T> klass){
		return gson.fromJson(jsonString, klass);
	}
	
}
