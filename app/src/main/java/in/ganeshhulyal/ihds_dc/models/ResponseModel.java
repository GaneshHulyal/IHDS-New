package in.ganeshhulyal.ihds_dc.models;

import com.google.gson.annotations.SerializedName;

public class ResponseModel {

    @SerializedName("fullName")
    public String fullName;
    @SerializedName("usn")
    public String usn;
    @SerializedName("email")
    public String email;
    @SerializedName("mobileNumber")
    public String mobileNumber;
    @SerializedName("isAuthenticated")
    public boolean isAuthenticated;
    @SerializedName("isVerified")
    public boolean isVerified;
    @SerializedName("isMobileExist")
    public boolean isMobileExist;
    @SerializedName("isEmailExist")
    public boolean isEmailExist;
    @SerializedName("isAgreementUploaded")
    public boolean isAgreementUploaded;

    public boolean isAgreementUploaded() {
        return isAgreementUploaded;
    }

    public void setAgreementUploaded(boolean agreementUploaded) {
        isAgreementUploaded = agreementUploaded;
    }


    public boolean isMobileExist() {
        return isMobileExist;
    }

    public void setMobileExist(boolean mobileExist) {
        isMobileExist = mobileExist;
    }

    public boolean isEmailExist() {
        return isEmailExist;
    }

    public void setEmailExist(boolean emailExist) {
        isEmailExist = emailExist;
    }
    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }

    public String getFullName() {
        return fullName;
    }

    public String getUsn() {
        return usn;
    }

    public String getEmail() {
        return email;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public boolean isAuthenticated() {
        return isAuthenticated;
    }

}
