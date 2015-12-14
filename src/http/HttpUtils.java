package http;

import javax.net.ssl.HttpsURLConnection;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

/**
 * Created by Basil on 14/12/2015.
 */
public class HttpUtils {

    private final static String API_KEY = "d4668d882aeec8d35bcfc23a0d401e6678d904ef";
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

    public static HttpResponse sendPost(String url, Map<String, Object> params) throws Exception {

        System.out.println(url);
        URL obj = new URL(url);
        HttpsURLConnection conn = (HttpsURLConnection) obj.openConnection();

        // unpack params
        StringBuilder postData = new StringBuilder();
        postData.append("{");
        for (Map.Entry<String,Object> param : params.entrySet()) {
            if (postData.length() != 1) postData.append(", ");
            postData.append("\"");
            postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
            postData.append("\"");
            postData.append(": ");

            if (!(param.getKey().equals("qty") || param.getKey().equals("price"))) postData.append("\"");
            postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
            if (!(param.getKey().equals("qty") || param.getKey().equals("price"))) postData.append("\"");

        }
        postData.append("}");

        System.out.println(postData);
        byte[] postDataBytes = postData.toString().getBytes("UTF-8");

        //add request header
        conn.setRequestMethod("POST");
        conn.setRequestProperty("User-Agent", USER_AGENT);
        conn.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        conn.setRequestProperty("X-Starfighter-Authorization", API_KEY);
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Accept", "application/json");
        conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
        conn.setDoOutput(true);
        conn.getOutputStream().write(postDataBytes);

        return new HttpResponse(conn.getResponseCode(), conn.getInputStream());

    }

}
