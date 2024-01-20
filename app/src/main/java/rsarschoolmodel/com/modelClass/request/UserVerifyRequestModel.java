package rsarschoolmodel.com.modelClass.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserVerifyRequestModel {
    @SerializedName("userEmail")
    @Expose
    private String userEmail;
    @SerializedName("action")
    @Expose
    private String action;

    public UserVerifyRequestModel(String userEmail, String action) {
        super();
        this.userEmail = userEmail;
        this.action = action;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getAction() {
        return action;
    }
    public void setAction(String action) {
        this.action = action;
    }

}
