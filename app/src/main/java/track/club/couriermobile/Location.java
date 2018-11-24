package track.club.couriermobile;

public class Location {
    private GPSPoint point;
    private String address;

    public Location(GPSPoint point) {
        this.point = point;
    }

    public Location(String address) {
        this.address = address;
    }

    public GPSPoint getPoint() {
        return point;
    }

    public void setPoint(GPSPoint point) {
        this.point = point;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
