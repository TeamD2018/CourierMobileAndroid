package track.club.couriermobile;

import android.support.v4.util.Consumer;
import android.util.Log;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.*;

public class CourierAPI {
    private final OkHttpClient client;
    private final Gson gson = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create();
    private final RESTAcceptor<Courier> restAcceptor;
    private static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");


    public CourierAPI(OkHttpClient client) {
        this.client = client;
        this.restAcceptor = new RESTAcceptor<>(gson);
    }

    public void Create(Courier courier, final Consumer<Courier> consumer) {
        RequestBody body = RequestBody.create(JSON, gson.toJson(courier));
        Request request = new Request.Builder()
                .post(body)
                .url("https://track-delivery.club/api/v1/couriers")
                .build();
        client.newCall(request).enqueue(restAcceptor.onResponse(consumer, Courier.class));
    }

    public void Update(Courier courier, final Consumer<Courier> consumer) {
        Gson gson = new Gson();
        String json = gson.toJson(courier);
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .put(body)
                .url("https://track-delivery.club/api/v1/couriers/" + courier.getId())
                .build();
        Log.i("update req", json);
        client.newCall(request).enqueue(restAcceptor.onResponse(consumer, Courier.class));
    }

    public void Delete(Courier courier) {
        HttpUrl respURL = new HttpUrl.Builder()
                .scheme("https")
                .host("track-delivery.club")
                .addPathSegment("api/v1/couriers/")
                .addPathSegment(courier.getId())
                .build();

        Request request = new Request.Builder()
                .delete()
                .url(respURL)
                .build();
        client.newCall(request).enqueue(restAcceptor.onEmptyResponse());
    }


}
