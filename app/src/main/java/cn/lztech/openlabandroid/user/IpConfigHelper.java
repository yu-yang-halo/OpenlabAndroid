package cn.lztech.openlabandroid.user;

import android.content.Context;

import cn.elnet.andrmb.elconnector.WSConnector;
import cn.lztech.openlabandroid.cache.ContentBox;

/**
 * Created by Administrator on 2016/12/14.
 */

public class IpConfigHelper {
    public static String[] fetchIpAddrPortArr(Context ctx){
        String ipAddrStr= ContentBox.getValueString(ctx,ContentBox.KEY_IP, WSConnector.IP1);
        String portStr=ContentBox.getValueString(ctx,ContentBox.KEY_PORT,"8080");
        return new String[]{ipAddrStr,portStr};
    }
    public static void saveIpAddrPortArr(Context ctx,String ipAddr,String portStr){
        ContentBox.loadString(ctx,ContentBox.KEY_IP,ipAddr);
        ContentBox.loadString(ctx,ContentBox.KEY_PORT,portStr);
    }

}
