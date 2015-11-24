package hci.tiendapp.backend;

import com.google.gson.annotations.Expose;

/**
 * Created by JuanMarcos on 23/11/15.
 */
public class OrderLine {

    @Expose
    int id;
    @Expose
    OrderLineProduct product;
    @Expose
    int quantity;
    @Expose
    double price;

    public OrderLine(int id, OrderLineProduct product, int quantity, double price) {
        this.id = id;
        this.product = product;
        this.quantity = quantity;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public OrderLineProduct getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }


    public class OrderLineProduct {

        @Expose
        int id;
        @Expose
        String name;
        @Expose
        String imageUrl;

        public OrderLineProduct(int id, String name, String imageUrl) {
            this.id = id;
            this.name = name;
            this.imageUrl = imageUrl;
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }
    }
}
