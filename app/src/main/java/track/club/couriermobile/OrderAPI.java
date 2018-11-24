package track.club.couriermobile;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.*;


import android.support.v4.util.Consumer;

public class OrderAPI {
    private OkHttpClient client;
    private static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    private final Gson gson = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create();
    private final RESTAcceptor<Order> restAcceptor;

    public OrderAPI(OkHttpClient client) {
        this.client = client;
        restAcceptor = new RESTAcceptor<>(gson);
    }

    public void Create(Order order, Consumer<Order> consumer) {
        RequestBody body = RequestBody.create(JSON, gson.toJson(order));
        HttpUrl reqURL = new HttpUrl.Builder()
                .scheme("https")
                .host("track-delivery.club")
                .addPathSegment("api/v1/couriers/")
                .addPathSegment(order.getCourierID())
                .addPathSegment("/orders")
                .build();
        Request request = new Request.Builder()
                .post(body)
                .url(reqURL)
                .build();
        client.newCall(request).enqueue(restAcceptor.onResponse(consumer, Order.class));
    }


    public void Delete(Order order) {
        HttpUrl reqURL = new HttpUrl.Builder()
                .scheme("https")
                .host("track-delivery.club")
                .addPathSegment("api/v1/couriers/")
                .addPathSegment(order.getCourierID())
                .addPathSegment("/orders/")
                .addPathSegment(order.getId())
                .build();
        Request request = new Request.Builder()
                .delete()
                .url(reqURL)
                .build();
        client.newCall(request).enqueue(restAcceptor.onEmptyResponse());
    }
}
