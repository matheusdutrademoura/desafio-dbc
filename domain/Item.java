package domain;

import java.math.BigDecimal;

public class Item {
    
    private long id;
    private long quantity;
    private String price;

    public Item(long id, long quantity, String price) {
        this.id = id;
        this.quantity = quantity;
        this.price = price;
    }

    public BigDecimal totalPrice() {
        return new BigDecimal(price).multiply(new BigDecimal(quantity));
    }

    public static Item parseItem(String itemString) {

        String [] pieces = itemString.split("-");

        return new Item(
            Long.parseLong(pieces[0]),
            Long.parseLong(pieces[1]),
            pieces[2]
        );
    }
}