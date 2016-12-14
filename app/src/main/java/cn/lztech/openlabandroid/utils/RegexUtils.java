package cn.lztech.openlabandroid.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2015/7/23.
 */
public class RegexUtils {
    public  static  boolean isIPAddress(String ipaddr){
        /**
         * 判断IP格式和范围
         */
        String rexp = "((?:(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))\\.){3}(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d))))";

        Pattern pat = Pattern.compile(rexp);

        Matcher mat = pat.matcher(ipaddr);


        return mat.matches();

    }
    public static boolean isNumber(String number){
        /**
         * 判断是否是手机号码
         *
         *     String rexp="^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";
         Pattern p = Pattern.compile(rexp);
         Matcher m = p.matcher(mobiles);
         return m.matches();
         */
        String rexp="\\d{2,7}$";
        Pattern p = Pattern.compile(rexp);
        Matcher m = p.matcher(number);
        return m.matches();
    }
    public static boolean isMobileNO(String mobiles){
        /**
         * 判断是否是手机号码
         *
         *     String rexp="^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";
         Pattern p = Pattern.compile(rexp);
         Matcher m = p.matcher(mobiles);
         return m.matches();
         */
        String rexp="\\d{11}$";
        Pattern p = Pattern.compile(rexp);
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }
    public static boolean isEamil(String email){
        /**
         * 判断是否是邮箱
         */
        String rexp="^[a-z0-9]+([._\\\\-]*[a-z0-9])*@([a-z0-9]+[-a-z0-9]*[a-z0-9]+.){1,63}[a-z0-9]+$";
        Pattern pattern = Pattern.compile(rexp);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    public static boolean isVaildPass(String password){
        /**
         * 判断有效密码格式
         */
        String rexp="^[@A-Za-z0-9!#$%^&*.~]{6,22}$";

        Pattern p = Pattern.compile(rexp);
        Matcher m = p.matcher(password);
        return m.matches();
    }
    public static boolean isVaildLoginName(String name){
        /**
         * 判断登录名是否有效
         */
        String rexp="^[@A-Za-z0-9!#$%^&*.~]{5,22}$";

        Pattern p = Pattern.compile(rexp);
        Matcher m = p.matcher(name);
        return m.matches();
    }
}
