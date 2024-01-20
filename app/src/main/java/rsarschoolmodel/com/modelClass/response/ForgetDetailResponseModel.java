package rsarschoolmodel.com.modelClass.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class ForgetDetailResponseModel
 {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("forgotDetails")
    @Expose
    private ForgotDetails forgotDetails;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ForgotDetails getForgotDetails() {
        return forgotDetails;
    }

    public void setForgotDetails(ForgotDetails forgotDetails) {
        this.forgotDetails = forgotDetails;
    }
     public static class ForgotDetails {

         @SerializedName("name")
         @Expose
         private String name;
         @SerializedName("email")
         @Expose
         private String email;
         @SerializedName("mobile")
         @Expose
         private String mobile;

         public String getName() {
             return name;
         }

         public void setName(String name) {
             this.name = name;
         }

         public String getEmail() {
             return email;
         }

         public void setEmail(String email) {
             this.email = email;
         }

         public String getMobile() {
             return mobile;
         }

         public void setMobile(String mobile) {
             this.mobile = mobile;
         }

     }

}