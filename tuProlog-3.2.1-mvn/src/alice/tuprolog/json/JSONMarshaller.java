package alice.tuprolog.json;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

//Alberto
public class JSONMarshaller implements JsonSerializer<Object>, JsonDeserializer<Object> {

	private static final String TYPE_INFO = "TYPE_INFO";

	@Override
	public JsonElement serialize(Object object, Type type, JsonSerializationContext jsonSerializationContext) {
		JsonElement jsonEle = jsonSerializationContext.serialize(object, object.getClass());
		jsonEle.getAsJsonObject().addProperty(TYPE_INFO, object.getClass().getCanonicalName());
		return jsonEle;
	}
	
	@Override
	public Object deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
		JsonObject jsonObj = jsonElement.getAsJsonObject();
		String className = jsonObj.get(TYPE_INFO).getAsString();
		try {
			Class<?> clz = Class.forName(className);
			return jsonDeserializationContext.deserialize(jsonElement, clz);
		}
		catch (ClassNotFoundException e) {
			throw new JsonParseException(e);
		}
	}
	
}
