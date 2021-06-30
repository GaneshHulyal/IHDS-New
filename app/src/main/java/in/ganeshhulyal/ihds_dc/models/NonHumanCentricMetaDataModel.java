package in.ganeshhulyal.ihds_dc.models;

public class NonHumanCentricMetaDataModel {

    private String locationType;
    private String subLocation;
    private String timing;
    private String lighting;
    private String deviceUsed;
    private String imageOrientation;
    private String resolution;
    private String dslrAndMobilePaired;
    private String dataMajorCategory;


    public NonHumanCentricMetaDataModel(String locationType, String subLocation, String timing, String lighting, String dataMajorCategory, String deviceUsed, String imageOrientation, String dslrAndMobilePaired, String resolution ) {
        this.locationType = locationType;
        this.subLocation = subLocation;
        this.timing = timing;
        this.lighting = lighting;
        this.dataMajorCategory=dataMajorCategory;
        this.deviceUsed=deviceUsed;
        this.imageOrientation=imageOrientation;
        this.dslrAndMobilePaired=dslrAndMobilePaired;
        this.resolution=resolution;
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
