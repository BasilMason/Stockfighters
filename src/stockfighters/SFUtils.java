package stockfighters;

/**
 * Created by Basil on 14/12/2015.
 */
public class SFUtils {

    public static String urlHeartbeat() {
        return "https://api.stockfighter.io/ob/api/heartbeat";
    }

    public static String urlVenues(SFVenues venue) {
        return "https://api.stockfighter.io/ob/api/venues/" + venue.name() + "/heartbeat";
    }

    public static String urlStocks(SFVenues venue) {
        return "https://api.stockfighter.io/ob/api/venues/" + venue.name() + "/stocks";
    }

    public static String urlOrderbook(SFVenues venue, SFStocks stock) {
        return "https://api.stockfighter.io/ob/api/venues/" + venue.name() + "/stocks/" + stock.name();
    }

    public static String urlOrder(SFVenues venue, SFStocks stock) {
        return "https://api.stockfighter.io/ob/api/venues/" + venue.name() + "/stocks/" + stock.name() + "/orders";
    }

}
