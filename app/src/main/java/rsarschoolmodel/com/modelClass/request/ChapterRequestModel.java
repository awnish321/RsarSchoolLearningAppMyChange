    package rsarschoolmodel.com.modelClass.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

    public class ChapterRequestModel {

        @SerializedName("schoolUI")
        @Expose
        private String schoolUI;
        @SerializedName("classId")
        @Expose
        private String classId;
        @SerializedName("subjectId")
        @Expose
        private String subjectId;
        @SerializedName("bookId")
        @Expose
        private String bookId;
        @SerializedName("userType")
        @Expose
        private String userType;
        @SerializedName("action")
        @Expose
        private String action;

        public ChapterRequestModel(String schoolUI, String classId, String subjectId, String bookId, String userType, String action) {
            super();
            this.schoolUI = schoolUI;
            this.classId = classId;
            this.subjectId = subjectId;
            this.bookId = bookId;
            this.userType = userType;
            this.action = action;
        }

        public String getSchoolUI() {
            return schoolUI;
        }

        public void setSchoolUI(String schoolUI) {
            this.schoolUI = schoolUI;
        }

        public String getClassId() {
            return classId;
        }

        public void setClassId(String classId) {
            this.classId = classId;
        }

        public String getSubjectId() {
            return subjectId;
        }

        public void setSubjectId(String subjectId) {
            this.subjectId = subjectId;
        }

        public String getBookId() {
            return bookId;
        }

        public void setBookId(String bookId) {
            this.bookId = bookId;
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