package in.ganeshhulyal.aidatalab.Retrofit.Interfaces;

import java.util.List;

import in.ganeshhulyal.aidatalab.activities.MetaDataNonHumanCentric;
import in.ganeshhulyal.aidatalab.models.MetaDataModelHumanCentric;
import in.ganeshhulyal.aidatalab.models.MetaDataResponseModel;
import in.ganeshhulyal.aidatalab.utils.FileUtil;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiService {
    String BASE_URL = "http://210.212.192.31/";

    @Multipart
    @POST("upload.php")
    Call<ResponseBody> uploadMultiple(
            @Part("imageType") RequestBody isHumanCentric,
            @Part("description") RequestBody description,
            @Part("size") RequestBody size,
            @Part("Category") RequestBody category,
            @Part("SubCat") RequestBody subCat,
            @Part("userName") RequestBody userName,
            @Part("dataType") RequestBody dataType,
            @Part("deviceUsed") RequestBody deviceUsed,
            @Part("resolution") RequestBody resolution,
            @Part("orientation") RequestBody orientation,
            @Part("dslrOrMobile") RequestBody dslrOrMobile,
            @Part("dataMajorCategory") RequestBody dataMajorCategory,
            @Part("location") RequestBody location,
            @Part("subLocation") RequestBody subLocation,
            @Part("timing") RequestBody timing,
            @Part("lighting") RequestBody lighting,
            @Part("humanPresent") RequestBody humanPresent,
            @Part("selfie") RequestBody selfie,
            @Part("type") RequestBody type,
            @Part("children")RequestBody children,
            @Part("props") RequestBody props,
            @Part("consentObtained") RequestBody consentObtained,
            @Part List<MultipartBody.Part> files);

}