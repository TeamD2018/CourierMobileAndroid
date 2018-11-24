package track.club.couriermobile;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import okhttp3.OkHttpClient;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private static final String ORDER_ID = "ORDER_ID";
    private LocationService locationService;
    private CourierAPI courierAPI;
    private OrderAPI orderAPI;
    private String courierID;
    private String orderID;
    private final String COURIER_ID = "COURIER_ID";
    private final Random OrderNumberGenerator = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(this, ForegroundStub.class);
        startService(intent);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    1);

        } else {
            initialize();
        }
        if (savedInstanceState != null) {
            courierID = savedInstanceState.getString(COURIER_ID, null);
            orderID = savedInstanceState.getString(ORDER_ID, null);
        }
        if (courierID == null) {
            SharedPreferences prefs = getPreferences(Context.MODE_PRIVATE);
            courierID = prefs.getString(COURIER_ID, null);
            orderID = prefs.getString(ORDER_ID, null);
        }

        Button courierCreateButton = findViewById(R.id.create_courier_button);
        courierCreateButton.setOnClickListener(v -> {
            if (courierID != null) {
                courierAPI.Delete(new Courier(courierID));
            }
            EditText name = findViewById(R.id.name_input);
            EditText phone = findViewById(R.id.phone_input);
            Courier courier = new Courier(name.getText().toString(), phone.getText().toString());
            courierAPI.Create(courier, created -> runOnUiThread(() -> courierID = created.getId()));
        });

        Button courierDeleteButton = findViewById(R.id.delete_courier);
        courierDeleteButton.setOnClickListener(v -> {
            if (courierID != null) {
                courierAPI.Delete(new Courier(courierID));
                courierID = null;
            }
        });

        Button orderCreateButton = findViewById(R.id.create_order_button);
        orderCreateButton.setOnClickListener(v -> {
            if (courierID == null) {
                return;
            }
            if (orderID != null) {
                orderAPI.Delete(new Order(orderID, courierID));
                orderID = null;
            }
            EditText from = findViewById(R.id.source_input);
            EditText dest = findViewById(R.id.destination_input);
            Integer orderNumber = OrderNumberGenerator.nextInt(10000);
            Order order = new Order(courierID, new Location(from.getText().toString()), new Location(dest.getText().toString()), orderNumber);
            orderAPI.Create(order, created -> runOnUiThread(() -> orderID = created.getId()));
        });
        Button orderDeleteButton = findViewById(R.id.delete_order);
        orderDeleteButton.setOnClickListener(v -> {
            if (courierID == null) {
                return;
            }
            if (orderID != null) {
                orderAPI.Delete(new Order(orderID, courierID));
                orderID = null;
            }
        });
    }

    private void geoTracker(GPSPoint gpsPoint) {
        Log.d("gps", String.valueOf(gpsPoint.getLatitude()) + " " + String.valueOf(gpsPoint.getLongitude()));
        if (courierID != null) {
            Courier courier = new Courier(courierID);
            courier.setLocation(new Location(gpsPoint));
            courierAPI.Update(courier, updated -> Log.d("got updated", updated.getLocation().toString()));
        }
    }

    private void initialize() {
        this.locationService = new LocationService(this);
        this.locationService.onChange(this::geoTracker);
        OkHttpClient client = new OkHttpClient();
        this.courierAPI = new CourierAPI(client);
        this.orderAPI = new OrderAPI(client);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initialize();
                } else {
                    throw new RuntimeException("pizdec");
                }

            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        SharedPreferences prefs = getPreferences(Context.MODE_PRIVATE);
        if (courierID != null) {
            outState.putString(COURIER_ID, courierID);
            prefs.edit().putString(COURIER_ID, courierID).apply();
        }
        if (orderID != null) {
            outState.putString(ORDER_ID, orderID);
            prefs.edit().putString(ORDER_ID, orderID).apply();
        }
        super.onSaveInstanceState(outState);
    }

}
