package cn.elnet.andrmb.bean;

/**
 * Created by Administrator on 2016/9/7.
 */
public class SemesterType {
    private int year;
    private int semester;

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public SemesterType(int year, int semester) {
        this.year = year;
        this.semester = semester;
    }

    @Override
    public String toString() {
        return "SemesterType{" +
                "year=" + year +
                ", semester=" + semester +
                '}';
    }

    public int getSemester() {

        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }
}
