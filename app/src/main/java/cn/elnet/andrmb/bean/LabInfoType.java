package cn.elnet.andrmb.bean;

import java.util.List;

public class LabInfoType {
	private int labId;
	private String name;
	private String desc;
	private int  numOfDesk;
	private String building;
	private short floor;
	private short room;
	
	private List<DeskInfo> deskInfos;
	
	public int getLabId() {
		return labId;
	}
	public void setLabId(int labId) {
		this.labId = labId;
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
	public int getNumOfDesk() {
		return numOfDesk;
	}
	public void setNumOfDesk(int numOfDesk) {
		this.numOfDesk = numOfDesk;
	}
	public String getBuilding() {
		return building;
	}
	public void setBuilding(String building) {
		this.building = building;
	}
	public short getFloor() {
		return floor;
	}
	public void setFloor(short floor) {
		this.floor = floor;
	}
	public short getRoom() {
		return room;
	}
	public void setRoom(short room) {
		this.room = room;
	}
	public LabInfoType(int labId, String name, String desc, int numOfDesk,
			String building, short floor, short room) {
		super();
		this.labId = labId;
		this.name = name;
		this.desc = desc;
		this.numOfDesk = numOfDesk;
		this.building = building;
		this.floor = floor;
		this.room = room;
	}
	@Override
	public String toString() {
		return "LabInfoType [labId=" + labId + ", name=" + name + ", desc="
				+ desc + ", numOfDesk=" + numOfDesk + ", building=" + building
				+ ", floor=" + floor + ", room=" + room + "]"+deskInfos;
	}
	public List<DeskInfo> getDeskInfos() {
		return deskInfos;
	}
	public void setDeskInfos(List<DeskInfo> deskInfos) {
		this.deskInfos = deskInfos;
	}
	
	
}
