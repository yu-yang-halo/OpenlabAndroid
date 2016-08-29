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

    public ReportInfo(int reportId, int userId, int assignmentId, String courseCode, String description, String fileName, String submitTime) {
        this.reportId = reportId;
        this.userId = userId;
        this.assignmentId = assignmentId;
        this.courseCode = courseCode;
        this.description = description;
        this.fileName = fileName;
        this.submitTime = submitTime;
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
    }
}
