package rsarschoolmodel.com.modelClass.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserLoginRequestModel {
        @SerializedName("activationCode")
        @Expose
        private String activationCode;
        @SerializedName("userName")
        @Expose
        private String userName;
        @SerializedName("userEmail")
        @Expose
        private String userEmail;
        @SerializedName("userMobile")
        @Expose
        private String userMobile;
        @SerializedName("userType")
        @Expose
        private String userType;
        @SerializedName("classId")
        @Expose
        private String classId;
        @SerializedName("appName")
        @Expose
        private String appName;
        @SerializedName("mDeviceId")
        @Expose
        private String mDeviceId;
        @SerializedName("mId")
        @Expose
        private String mId;
        @SerializedName("mProduct")
        @Expose
        private String mProduct;
        @SerializedName("mBrand")
        @Expose
        private String mBrand;
        @SerializedName("mManufacture")
        @Expose
        private String mManufacture;
        @SerializedName("mModel")
        @Expose
        private String mModel;
        @SerializedName("action")
        @Expose
        private String action;


        public UserLoginRequestModel(String activationCode, String userName, String userEmail, String userMobile, String userType, String classId, String appName, String mDeviceId, String mId, String mProduct, String mBrand, String mManufacture, String mModel, String action) {
            super();
            this.activationCode = activationCode;
            this.userName = userName;
            this.userEmail = userEmail;
            this.userMobile = userMobile;
            this.userType = userType;
            this.classId = classId;
            this.appName = appName;
            this.mDeviceId = mDeviceId;
            this.mId = mId;
            this.mProduct = mProduct;
            this.mBrand = mBrand;
            this.mManufacture = mManufacture;
            this.mModel = mModel;
            this.action = action;
        }

        public String getActivationCode() {
            return activationCode;
        }

        public void setActivationCode(String activationCode) {
            this.activationCode = activationCode;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getUserEmail() {
            return userEmail;
        }

        public void setUserEmail(String userEmail) {
            this.userEmail = userEmail;
        }

        public String getUserMobile() {
            return userMobile;
        }

        public void setUserMobile(String userMobile) {
            this.userMobile = userMobile;
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

        public String getAppName() {
            return appName;
        }

        public void setAppName(String appName) {
            this.appName = appName;
        }

        public String getmDeviceId() {
            return mDeviceId;
        }

        public void setmDeviceId(String mDeviceId) {
            this.mDeviceId = mDeviceId;
        }

        public String getmId() {
            return mId;
        }

        public void setmId(String mId) {
            this.mId = mId;
        }

        public String getmProduct() {
            return mProduct;
        }

        public void setmProduct(String mProduct) {
            this.mProduct = mProduct;
        }

        public String getmBrand() {
            return mBrand;
        }

        public void setmBrand(String mBrand) {
            this.mBrand = mBrand;
        }

        public String getmManufacture() {
            return mManufacture;
        }

        public void setmManufacture(String mManufacture) {
            this.mManufacture = mManufacture;
        }

        public String getmModel() {
            return mModel;
        }

        public void setmModel(String mModel) {
            this.mModel = mModel;
        }

        public String getAction() {
            return action;
        }

        public void setAction(String action) {
            this.action = action;
        }

    }