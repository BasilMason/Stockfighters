package stockfighters;

import http.HttpResponse;
import http.HttpUtils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.OptionalInt;

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
        String account = "SOB40081701";
        SFVenues venue = SFVenues.OTLEX;
        SFStocks stock = SFStocks.IXSU;
        OptionalInt price = null;
        int qty = 50;
        SFOrderDirection direction = SFOrderDirection.buy;
        SFOrderType orderType = SFOrderType.MARKET;

        int purchased = 0;
        int totQty = 70000;

        while (purchased < totQty) {
            System.out.println(purchased);
            testPlaceOrder(new SFOrder(account, venue, stock, price, qty, direction, orderType));
            purchased += qty;
            try {
                Thread.sleep(500);                 //1000 milliseconds is one second.
            } catch(InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }



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
