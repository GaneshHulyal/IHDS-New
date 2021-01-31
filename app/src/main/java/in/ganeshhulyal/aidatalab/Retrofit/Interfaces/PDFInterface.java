package in.ganeshhulyal.aidatalab.Retrofit.Interfaces;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface PDFInterface {

    String IMAGEURL = "http://210.212.192.31/";

    @Multipart
    @POST("uploadPdf.php")
    Call<String> uploadImage(
            @Part MultipartBody.Part file, @Part("filename") RequestBody name,
            @Part("Category") RequestBody category,
            @Part("SubCat") RequestBody subCat,
            @Part("userName") RequestBody userName
    );
}