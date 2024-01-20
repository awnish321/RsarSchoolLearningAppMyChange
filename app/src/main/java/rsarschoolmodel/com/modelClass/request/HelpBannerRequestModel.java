package rsarschoolmodel.com.modelClass.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HelpBannerRequestModel {

    @SerializedName("action")
    @Expose
    private String action;

    public HelpBannerRequestModel(String action) {
        super();
        this.action = action;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

}