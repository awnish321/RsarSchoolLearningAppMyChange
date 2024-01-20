package rsarschoolmodel.com.modelClass.response;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class ContactUsResponseModel {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("contactData")
    @Expose
    private ContactData contactData;

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

    public ContactData getContactData() {
        return contactData;
    }

    public void setContactData(ContactData contactData) {
        this.contactData = contactData;
    }

    public static class ContactData {

        @SerializedName("phone")
        @Expose
        private String phone;
        @SerializedName("mobile")
        @Expose
        private String mobile;
        @SerializedName("whatsAppNo")
        @Expose
        private String whatsAppNo;
        @SerializedName("emailId")
        @Expose
        private String emailId;
        @SerializedName("headOfficeAddress")
        @Expose
        private String headOfficeAddress;
        @SerializedName("websiteUrl")
        @Expose
        private String websiteUrl;

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getWhatsAppNo() {
            return whatsAppNo;
        }

        public void setWhatsAppNo(String whatsAppNo) {
            this.whatsAppNo = whatsAppNo;
        }

        public String getEmailId() {
            return emailId;
        }

        public void setEmailId(String emailId) {
            this.emailId = emailId;
        }

        public String getHeadOfficeAddress() {
            return headOfficeAddress;
        }

        public void setHeadOfficeAddress(String headOfficeAddress) {
            this.headOfficeAddress = headOfficeAddress;
        }

        public String getWebsiteUrl() {
            return websiteUrl;
        }

        public void setWebsiteUrl(String websiteUrl) {
            this.websiteUrl = websiteUrl;
        }

    }

}