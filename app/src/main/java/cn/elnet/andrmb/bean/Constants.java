package cn.elnet.andrmb.bean;

import java.util.HashMap;
import java.util.Map;

public class Constants {
	public static final int STATUS_NORMAL=0;
    public static final int STATUS_ACTIVE=1;
    public static final int STATUS_CARD_REMOVED=2;
    public static final int STATUS_CANCEL=3;
    public static final int STATUS_EXPIRED=4;
    public static final int STATUS_USED=5;
    
    private static Map<Integer,String> emptyDescriptionMap=new HashMap<Integer, String>();
    static{
    	initEmptyDescriptionMap();
    }
    private static void initEmptyDescriptionMap(){
    	emptyDescriptionMap.put(STATUS_NORMAL, "暂无数据");
    	emptyDescriptionMap.put(STATUS_ACTIVE, "暂无数据");
    	emptyDescriptionMap.put(STATUS_CANCEL, "暂无数据");
    	emptyDescriptionMap.put(STATUS_EXPIRED, "暂无数据");
      	emptyDescriptionMap.put(STATUS_USED, "暂无数据");
    }
    public static Map<Integer,String> getEmptyDescriptionMap(){
    	return emptyDescriptionMap;
    }
  
}
