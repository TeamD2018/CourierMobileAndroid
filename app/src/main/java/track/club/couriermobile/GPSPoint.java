package track.club.couriermobile;

import com.google.gson.annotations.SerializedName;

public class GPSPoint {
    @SerializedName("lat")
    private double latitude;
    @SerializedName("lon")
    private double longitude;

    public GPSPoint(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
