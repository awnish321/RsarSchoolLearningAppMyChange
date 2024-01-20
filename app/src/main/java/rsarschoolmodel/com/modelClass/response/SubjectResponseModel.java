package rsarschoolmodel.com.modelClass.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SubjectResponseModel {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("classId")
    @Expose
    private String classId;
    @SerializedName("classTitle")
    @Expose
    private String classTitle;
    @SerializedName("schoolUI")
    @Expose
    private String schoolUI;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("subjectData")
    @Expose
    private List<SubjectDatum> subjectData;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

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

    public List<SubjectDatum> getSubjectData() {
        return subjectData;
    }

    public void setSubjectData(List<SubjectDatum> subjectData) {
        this.subjectData = subjectData;
    }

    public static class SubjectDatum {

        @SerializedName("subjectId")
        @Expose
        private String subjectId;
        @SerializedName("subjectTitle")
        @Expose
        private String subjectTitle;
        @SerializedName("dbName")
        @Expose
        private String dbName;

        public String getSubjectId() {
            return subjectId;
        }

        public void setSubjectId(String subjectId) {
            this.subjectId = subjectId;
        }

        public String getSubjectTitle() {
            return subjectTitle;
        }

        public void setSubjectTitle(String subjectTitle) {
            this.subjectTitle = subjectTitle;
        }

        public String getDbName() {
            return dbName;
        }

        public void setDbName(String dbName) {
            this.dbName = dbName;
        }

    }

}