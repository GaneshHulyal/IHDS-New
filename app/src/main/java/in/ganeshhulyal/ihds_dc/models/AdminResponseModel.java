package in.ganeshhulyal.ihds_dc.models;

import com.google.gson.annotations.SerializedName;

public class AdminResponseModel {

    @SerializedName("totalImages")
    public String totalImages;

    @SerializedName("totalUsers")
    public String totalUsers;

    @SerializedName("totalHumanCentric")
    public String totalHumanCentric;

    @SerializedName("totalNonHumanCentric")
    public String totalNonHumanCentric;

    @SerializedName("totalTextCentric")
    public String totalTextCentric;




    public String getTotalTextCentric() {
        return totalTextCentric;
    }

    public void setTotalTextCentric(String totalTextCentric) {
        this.totalTextCentric = totalTextCentric;
    }

    public String getTotalImages() {
        return totalImages;
    }

    public void setTotalImages(String totalImages) {
        this.totalImages = totalImages;
    }

    public String getTotalUsers() {
        return totalUsers;
    }

    public void setTotalUsers(String totalUsers) {
        this.totalUsers = totalUsers;
    }

    public String getTotalHumanCentric() {
        return totalHumanCentric;
    }

    public void setTotalHumanCentric(String totalHumanCentric) {
        this.totalHumanCentric = totalHumanCentric;
    }

    public String getTotalNonHumanCentric() {
        return totalNonHumanCentric;
    }

    public void setTotalNonHumanCentric(String totalNonHumanCentric) {
        this.totalNonHumanCentric = totalNonHumanCentric;
    }


}
