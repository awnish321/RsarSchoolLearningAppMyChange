package rsarschoolmodel.com.modelClass.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ClassListResponseModel
{

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("schoolUI")
    @Expose
    private String schoolUI;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("classData")
    @Expose
    private List<ClassDatum> classData = null;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getSchoolUI() {
        return schoolUI;
    }

    public void setSchoolUI(String schoolUI) {
        this.schoolUI = schoolUI;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<ClassDatum> getClassData() {
        return classData;
    }

    public void setClassData(List<ClassDatum> classData) {
        this.classData = classData;
    }

    public static class ClassDatum {

        @SerializedName("classId")
        @Expose
        private String classId;
        @SerializedName("classTitle")
        @Expose
        private String classTitle;
        @SerializedName("dbName")
        @Expose
        private String dbName;

        public String getClassId() {
            return classId;
        }

        public void setClassId(String classId) {
            this.classId = classId;
        }

        public String getClassTitle() {
            return classTitle;
        }

        public void setClassTitle(String classTitle) {
            this.classTitle = classTitle;
        }

        public String getDbName() {
            return dbName;
        }

        public void setDbName(String dbName) {
            this.dbName = dbName;
        }

    }


}
