package cn.elnet.andrmb.bean;

/**
 * Created by Administrator on 2016/10/20.
 */

public class ScoreType {
    public ScoreType(int studentId,int courseId,String courseCode, float score, String comment, short status) {
        this.studentId = studentId;
        this.courseId=courseId;
        this.courseCode = courseCode;
        this.score = score;
        this.comment = comment;
        this.status = status;
    }

    /**
        <studentId>26</studentId>
        <courseCode>AT20160927</courseCode>
        <score>20</score>
        <comment>很不好</comment>
        <status>1</status>
     */


    private int studentId;
    private int courseId;
    private String courseCode;
    private float score;
    private String comment;
    private short status;

    private String courseName;

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public short getStatus() {
        return status;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    @Override
    public String toString() {
        return "ScoreType{" +
                "studentId=" + studentId +
                ", courseCode='" + courseCode + '\'' +
                ", score=" + score +
                ", comment='" + comment + '\'' +
                ", status=" + status +
                '}';
    }

    public void setStatus(short status) {
        this.status = status;
    }
}
