package rsarschoolmodel.com.modelClass.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OtpVerifyResponseModel {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("userData")
    @Expose
    private List<UserDatum> userData = null;

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

    public List<UserDatum> getUserData() {
        return userData;
    }

    public void setUserData(List<UserDatum> userData) {
        this.userData = userData;
    }

    public static class UserDatum {

        @SerializedName("email")
        @Expose
        private String email;
        @SerializedName("mobile")
        @Expose
        private String mobile;
        @SerializedName("deviceId")
        @Expose
        private String deviceId;
        @SerializedName("userType")
        @Expose
        private String userType;
        @SerializedName("schoolName")
        @Expose
        private String schoolName;
        @SerializedName("schoolUI")
        @Expose
        private String schoolUI;
        @SerializedName("schoolFolderName")
        @Expose
        private String schoolFolderName;
        @SerializedName("bgCode")
        @Expose
        private String bgCode;
        @SerializedName("topBgCode")
        @Expose
        private String topBgCode;
        @SerializedName("buttonBgColor")
        @Expose
        private String buttonBgColor;
        @SerializedName("schoolNameColor")
        @Expose
        private String schoolNameColor;
        @SerializedName("folderName")
        @Expose
        private String folderName;
        @SerializedName("schoolFolderPath")
        @Expose
        private String schoolFolderPath;

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

        public String getDeviceId() {
            return deviceId;
        }

        public void setDeviceId(String deviceId) {
            this.deviceId = deviceId;
        }

        public String getUserType() {
            return userType;
        }

        public void setUserType(String userType) {
            this.userType = userType;
        }

        public String getSchoolName() {
            return schoolName;
        }

        public void setSchoolName(String schoolName) {
            this.schoolName = schoolName;
        }

        public String getSchoolUI() {
            return schoolUI;
        }

        public void setSchoolUI(String schoolUI) {
            this.schoolUI = schoolUI;
        }

        public String getSchoolFolderName() {
            return schoolFolderName;
        }

        public void setSchoolFolderName(String schoolFolderName) {
            this.schoolFolderName = schoolFolderName;
        }

        public String getBgCode() {
            return bgCode;
        }

        public void setBgCode(String bgCode) {
            this.bgCode = bgCode;
        }

        public String getTopBgCode() {
            return topBgCode;
        }

        public void setTopBgCode(String topBgCode) {
            this.topBgCode = topBgCode;
        }

        public String getButtonBgColor() {
            return buttonBgColor;
        }

        public void setButtonBgColor(String buttonBgColor) {
            this.buttonBgColor = buttonBgColor;
        }

        public String getSchoolNameColor() {
            return schoolNameColor;
        }

        public void setSchoolNameColor(String schoolNameColor) {
            this.schoolNameColor = schoolNameColor;
        }

        public String getFolderName() {
            return folderName;
        }

        public void setFolderName(String folderName) {
            this.folderName = folderName;
        }

        public String getSchoolFolderPath() {
            return schoolFolderPath;
        }

        public void setSchoolFolderPath(String schoolFolderPath) {
            this.schoolFolderPath = schoolFolderPath;
        }

    }

}
