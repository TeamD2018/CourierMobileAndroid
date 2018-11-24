package track.club.couriermobile;

public class Courier {
    private String id;
    private String name;
    private String phone;
    private Location location;

    public Courier(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }

    public Courier(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


}
