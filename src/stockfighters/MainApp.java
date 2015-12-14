package stockfighters;

import http.HttpResponse;
import http.HttpUtils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Basil on 14/12/2015.
 */
public class MainApp {

    public static void main(String[] args) {
        new MainApp().launch();
    }

    private void launch() {

        try {

            HttpResponse response = HttpUtils.sendGet(SFUtils.urlHeartbeat());

            printErrorCode(response.getErrorCode());
            printResponse(response.getResponse());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }

    }

    private void printErrorCode(int errorCode) {
        System.out.println("Error Code: " + errorCode);
    }

    private void printResponse(InputStream response) throws Exception {

        BufferedReader in = new BufferedReader(new InputStreamReader(response));
        String inputLine;
        StringBuffer sb = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            sb.append(inputLine);
        }
        in.close();

        System.out.println(sb.toString());

    }

}
