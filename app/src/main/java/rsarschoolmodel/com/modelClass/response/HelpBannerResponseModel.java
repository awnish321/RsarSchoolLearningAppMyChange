package rsarschoolmodel.com.modelClass.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HelpBannerResponseModel {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("bannersData")
    @Expose
    private List<BannersDatum> bannersData;

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

    public List<BannersDatum> getBannersData() {
        return bannersData;
    }

    public void setBannersData(List<BannersDatum> bannersData) {
        this.bannersData = bannersData;
    }

    public static class BannersDatum {

        @SerializedName("BannerId")
        @Expose
        private String bannerId;
        @SerializedName("BannerURL")
        @Expose
        private String bannerURL;

        public String getBannerId() {
            return bannerId;
        }

        public void setBannerId(String bannerId) {
            this.bannerId = bannerId;
        }

        public String getBannerURL() {
            return bannerURL;
        }

        public void setBannerURL(String bannerURL) {
            this.bannerURL = bannerURL;
        }

    }

}