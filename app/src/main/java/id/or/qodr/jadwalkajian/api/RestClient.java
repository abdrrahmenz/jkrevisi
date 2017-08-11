package id.or.qodr.jadwalkajian.api;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by adul on 01/08/17.
 */

public class RestClient {
    public static final String BASE_URL = "https://adul.000webhostapp.com/";
    private static Retrofit.Builder retrofit = null;

    public static Retrofit.Builder getClient(OkHttpClient.Builder okHttpClient) {
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient.build());
        }
        return retrofit;
    }
}
