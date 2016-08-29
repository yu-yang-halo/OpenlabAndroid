package cn.elnet.andrmb.bean;

public class AssignmentType {
	private int id;
	private String courseCode;
	private String desc;
	private String dueDate;
	private String createdTime;
	private int createdBy;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCourseCode() {
		return courseCode;
	}
	public void setCourseCode(String courseCode) {
		this.courseCode = courseCode;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getDueDate() {
		return dueDate;
	}
	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}
	public String getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(String createdTime) {
		this.createdTime = createdTime;
	}
	public int getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}
	public AssignmentType(int id, String courseCode, String desc,
			String dueDate, String createdTime, int createdBy) {
		super();
		this.id = id;
		this.courseCode = courseCode;
		this.desc = desc;
		this.dueDate = dueDate;
		this.createdTime = createdTime;
		this.createdBy = createdBy;
	}
	@Override
	public String toString() {
		return "AssignmentType [id=" + id + ", courseCode=" + courseCode
				+ ", desc=" + desc + ", dueDate=" + dueDate + ", createdTime="
				+ createdTime + ", createdBy=" + createdBy + "]";
	}
	
}
