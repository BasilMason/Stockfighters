package http;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Basil on 14/12/2015.
 */
public class HttpUtils {

    private final static String USER_AGENT = "Mozilla/5.0";

    public static HttpResponse sendGet(String url) throws Exception {

        URL obj = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) obj.openConnection();

        // optional default is GET
        conn.setRequestMethod("GET");

        //add request header
        conn.setRequestProperty("User-Agent", USER_AGENT);

        return new HttpResponse(conn.getResponseCode(), conn.getInputStream());

    }

}
