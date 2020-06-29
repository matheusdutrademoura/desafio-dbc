package domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Sale {

    private long id;
    private String seller;
    private List<Item> items;

    public Sale(long id, String seller, List<Item> items) {
        this.id = id;
        this.seller = seller;
        this.items = items;
    }

    public long getId() {return id;}

    public String getSeller() {return seller;}

    public BigDecimal totalPrice() {

        BigDecimal total = new BigDecimal(0);

        for(Item item : items)
            total = total.add(item.totalPrice());

        return total;
    }

    public static Sale parseSale(String saleString) {

        String positions[] = saleString.split("ç");

        return new Sale(
            Long.parseLong(positions[1]),
            positions[3],
            // remove square brackets
            Sale.parseItems(positions[2].substring(1, positions[2].length() -1))
        );
    }

    public static List<Item> parseItems(String listOfItemsString) {

        List<Item> items = new ArrayList<Item>();

        String itemsString[] = listOfItemsString.split(",");

        for(String itemString : itemsString)
            items.add(Item.parseItem(itemString));

        return items;
    }

}