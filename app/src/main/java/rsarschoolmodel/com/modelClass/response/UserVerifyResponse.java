package rsarschoolmodel.com.modelClass.response;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserVerifyResponse {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("otpStatus")
    @Expose
    private String otpStatus;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("userData")
    @Expose
    private UserData userData;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getOtpStatus() {
        return otpStatus;
    }

    public void setOtpStatus(String otpStatus) {
        this.otpStatus = otpStatus;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public UserData getUserData() {
        return userData;
    }

    public void setUserData(UserData userData) {
        this.userData = userData;
    }

    public static class UserData {

        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("email")
        @Expose
        private String email;
        @SerializedName("mobile")
        @Expose
        private String mobile;
        @SerializedName("userType")
        @Expose
        private String userType;
        @SerializedName("classId")
        @Expose
        private String classId;
        @SerializedName("className")
        @Expose
        private String className;
        @SerializedName("SchoolUI")
        @Expose
        private String schoolUI;
        @SerializedName("userOtpStatus")
        @Expose
        private String userOtpStatus;

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

        public String getUserType() {
            return userType;
        }

        public void setUserType(String userType) {
            this.userType = userType;
        }

        public String getClassId() {
            return classId;
        }

        public void setClassId(String classId) {
            this.classId = classId;
        }

        public String getClassName() {
            return className;
        }

        public void setClassName(String className) {
            this.className = className;
        }

        public String getSchoolUI() {
            return schoolUI;
        }

        public void setSchoolUI(String schoolUI) {
            this.schoolUI = schoolUI;
        }

        public String getUserOtpStatus() {
            return userOtpStatus;
        }

        public void setUserOtpStatus(String userOtpStatus) {
            this.userOtpStatus = userOtpStatus;
        }

    }

}