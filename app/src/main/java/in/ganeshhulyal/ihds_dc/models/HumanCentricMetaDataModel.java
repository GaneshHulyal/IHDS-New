package in.ganeshhulyal.ihds_dc.models;

public class HumanCentricMetaDataModel {

    private String locationType;
    private String subLocation;
    private String timing;
    private String lighting;
    private String deviceUsed;
    private String imageOrientation;
    private String resolution;
    private String dslrAndMobilePaired;
    private String dataMajorCategory;
    private String isHumanCentric;
    private String selfie;
    private String type;
    private String isAboveEighteen;
    private String isConsenObtained;

    public HumanCentricMetaDataModel(String locationType, String subLocation, String timing, String lighting, String deviceUsed, String imageOrientation, String resolution, String dslrAndMobilePaired, String dataMajorCategory, String isHumanCentric, String selfie, String type, String isAboveEighteen, String isConsenObtained) {
        this.locationType = locationType;
        this.subLocation = subLocation;
        this.timing = timing;
        this.lighting = lighting;
        this.deviceUsed = deviceUsed;
        this.imageOrientation = imageOrientation;
        this.resolution = resolution;
        this.dslrAndMobilePaired = dslrAndMobilePaired;
        this.dataMajorCategory = dataMajorCategory;
        this.isHumanCentric = isHumanCentric;
        this.selfie = selfie;
        this.type = type;
        this.isAboveEighteen = isAboveEighteen;
        this.isConsenObtained = isConsenObtained;
    }

    public String getIsHumanCentric() {
        return isHumanCentric;
    }

    public void setIsHumanCentric(String isHumanCentric) {
        this.isHumanCentric = isHumanCentric;
    }

    public String getSelfie() {
        return selfie;
    }

    public void setSelfie(String selfie) {
        this.selfie = selfie;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIsAboveEighteen() {
        return isAboveEighteen;
    }

    public void setIsAboveEighteen(String isAboveEighteen) {
        this.isAboveEighteen = isAboveEighteen;
    }

    public String getIsConsenObtained() {
        return isConsenObtained;
    }

    public void setIsConsenObtained(String isConsenObtained) {
        this.isConsenObtained = isConsenObtained;
    }


    public String getDeviceUsed() {
        return deviceUsed;
    }

    public void setDeviceUsed(String deviceUsed) {
        this.deviceUsed = deviceUsed;
    }

    public String getImageOrientation() {
        return imageOrientation;
    }

    public void setImageOrientation(String imageOrientation) {
        this.imageOrientation = imageOrientation;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public String getDslrAndMobilePaired() {
        return dslrAndMobilePaired;
    }

    public void setDslrAndMobilePaired(String dslrAndMobilePaired) {
        this.dslrAndMobilePaired = dslrAndMobilePaired;
    }

    public String getDataMajorCategory() {
        return dataMajorCategory;
    }

    public void setDataMajorCategory(String dataMajorCategory) {
        this.dataMajorCategory = dataMajorCategory;
    }

    public String getLocationType() {
        return locationType;
    }

    public void setLocationType(String locationType) {
        this.locationType = locationType;
    }

    public String getSubLocation() {
        return subLocation;
    }

    public void setSubLocation(String subLocation) {
        this.subLocation = subLocation;
    }

    public String getTiming() {
        return timing;
    }

    public void setTiming(String timing) {
        this.timing = timing;
    }

    public String getLighting() {
        return lighting;
    }

    public void setLighting(String lighting) {
        this.lighting = lighting;
    }


}
