    package rsarschoolmodel.com.modelClass.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OtpVerifyRequestModel {

    @SerializedName("userEmail")
    @Expose
    private String userEmail;
    @SerializedName("otp")
    @Expose
    private String otp;
    @SerializedName("action")
    @Expose
    private String action;

    public OtpVerifyRequestModel(String userEmail, String otp, String action) {
        super();
        this.userEmail = userEmail;
        this.otp = otp;
        this.action = action;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

}