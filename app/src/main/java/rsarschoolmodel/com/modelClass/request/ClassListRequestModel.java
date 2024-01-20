    package rsarschoolmodel.com.modelClass.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

    public class ClassListRequestModel {

        @SerializedName("schoolUI")
        @Expose
        private String schoolUI;
        @SerializedName("userType")
        @Expose
        private String userType;
        @SerializedName("action")
        @Expose
        private String action;

        public ClassListRequestModel(String schoolUI, String userType, String action) {
            this.schoolUI = schoolUI;
            this.userType = userType;
            this.action = action;
        }

        public String getSchoolUI() {
            return schoolUI;
        }

        public void setSchoolUI(String schoolUI) {
            this.schoolUI = schoolUI;
        }

        public String getUserType() {
            return userType;
        }

        public void setUserType(String userType) {
            this.userType = userType;
        }

        public String getAction() {
            return action;
        }

        public void setAction(String action) {
            this.action = action;
        }
    }