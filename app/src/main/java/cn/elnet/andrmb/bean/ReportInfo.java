package cn.elnet.andrmb.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2016/6/21.
 */
public class ReportInfo implements Parcelable {
    private int reportId;
    private int userId;
    private int assignmentId;
    private String courseCode;
    private String description;
    private String fileName;
    private String submitTime;

    private float score;
    private String scoreComment;
    private int givenBy;
    private String givenTime;
    private short status=-1;

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public String getScoreComment() {
        return scoreComment;
    }

    public void setScoreComment(String scoreComment) {
        this.scoreComment = scoreComment;
    }

    public int getGivenBy() {
        return givenBy;
    }

    public void setGivenBy(int givenBy) {
        this.givenBy = givenBy;
    }

    public String getGivenTime() {
        return givenTime;
    }

    public void setGivenTime(String givenTime) {
        this.givenTime = givenTime;
    }

    public short getStatus() {
        return status;
    }

    public void setStatus(short status) {
        this.status = status;
    }

    public int getReportId() {
        return reportId;
    }

    public void setReportId(int reportId) {
        this.reportId = reportId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getAssignmentId() {
        return assignmentId;
    }

    public void setAssignmentId(int assignmentId) {
        this.assignmentId = assignmentId;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(String submitTime) {
        this.submitTime = submitTime;
    }


    public ReportInfo(int reportId, int userId, int assignmentId, String courseCode,
                      String description, String fileName,
                      String submitTime,float score,String scoreComment,int givenBy,
                      String givenTime,short status) {
        this.reportId = reportId;
        this.userId = userId;
        this.assignmentId = assignmentId;
        this.courseCode = courseCode;
        this.description = description;
        this.fileName = fileName;
        this.submitTime = submitTime;

        this.score=score;
        this.scoreComment=scoreComment;
        this.givenBy=givenBy;
        this.givenTime=givenTime;
        this.status=status;
    }
    public ReportInfo(){

    }


    public static final Parcelable.Creator<ReportInfo> CREATOR = new Creator<ReportInfo>() {

        @Override
        public ReportInfo[] newArray(int size) {
            return null;
        }

        @Override
        public ReportInfo createFromParcel(Parcel source) {
            ReportInfo result = new ReportInfo();
            result.reportId = source.readInt();
            result.userId = source.readInt();
            result.assignmentId = source.readInt();

            result.courseCode = source.readString();
            result.description = source.readString();
            result.fileName = source.readString();
            result.submitTime = source.readString();

            result.score=source.readFloat();
            result.scoreComment=source.readString();
            result.givenBy=source.readInt();
            result.givenTime=source.readString();
            result.status=(short) source.readInt();

            return result;
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public String toString() {
        return "ReportInfo{" +
                "reportId=" + reportId +
                ", userId=" + userId +
                ", assignmentId=" + assignmentId +
                ", courseCode='" + courseCode + '\'' +
                ", description='" + description + '\'' +
                ", fileName='" + fileName + '\'' +
                ", submitTime='" + submitTime + '\'' +
                '}';
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(reportId);
        dest.writeInt(userId);
        dest.writeInt(assignmentId);
        dest.writeString(courseCode);
        dest.writeString(description);
        dest.writeString(fileName);
        dest.writeString(submitTime);

        dest.writeFloat(score);
        dest.writeString(scoreComment);
        dest.writeInt(givenBy);
        dest.writeString(givenTime);
        dest.writeInt(status);


    }
}
