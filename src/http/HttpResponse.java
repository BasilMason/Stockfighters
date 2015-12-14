package http;

import java.io.InputStream;

/**
 * Created by Basil on 14/12/2015.
 */
public class HttpResponse {

    private int errorCode;
    private InputStream response;

    public HttpResponse(int errorCode, InputStream response) {
        this.errorCode = errorCode;
        this.response = response;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public InputStream getResponse() {
        return response;
    }
}
