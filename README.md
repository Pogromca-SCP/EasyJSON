# EasyJSON
 Small and easy to use JSON parser for Java.
 
 ## Reading data from JSON
 ### Deserializing JSON as an array
 ```java
import java.io.Reader;
import org.json.easy.dom.JSONArray;
import org.json.easy.serialization.JSONReader;
import org.json.easy.serialization.JSONSerializer;

// ...

JSONArray arr;

try (JSONReader reader = new JSONReader(/* Insert your reader here */)) {
    arr = JSONSerializer.deserializeArray(reader);
}
 ```
 ### Deserializing JSON as an object
 ```java
import java.io.Reader;
import org.json.easy.dom.JSONObject;
import org.json.easy.serialization.JSONReader;
import org.json.easy.serialization.JSONSerializer;

// ...

JSONObject obj;

try (JSONReader reader = new JSONReader(/* Insert your reader here */)) {
    obj = JSONSerializer.deserializeObject(reader);
}
 ```
 ### Deserializing JSON as a value (for scenarios where data can be either an object or an array)
 ```java
import java.io.Reader;
import org.json.easy.dom.JSONValue;
import org.json.easy.serialization.JSONReader;
import org.json.easy.serialization.JSONSerializer;

// ...

JSONValue value;

try (JSONReader reader = new JSONReader(/* Insert your reader here */)) {
    value = JSONSerializer.deserialize(reader);
}
 ```
 ## Writing data to JSON
 ### Serializing an array
 ```java
import java.io.Writer;
import org.json.easy.policies.JSONPrintPolicy;
import org.json.easy.dom.JSONArray;
import org.json.easy.util.JSONArrayBuilder;
import org.json.easy.serialization.JSONWriter;
import org.json.easy.serialization.JSONSerializer;

// ...

JSONArray arr = new JSONArrayBuilder().add(true).add(12.34)/* ... */.add("Example text").asArray();

try (JSONWriter writer = new JSONWriter(/* Insert your writer here */, /* Insert JSON print policy here if default formatting is not desired */)) {
    JSONSerializer.serialize(arr, writer);
}
 ```
 ### Serializing an object
 ```java
import java.io.Writer;
import org.json.easy.policies.JSONPrintPolicy;
import org.json.easy.dom.JSONObject;
import org.json.easy.util.JSONObjectBuilder;
import org.json.easy.serialization.JSONWriter;
import org.json.easy.serialization.JSONSerializer;

// ...

JSONObject obj = new JSONObjectBuilder().set("bool", true).set("myNumber", 12.34)/* ... */.set("stringExample", "Example text").asObject();

try (JSONWriter writer = new JSONWriter(/* Insert your writer here */, /* Insert JSON print policy here if default formatting is not desired */)) {
    JSONSerializer.serialize(obj, writer);
}
 ```
 ### Creating custom JSON Print Policy (if you want to serialize JSON in your own format)
 ```java
import org.json.easy.policies.JSONPrintPolicy;

public class MyNewJSONFormat implements JSONPrintPolicy {
    // Implement interface methods to change JSON formatting behaviour
}
 ```
