package in.ganeshhulyal.aidatalab.models;

import com.google.gson.annotations.SerializedName;

public class ModelApproveUser {

    @SerializedName("fullName")
    public String fullName;

    @SerializedName("email")
    public String email;


    @SerializedName("agreementStatus")
    public String agreementStatus;

    public String getAgreementStatus() {
        return agreementStatus;
    }

    public void setAgreementStatus(String agreementStatus) {
        this.agreementStatus = agreementStatus;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
