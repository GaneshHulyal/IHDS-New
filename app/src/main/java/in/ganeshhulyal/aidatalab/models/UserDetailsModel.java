package in.ganeshhulyal.aidatalab.models;

import com.google.gson.annotations.SerializedName;


public class UserDetailsModel {

    @SerializedName("userFullName")
    public String userFullName;

    @SerializedName("userMobileNumber")
    public String userMobileNumber;

    @SerializedName("userEmail")
    public String userEmail;

    @SerializedName("userPeopleCentric")
    public String userPeopleCentric;

    @SerializedName("userNonPeopleCentric")
    public String userNonPeopleCentric;

    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }

    public String getUserMobileNumber() {
        return userMobileNumber;
    }

    public void setUserMobileNumber(String userMobileNumber) {
        this.userMobileNumber = userMobileNumber;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPeopleCentric() {
        return userPeopleCentric;
    }

    public void setUserPeopleCentric(String userPeopleCentric) {
        this.userPeopleCentric = userPeopleCentric;
    }

    public String getUserNonPeopleCentric() {
        return userNonPeopleCentric;
    }

    public void setUserNonPeopleCentric(String userNonPeopleCentric) {
        this.userNonPeopleCentric = userNonPeopleCentric;
    }
}
