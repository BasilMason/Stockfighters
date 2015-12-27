package websocket;

import javax.json.Json;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.StringReader;
import java.net.URI;

/**
 * Created by Basil on 27/12/2015.
 */
public class WSQuote extends WSClient {
    public WSQuote(URI uri) {
        super(uri);
    }

    public int getBid() {

        JsonReader jsonReader = Json.createReader(new StringReader(getMessage()));
        JsonObject object = jsonReader.readObject();
        JsonObject quote = (JsonObject) object.get("quote");
        JsonNumber bid = (JsonNumber) quote.get("bid");

        return bid.intValue();

    }
}
