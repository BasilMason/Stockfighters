package stockfighters;

import java.util.HashMap;
import java.util.Map;
import java.util.OptionalInt;

/**
 * Created by Basil on 14/12/2015.
 */
public class SFOrder {

    private Map<String, Object> params;
    private SFVenues venue;
    private SFStocks stock;

    public SFOrder(String account, SFVenues venue, SFStocks stock, OptionalInt price, int qty, SFOrderDirection direction, SFOrderType orderType) {

        this.venue = venue;
        this.stock = stock;

        params = new HashMap<>();

        params.put("account", account);
        params.put("venue", venue.name());
        params.put("stock", stock.name());
        if (price != null) params.put("price", price.getAsInt());
        params.put("qty", qty);
        params.put("direction", direction.name());
        params.put("orderType", orderType.name());

    }

    public Map<String, Object> getOrder() {
        return params;
    }

    public SFVenues getVenue() {
        return venue;
    }

    public SFStocks getStock() {
        return stock;
    }

}
