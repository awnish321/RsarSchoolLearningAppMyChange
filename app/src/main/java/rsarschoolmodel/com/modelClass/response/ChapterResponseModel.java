package rsarschoolmodel.com.modelClass.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ChapterResponseModel {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("classId")
    @Expose
    private String classId;
    @SerializedName("className")
    @Expose
    private String className;
    @SerializedName("subjectId")
    @Expose
    private String subjectId;
    @SerializedName("subjectName")
    @Expose
    private String subjectName;
    @SerializedName("bookName")
    @Expose
    private String bookName;
    @SerializedName("bookDbName")
    @Expose
    private String bookDbName;
    @SerializedName("schoolFolderName")
    @Expose
    private String schoolFolderName;
    @SerializedName("schoolUI")
    @Expose
    private String schoolUI;
    @SerializedName("datasetName")
    @Expose
    private String datasetName;
    @SerializedName("datasetLink")
    @Expose
    private String datasetLink;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("chapterData")
    @Expose
    private List<ChapterDatum> chapterData;

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

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getBookDbName() {
        return bookDbName;
    }

    public void setBookDbName(String bookDbName) {
        this.bookDbName = bookDbName;
    }

    public String getSchoolFolderName() {
        return schoolFolderName;
    }

    public void setSchoolFolderName(String schoolFolderName) {
        this.schoolFolderName = schoolFolderName;
    }

    public String getSchoolUI() {
        return schoolUI;
    }

    public void setSchoolUI(String schoolUI) {
        this.schoolUI = schoolUI;
    }

    public String getDatasetName() {
        return datasetName;
    }

    public void setDatasetName(String datasetName) {
        this.datasetName = datasetName;
    }

    public String getDatasetLink() {
        return datasetLink;
    }

    public void setDatasetLink(String datasetLink) {
        this.datasetLink = datasetLink;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<ChapterDatum> getChapterData() {
        return chapterData;
    }

    public void setChapterData(List<ChapterDatum> chapterData) {
        this.chapterData = chapterData;
    }

    public static class ChapterDatum {

        @SerializedName("chapterId")
        @Expose
        private String chapterId;
        @SerializedName("chapterName")
        @Expose
        private String chapterName;
        @SerializedName("zipName")
        @Expose
        private String zipName;
        @SerializedName("zip")
        @Expose
        private String zip;
        @SerializedName("videoName")
        @Expose
        private String videoName;
        @SerializedName("assessmentName")
        @Expose
        private String assessmentName;
        @SerializedName("assessmentValue")
        @Expose
        private String assessmentValue;
        @SerializedName("rsarappAssessmentValue")
        @Expose
        private String rsarappAssessmentValue;
        @SerializedName("chapterIdDb")
        @Expose
        private String chapterIdDb;
        @SerializedName("dbName")
        @Expose
        private String dbName;
        @SerializedName("downloadStatus")
        @Expose
        private String downloadStatus;
        @SerializedName("downloadLink")
        @Expose
        private String downloadLink;

        public String getChapterId() {
            return chapterId;
        }

        public void setChapterId(String chapterId) {
            this.chapterId = chapterId;
        }

        public String getChapterName() {
            return chapterName;
        }

        public void setChapterName(String chapterName) {
            this.chapterName = chapterName;
        }

        public String getZipName() {
            return zipName;
        }

        public void setZipName(String zipName) {
            this.zipName = zipName;
        }

        public String getZip() {
            return zip;
        }

        public void setZip(String zip) {
            this.zip = zip;
        }

        public String getVideoName() {
            return videoName;
        }

        public void setVideoName(String videoName) {
            this.videoName = videoName;
        }

        public String getAssessmentName() {
            return assessmentName;
        }

        public void setAssessmentName(String assessmentName) {
            this.assessmentName = assessmentName;
        }

        public String getAssessmentValue() {
            return assessmentValue;
        }

        public void setAssessmentValue(String assessmentValue) {
            this.assessmentValue = assessmentValue;
        }

        public String getRsarappAssessmentValue() {
            return rsarappAssessmentValue;
        }

        public void setRsarappAssessmentValue(String rsarappAssessmentValue) {
            this.rsarappAssessmentValue = rsarappAssessmentValue;
        }

        public String getChapterIdDb() {
            return chapterIdDb;
        }

        public void setChapterIdDb(String chapterIdDb) {
            this.chapterIdDb = chapterIdDb;
        }

        public String getDbName() {
            return dbName;
        }

        public void setDbName(String dbName) {
            this.dbName = dbName;
        }

        public String getDownloadStatus() {
            return downloadStatus;
        }

        public void setDownloadStatus(String downloadStatus) {
            this.downloadStatus = downloadStatus;
        }

        public String getDownloadLink() {
            return downloadLink;
        }

        public void setDownloadLink(String downloadLink) {
            this.downloadLink = downloadLink;
        }

    }

}