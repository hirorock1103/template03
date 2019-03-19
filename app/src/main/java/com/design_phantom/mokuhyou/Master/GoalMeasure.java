package com.design_phantom.mokuhyou.Master;

public class GoalMeasure {

    private int measureId;
    private String measureTitle;
    private int parentGoalId;
    private String measureType;
    private int measureIntValue;
    private byte[] measureImageValue;
    private String measureDate;
    private String createdate;
    private String updatedate;

    public int getMeasureId() {
        return measureId;
    }

    public void setMeasureId(int measureId) {
        this.measureId = measureId;
    }

    public String getMeasureTitle() {
        return measureTitle;
    }

    public void setMeasureTitle(String measureTitle) {
        this.measureTitle = measureTitle;
    }

    public int getParentGoalId() {
        return parentGoalId;
    }

    public void setParentGoalId(int parentGoalId) {
        this.parentGoalId = parentGoalId;
    }

    public String getMeasureType() {
        return measureType;
    }

    public void setMeasureType(String measureType) {
        this.measureType = measureType;
    }

    public String getCreatedate() {
        return createdate;
    }

    public void setCreatedate(String createdate) {
        this.createdate = createdate;
    }

    public String getUpdatedate() {
        return updatedate;
    }

    public void setUpdatedate(String updatedate) {
        this.updatedate = updatedate;
    }

    public String getMeasureDate() {
        return measureDate;
    }

    public void setMeasureDate(String measureDate) {
        this.measureDate = measureDate;
    }

    public int getMeasureIntValue() {
        return measureIntValue;
    }

    public void setMeasureIntValue(int measureIntValue) {
        this.measureIntValue = measureIntValue;
    }

    public byte[] getMeasureImageValue() {
        return measureImageValue;
    }

    public void setMeasureImageValue(byte[] measureImageValue) {
        this.measureImageValue = measureImageValue;
    }
}
