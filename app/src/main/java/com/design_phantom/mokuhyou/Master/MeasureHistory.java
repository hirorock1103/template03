package com.design_phantom.mokuhyou.Master;

public class MeasureHistory {

    private int historyId;
    private int parentMeasureId;
    private int measureIntValue;
    private byte[] measureImageValue;
    private String measureDate;
    private String createdate;
    private String updatedate;

    public int getHistoryId() {
        return historyId;
    }

    public void setHistoryId(int historyId) {
        this.historyId = historyId;
    }

    public int getParentMeasureId() {
        return parentMeasureId;
    }

    public void setParentMeasureId(int parentMeasureId) {
        this.parentMeasureId = parentMeasureId;
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

    public String getMeasureDate() {
        return measureDate;
    }

    public void setMeasureDate(String measureDate) {
        this.measureDate = measureDate;
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
}
