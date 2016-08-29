package cn.elnet.andrmb.bean;

import java.util.EnumSet;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/6/14.
 */
public class CourseType {

    public enum Semester{
        NONE_SEMESTER(0),
        LAST_SEMESTER(1),//1 第一学期
        NEXT_SEMESTER(2); //2 第二学期

        private static final Map<Integer, Semester> lookup = new HashMap<Integer, Semester>();
        static {
            for (Semester s : EnumSet.allOf(Semester.class))
                lookup.put(s.getValue(), s);
        }
        private int value;

        Semester(int value){
            this.value=value;
        }
        public int getValue(){
            return this.value;
        }

        public static Semester value(int value){
            Semester semester=lookup.get(value);
            if(semester!=null){
                return semester;
            }
            return NONE_SEMESTER;
        }

    }

    private String courseCode;
    private String name;
    private String desc;
    private int year;
    private Semester semester;



    List<AssignmentType> assignmentTypes;
    List<ReportInfo> reportInfos;

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public List<AssignmentType> getAssignmentTypes() {
        return assignmentTypes;
    }

    public void setAssignmentTypes(List<AssignmentType> assignmentTypes) {
        this.assignmentTypes = assignmentTypes;
    }

    public Semester getSemester() {
        return semester;
    }

    public void setSemester(Semester semester) {
        this.semester = semester;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public CourseType(String courseCode, String name, String desc, int year, Semester semester, List<AssignmentType> assignmentTypes) {
        this.courseCode = courseCode;
        this.name = name;
        this.desc = desc;
        this.year = year;
        this.semester = semester;
        this.assignmentTypes = assignmentTypes;
    }

    public List<ReportInfo> getReportInfos() {
        return reportInfos;
    }

    public void setReportInfos(List<ReportInfo> reportInfos) {
        this.reportInfos = reportInfos;
    }
}
