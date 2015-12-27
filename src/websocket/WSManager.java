package websocket;

import stockfighters.SFStocks;
import stockfighters.SFUtils;
import stockfighters.SFVenues;

import javax.json.JsonObject;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by Basil on 27/12/2015.
 */
public class WSManager {

    // thread each WS

    private WSClient quoteWebsocket = null;
    private WSClient executionWebsocket = null;

    private String tradingAccount;
    private SFVenues venue;
    private SFStocks stock;



    public WSManager(WSType type, String tradingAccount, SFVenues venue) {

        /*switch (type) {
            case QUOTE:
                getQuoteWebsocket(tradingAccount, venue);
        }*/

        this.tradingAccount = tradingAccount;
        this.venue = venue;
        this.stock = null;
    }

    public JsonObject getQuoteJson() {
        return null;
    }

    public int getQuoteAsk() {
        return 0;
    }

    // quote websocket
    public WSClient getQuoteWebsocket(String account, SFVenues venue) throws URISyntaxException {

        if (quoteWebsocket == null) {
            quoteWebsocket = new WSClient(new URI(SFUtils.urlQuoteWss(account, venue)));
        }


        return quoteWebsocket;

    }



    // execution websocket

}
