package rsarschoolmodel.com.modelClass.response;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BookResponseModel
{
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("classId")
    @Expose
    private String classId;
    @SerializedName("classTitle")
    @Expose
    private String classTitle;
    @SerializedName("subjectId")
    @Expose
    private String subjectId;
    @SerializedName("subjectTitle")
    @Expose
    private String subjectTitle;
    @SerializedName("schoolUI")
    @Expose
    private String schoolUI;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("bookData")
    @Expose
    private List<BookDatum> bookData;

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

    public List<BookDatum> getBookData() {
        return bookData;
    }

    public void setBookData(List<BookDatum> bookData) {
        this.bookData = bookData;
    }

    public static class BookDatum {

        @SerializedName("bookId")
        @Expose
        private String bookId;
        @SerializedName("bookTitle")
        @Expose
        private String bookTitle;
        @SerializedName("practicePaper")
        @Expose
        private String practicePaper;
        @SerializedName("examinationPaper")
        @Expose
        private String examinationPaper;
        @SerializedName("teacherResourceManual")
        @Expose
        private String teacherResourceManual;
        @SerializedName("practicePaperValue")
        @Expose
        private String practicePaperValue;
        @SerializedName("examinationPaperValue")
        @Expose
        private String examinationPaperValue;
        @SerializedName("teacherResourceManualValue")
        @Expose
        private String teacherResourceManualValue;
        @SerializedName("rsarValue")
        @Expose
        private String rsarValue;
        @SerializedName("differentPlay")
        @Expose
        private String differentPlay;

        public String getBookId() {
            return bookId;
        }

        public void setBookId(String bookId) {
            this.bookId = bookId;
        }

        public String getBookTitle() {
            return bookTitle;
        }

        public void setBookTitle(String bookTitle) {
            this.bookTitle = bookTitle;
        }

        public String getPracticePaper() {
            return practicePaper;
        }

        public void setPracticePaper(String practicePaper) {
            this.practicePaper = practicePaper;
        }

        public String getExaminationPaper() {
            return examinationPaper;
        }

        public void setExaminationPaper(String examinationPaper) {
            this.examinationPaper = examinationPaper;
        }

        public String getTeacherResourceManual() {
            return teacherResourceManual;
        }

        public void setTeacherResourceManual(String teacherResourceManual) {
            this.teacherResourceManual = teacherResourceManual;
        }

        public String getPracticePaperValue() {
            return practicePaperValue;
        }

        public void setPracticePaperValue(String practicePaperValue) {
            this.practicePaperValue = practicePaperValue;
        }

        public String getExaminationPaperValue() {
            return examinationPaperValue;
        }

        public void setExaminationPaperValue(String examinationPaperValue) {
            this.examinationPaperValue = examinationPaperValue;
        }

        public String getTeacherResourceManualValue() {
            return teacherResourceManualValue;
        }

        public void setTeacherResourceManualValue(String teacherResourceManualValue) {
            this.teacherResourceManualValue = teacherResourceManualValue;
        }

        public String getRsarValue() {
            return rsarValue;
        }

        public void setRsarValue(String rsarValue) {
            this.rsarValue = rsarValue;
        }

        public String getDifferentPlay() {
            return differentPlay;
        }

        public void setDifferentPlay(String differentPlay) {
            this.differentPlay = differentPlay;
        }

    }

}