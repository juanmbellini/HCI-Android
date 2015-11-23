package hci.tiendapp.backend;

import com.google.gson.annotations.Expose;

/**
 * Created by JuanMarcos on 23/11/15.
 */
public class Order {

    @Expose
    int id;
    @Expose
    Address address;
    @Expose
    CreditCard creditCard;
    @Expose
    String status;
    @Expose
    String receivedDate;
    @Expose
    String processedDate;
    @Expose
    String shippedDate;
    @Expose
    int latitude;
    @Expose
    int longitude;
    @Expose
    OrderLine[] items;

    public Order(int id, Address address, CreditCard creditCard, String status,
                 String receivedDate, String processedDate, String shippedDate,
                 int latitude, int longitude, OrderLine[] items) {
        this.id = id;
        this.address = address;
        this.creditCard = creditCard;
        this.status = status;
        this.receivedDate = receivedDate;
        this.processedDate = processedDate;
        this.shippedDate = shippedDate;
        this.latitude = latitude;
        this.longitude = longitude;
        this.items = items;
    }


    public int getId() {
        return id;
    }

    public Address getAddress() {
        return address;
    }

    public CreditCard getCreditCard() {
        return creditCard;
    }

    public String getStatus() {
        return status;
    }

    public String getReceivedDate() {
        return receivedDate;
    }

    public String getProcessedDate() {
        return processedDate;
    }

    public String getShippedDate() {
        return shippedDate;
    }

    public int getLatitude() {
        return latitude;
    }

    public int getLongitude() {
        return longitude;
    }

    public OrderLine[] getItems() {
        return items;
    }
}
