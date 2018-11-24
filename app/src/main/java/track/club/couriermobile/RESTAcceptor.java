package track.club.couriermobile;

import android.support.v4.util.Consumer;
import android.util.Log;
import com.google.gson.Gson;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.io.IOException;


public class RESTAcceptor<T> {
    private Gson gson;

    public RESTAcceptor(Gson gson) {
        this.gson = gson;
    }

    public Callback onResponse(Consumer<T> consumer, Class<T> clazz) {
        return new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
                Log.e("err", e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String body = response.body().string();
                Log.d("body", body + " of " + clazz.toString());
                if (response.isSuccessful()) {
                    T got = gson.fromJson(body, clazz);
                    consumer.accept(got);
                }
                call.cancel();
                response.close();
            }
        };
    }

    public Callback onEmptyResponse() {
        return new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
                Log.e("err", e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    response.close();
                    return;
                }
                ResponseBody body = response.body();
                if (body == null) {
                    response.close();
                    return;
                }
                String message = body.string();
                Log.e("got error", message);
                response.close();
            }
        };
    }
}
