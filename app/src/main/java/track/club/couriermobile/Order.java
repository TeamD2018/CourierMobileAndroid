package track.club.couriermobile;

import com.google.gson.annotations.SerializedName;

public class Order {
    @SerializedName("source")
    private Location source;
    @SerializedName("destination")
    private Location destination;
    @SerializedName("id")
    private String id;
    @SerializedName("courier_id")
    private String courierID;
    @SerializedName("order_number")
    private Integer orderNumber;

    public Order(String id, String courierID) {
        this.id = id;
        this.courierID = courierID;
    }

    public Order(String courierID, Location source, Location destination, Integer orderNumber) {
        this.source = source;
        this.destination = destination;
        this.orderNumber = orderNumber;
        this.courierID = courierID;
    }

    public Location getSource() {
        return source;
    }

    public void setSource(Location source) {
        this.source = source;
    }

    public Location getDestination() {
        return destination;
    }

    public void setDestination(Location destination) {
        this.destination = destination;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getCourierID() {
        return courierID;
    }

    public void setCourierID(String courierID) {
        this.courierID = courierID;
    }
}
