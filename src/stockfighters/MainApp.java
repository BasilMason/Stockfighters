package stockfighters;

import http.HttpResponse;
import http.HttpUtils;
import websocket.WSClient;

import javax.json.Json;
import javax.json.JsonObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by Basil on 14/12/2015.
 */
public class MainApp {

    public static void main(String[] args) {
        new MainApp().launch();
    }

    private void launch() {

        // 1. Heartbeat]
        //testHeartbeat();

        // 2. Venues
        //testVenue(SFVenues.TESTEX);

        // 3. Stocks
        //testStocks(SFVenues.TESTEX);

        // 4. Order book
        //testOrderbook(SFVenues.TESTEX, SFStocks.FOOBAR);

        // 5. Order
        /*
        String account = "FAW24796800";
        SFVenues venue = SFVenues.DRLBEX;
        SFStocks stock = SFStocks.IHOM;
        OptionalInt price = null;
        int qty = 50;
        SFOrderDirection direction = SFOrderDirection.buy;
        SFOrderType orderType = SFOrderType.MARKET;

        int purchased = 0;
        int totQty = 100000;

        while (purchased < totQty) {
            System.out.println(purchased);
            testPlaceOrder(new SFOrder(account, venue, stock, price, qty, direction, orderType));
            purchased += qty;
            try {
                Thread.sleep(3000);                 //1000 milliseconds is one second.
            } catch(InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }

*/

        // 6. Quotes
        testQuotes();

    }

    // api call tests
    private void testHeartbeat() {
        try {
            System.out.println("Checking heartbeat");
            HttpResponse heartbeat = HttpUtils.sendGet(SFUtils.urlHeartbeat());

            printErrorCode(heartbeat.getErrorCode());
            printResponse(heartbeat.getResponse());
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

    private void testPlaceOrder(SFOrder order) {

        // String account, SFVenues venue, SFStocks stock, OptionalInt price, int qty, SFOrderDirection direction, SFOrderType orderType

        try {
            System.out.println("Submitting order for stock: " + order.getStock() + " on venue: " + order.getVenue());
            HttpResponse placeOrder = HttpUtils.sendPost(SFUtils.urlOrder(order.getVenue(), order.getStock()), order.getOrder());

            printErrorCode(placeOrder.getErrorCode());
            printResponse(placeOrder.getResponse());
            System.out.println();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
    }

    private void testQuotes() {

        String account = "NTC28568995";
        SFVenues venue = SFVenues.GMSTEX;

        // catch exception
        try {

            //set websocket client
            final WSClient clientEndPoint = new WSClient(new URI(SFUtils.urlQuote(account, venue)));

            // handle message
            clientEndPoint.addMessageHandler(message -> {

                // create JSON from message
                JsonObject jsonObject = Json.createReader(new StringReader(message)).readObject();
                System.out.println(jsonObject.toString());
                /*String userName = jsonObject.getString("user");
                if (!"bot".equals(userName)) {
                    clientEndPoint.sendMessage(getMessage("Hello " + userName +", How are you?"));
                    // other dirty bot logic goes here.. :)
                }*/
            });

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
