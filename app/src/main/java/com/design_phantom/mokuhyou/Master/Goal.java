package com.design_phantom.mokuhyou.Master;

public class Goal {

    private int goalId;
    private String goalTitle;
    private String goalExpiredDate;
    private String createdate;
    private String updatedate;


    public int getGoalId() {
        return goalId;
    }

    public void setGoalId(int goalId) {
        this.goalId = goalId;
    }

    public String getGoalTitle() {
        return goalTitle;
    }

    public void setGoalTitle(String goalTitle) {
        this.goalTitle = goalTitle;
    }

    public String getGoalExpiredDate() {
        return goalExpiredDate;
    }

    public void setGoalExpiredDate(String goalExpiredDate) {
        this.goalExpiredDate = goalExpiredDate;
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
