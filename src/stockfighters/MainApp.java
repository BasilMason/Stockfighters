package stockfighters;

import http.HttpResponse;
import http.HttpUtils;
import json.JsonUtils;
import websocket.WSClient;

import javax.json.*;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.OptionalInt;

/**
 * Created by Basil on 14/12/2015.
 */
public class MainApp {

    public static void main(String[] args) {
        new MainApp().launch();
    }

    private void launch() {

        /**
         * Current
         */

        // 6. Quotes
        //testQuotes();
        sell_side();

        /**
         * Old
         */

        // 1. Heartbeat]
        //testHeartbeat();

        // 2. Venues
        //testVenue(SFVenues.TESTEX);

        // 3. Stocks
        //testStocks(SFVenues.TESTEX);

        // 4. Order book
        //testOrderbook(SFVenues.TESTEX, SFStocks.FOOBAR);

        // 5. chock_a_block
        //chock_a_block();

        // 6. Quotes
        //testQuotes();

        // 7. sell_side
        //sell_side();

    }

    /* LEVELS */
    private void chock_a_block() {

        String account = "ARP92565557";
        SFVenues venue = SFVenues.BNOEX;
        SFStocks stock = SFStocks.EPG;
        OptionalInt price = null;
        int qty = 50;
        SFOrderDirection direction = SFOrderDirection.buy;
        SFOrderType orderType = SFOrderType.MARKET;

        int purchased = 0;
        int totQty = 100000;

        while (purchased < totQty) {
            System.out.println(purchased);
            purchased += testPlaceOrder(new SFOrder(account, venue, stock, price, qty, direction, orderType));

            try {
                Thread.sleep(3000);                 //1000 milliseconds is one second.
            } catch(InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }

    }

    private void sell_side() {

        // 1. setup level
        String account = "TRB64098977";
        SFVenues venue = SFVenues.FSNBEX;
        SFStocks stock = SFStocks.RAR;
        int qty = 50, position = 0, nav = 0;
        int bid = 0, bidSize = 0, ask = 0, askSize = 0, spread = 0, buyAt = 0, sellAt = 0;
        int bidId = 0, askId = 0;

        // 2. main processing until profit goal
        while (nav < 1000) {

            // get current quote
            HttpResponse quote = null;
            boolean quoted = false;

            // 3. get quote
            while (!quoted) {

                try {
                    quote = HttpUtils.sendGet(SFUtils.urlQuote(venue, stock));

                    JsonReader jsonReader = Json.createReader(quote.getResponse());
                    JsonObject object = jsonReader.readObject();

                    // if quote ok
                    if (object.get("ok").getValueType() == JsonValue.ValueType.TRUE) {

                        JsonNumber nBid = (JsonNumber) object.get("bid");
                        JsonNumber nBidSize = (JsonNumber) object.get("bidSize");
                        JsonNumber nAsk = (JsonNumber) object.get("ask");
                        JsonNumber nAskSize = (JsonNumber) object.get("askSize");

                        bid = nBid.intValue() - 100;
                        bidSize = nBidSize.intValue();
                        ask = nAsk.intValue() + 100;
                        askSize = nAskSize.intValue();

                        System.out.println("Bid: " + bid + ", Size: " + bidSize);
                        System.out.println("Ask: " + ask + ", Size: " + askSize);

                        quoted = true;
                    }

                } catch (Exception e) {
                    System.out.println("No connection");
                    //e.printStackTrace();
                }

            }

            // 4. set limit order pair around quote
            if (bid != 0 && ask != 0) {

                boolean bidPlaced = false, askPlaced = false;

                // place bid until order id received
                while (!bidPlaced) {

                    // make bid
                    try {

                        // setup order
                        SFOrder buyOrder = new SFOrder(account, venue, stock, OptionalInt.of(bid), qty, SFOrderDirection.buy, SFOrderType.LIMIT);
                        System.out.println("Submitting order for stock: " + buyOrder.getStock() + " on venue: " + buyOrder.getVenue());

                        // get response
                        HttpResponse placeOrder = HttpUtils.sendPost(SFUtils.urlOrder(buyOrder.getVenue(), buyOrder.getStock()), buyOrder.getOrder());

                        // parse response
                        JsonReader jsonReader = Json.createReader(placeOrder.getResponse());
                        JsonObject object = jsonReader.readObject();

                        // if placed ok
                        if (object.get("ok").getValueType() == JsonValue.ValueType.TRUE) {
                            System.out.println("Bid Order ID: " + object.get("id"));
                            bidId = Integer.parseInt(object.get("id").toString());
                            bidPlaced = true;
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                    }

                }

                // place bid until order id received
                while (!askPlaced) {

                    // make ask
                    try {

                        SFOrder sellOrder = new SFOrder(account, venue, stock, OptionalInt.of(ask), qty, SFOrderDirection.sell, SFOrderType.LIMIT);

                        System.out.println("Submitting order for stock: " + sellOrder.getStock() + " on venue: " + sellOrder.getVenue());
                        HttpResponse placeOrder = HttpUtils.sendPost(SFUtils.urlOrder(sellOrder.getVenue(), sellOrder.getStock()), sellOrder.getOrder());

                        JsonReader jsonReader = Json.createReader(placeOrder.getResponse());
                        JsonObject object = jsonReader.readObject();

                        // if placed ok
                        if (object.get("ok").getValueType() == JsonValue.ValueType.TRUE) {
                            System.out.println("Ask Order ID: " + object.get("id"));
                            askId = Integer.parseInt(object.get("id").toString());
                            askPlaced = true;
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                    }

                }

            }

            nav+= 100;

        }



        // once one side executed, wait to see if open position clears too



        // if too long or market moves away, cancel order



        // repeat



        /*try {
            //connection = new WSQuote(new URI(SFUtils.urlQuoteStock(account, venue, stock)));
            final WSClient clientEndPoint = new WSClient(new URI(SFUtils.urlQuoteWss(account, venue)));

            try {
                while (true) {


                    while (nav < 1000) {

                        // set limit order pair
                        int quote, bid, bidSize, ask, askSize, spread, buyAt, sellAt;

                        bid = connection.getBid();

                        System.out.println("Bid: " + bid);

                        nav+= 100;

                    }

                    Thread.sleep(30000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }



        } catch (URISyntaxException e) {
            e.printStackTrace();
        }*/
    }

    // api call tests
    private void testHeartbeat() {
        try {
            System.out.println("Checking heartbeat");
            HttpResponse heartbeat = HttpUtils.sendGet(SFUtils.urlHeartbeat());

            JsonReader jsonReader = Json.createReader(heartbeat.getResponse());
            JsonObject object = jsonReader.readObject();
            jsonReader.close();

            JsonUtils.navigateTree(object, "ok");
            //navigateTree(object, "ok");

            System.out.println("Json: " + object.toString());
            //printErrorCode(heartbeat.getErrorCode());
            //printResponse(heartbeat.getResponse());
            System.out.println();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
    }

    private void testVenue(SFVenues venue) {
        try {
            System.out.println("Checking venue: " + venue.name());
            HttpResponse venueCheck = HttpUtils.sendGet(SFUtils.urlVenues(venue));

            printErrorCode(venueCheck.getErrorCode());
            printResponse(venueCheck.getResponse());
            System.out.println();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
    }

    private void testStocks(SFVenues venue) {
        try {
            System.out.println("Checking stocks on venue: " + venue.name());
            HttpResponse stockCheck = HttpUtils.sendGet(SFUtils.urlStocks(venue));

            printErrorCode(stockCheck.getErrorCode());
            printResponse(stockCheck.getResponse());
            System.out.println();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
    }

    private void testOrderbook(SFVenues venue, SFStocks stock) {
        try {
            System.out.println("Checking orderbook for stock: " + stock.name() + " on venue: " + venue.name());
            HttpResponse stockCheck = HttpUtils.sendGet(SFUtils.urlOrderbook(venue, stock));

            printErrorCode(stockCheck.getErrorCode());
            printResponse(stockCheck.getResponse());
            System.out.println();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
    }

    private int testPlaceOrder(SFOrder order) {

        int fill = 0;
        // String account, SFVenues venue, SFStocks stock, OptionalInt price, int qty, SFOrderDirection direction, SFOrderType orderType

        try {
            System.out.println("Submitting order for stock: " + order.getStock() + " on venue: " + order.getVenue());
            HttpResponse placeOrder = HttpUtils.sendPost(SFUtils.urlOrder(order.getVenue(), order.getStock()), order.getOrder());

            /*try {

            } catch (IOException e) {
                e.printStackTrace();
            }*/

            JsonReader jsonReader = Json.createReader(placeOrder.getResponse());
            JsonObject object = jsonReader.readObject();

            System.out.println("Filled: " + object.get("totalFilled"));
            fill = Integer.parseInt(object.get("totalFilled").toString());

            //JsonUtils.navigateTree(object, "totalFilled");

            //jsonReader.close();

            printErrorCode(placeOrder.getErrorCode());
            printResponse(placeOrder.getResponse());
            System.out.println();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }

        return fill;
    }

    private void testQuotes() {

        String account = "BPB17075937";
        SFVenues venue = SFVenues.GKHEX;

        // catch exception
        try {

            //set websocket client
            final WSClient clientEndPoint = new WSClient(new URI(SFUtils.urlQuoteWss(account, venue)));

           /* // handle message
            clientEndPoint.addMessageHandler(message -> {

                // create JSON from message
                JsonObject jsonObject = Json.createReader(new StringReader(message)).readObject();
                System.out.println(jsonObject.toString());
                *//*String userName = jsonObject.getString("user");
                if (!"bot".equals(userName)) {
                    clientEndPoint.sendMessage(getMessage("Hello " + userName +", How are you?"));
                    // other dirty bot logic goes here.. :)
                }*//*
            });*/

            try {
                while (true) {
                    //
                    // clientEndPoint.sendMessage(getMessage("Hi There!!"));
                    Thread.sleep(30000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //clientEndPoint.sendMessage(getMessage("Hi There!!"));

/*
            try {
                while (true) {
                    clientEndPoint.sendMessage(getMessage("Hi There!!"));
                    Thread.sleep(30000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            */

        } catch (URISyntaxException e) {
            e.printStackTrace();
        } finally {
        }

    }

    /**
     * Create a json representation.
     *
     * @param message
     * @return
     */
    private String getMessage(String message) {
        return Json.createObjectBuilder()
                .add("user", "bot")
                .add("message", message)
                .build()
                .toString();
    }

    // print responses
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
