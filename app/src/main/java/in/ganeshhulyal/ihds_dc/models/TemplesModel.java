package in.ganeshhulyal.ihds_dc.models;

import com.google.gson.annotations.SerializedName;

public class TemplesModel {

    @SerializedName("id")
    String id;

    @SerializedName("templeName")
    String templeName;

    @SerializedName("isApproved")
    String isApproved;

    @SerializedName("responseFromServer")
    String responseFromServer;


    public String getResponseFromServer() {
        return responseFromServer;
    }

    public void setResponseFromServer(String responseFromServer) {
        this.responseFromServer = responseFromServer;
    }

    public String getIsApproved() {
        return isApproved;
    }

    public void setIsApproved(String isApproved) {
        this.isApproved = isApproved;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTempleName() {
        return templeName;
    }

    public void setTempleName(String templeName) {
        this.templeName = templeName;
    }
}
