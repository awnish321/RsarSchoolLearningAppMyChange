package rsarschoolmodel.com.modelClass.request;

public class UserDeviceDetailRequestModel {
    private String deviceId;
    private String mobId;
    private String mobProduct;
    private String mobBrand;
    private String mobManufacture;
    private String mobModel;

    public String getDeviceId() {
        return deviceId;
    }

    public UserDeviceDetailRequestModel(String deviceId, String mobId, String mobProduct, String mobBrand, String mobManufacture, String mobModel) {
        this.deviceId = deviceId;
        this.mobId = mobId;
        this.mobProduct = mobProduct;
        this.mobBrand = mobBrand;
        this.mobManufacture = mobManufacture;
        this.mobModel = mobModel;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getMobId() {
        return mobId;
    }

    public void setMobId(String mobId) {
        this.mobId = mobId;
    }

    public String getMobProduct() {
        return mobProduct;
    }

    public void setMobProduct(String mobProduct) {
        this.mobProduct = mobProduct;
    }

    public String getMobBrand() {
        return mobBrand;
    }

    public void setMobBrand(String mobBrand) {
        this.mobBrand = mobBrand;
    }

    public String getMobManufacture() {
        return mobManufacture;
    }

    public void setMobManufacture(String mobManufacture) {
        this.mobManufacture = mobManufacture;
    }

    public String getMobModel() {
        return mobModel;
    }

    public void setMobModel(String mobModel) {
        this.mobModel = mobModel;
    }
}
