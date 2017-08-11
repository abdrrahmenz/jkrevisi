package id.or.qodr.jadwalkajian.api;

import java.util.Map;

import id.or.qodr.jadwalkajian.model.JadwalResponse;
import id.or.qodr.jadwalkajian.model.UserResponse;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by adul on 01/08/17.
 */

public interface RestApi {

    // Fetch Data All Jadwal
    @GET("apiv2/jadwal")
    Call<ResponseBody> getAllDataFromServer();

    // Get Login Admin
    @GET("apiv2/login/{username}/{password}")
    Call<ResponseBody> getLoginAdmin(@Path("username") String username, @Path("password") String password);

    // Update DataLogin Admin
    @FormUrlEncoded
    @POST("apiv2/login/update")
    Call<ResponseBody> updateUserAdmin(
            @FieldMap Map<String, String> map
    );

    // Show Data Kajian Today
    @GET("apiv2/jadwal/{today}")
    Call<JadwalResponse> getKajianToday(@Path("today") String today);

    // Show Data Kajian Week
    @GET("apiv2/jadwal/{from}/{to}")
    Call<ResponseBody> getKajianWeek(@Path("from") String from, @Path("to") String to);

    // Input New Data Kajian
    @FormUrlEncoded
    @POST("apiv2/jadwal/insert")
    Call<ResponseBody> submitNewKajian(
            @FieldMap Map<String, String> map
    );
}
