package cn.elnet.andrmb.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/6/21.
 */
public class AssignmentReportTurple {
    private List<AssignmentType> assignmentTypeList;
    private List<ReportInfo>     reportInfoList;

    private ScoreType scoreType;

    public List<AssignmentType> getAssignmentTypeList() {
        return assignmentTypeList;
    }

    public void setAssignmentTypeList(List<AssignmentType> assignmentTypeList) {
        this.assignmentTypeList = assignmentTypeList;
    }

    public List<ReportInfo> getReportInfoList() {
        return reportInfoList;
    }

    public ScoreType getScoreType() {
        return scoreType;
    }

    public void setScoreType(ScoreType scoreType) {
        this.scoreType = scoreType;
    }

    public void setReportInfoList(List<ReportInfo> reportInfoList) {
        this.reportInfoList = reportInfoList;

    }
}
