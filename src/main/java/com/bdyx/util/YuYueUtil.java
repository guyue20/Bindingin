package com.bdyx.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.log4j.Log4j;
import org.thymeleaf.util.StringUtils;
import sun.misc.BASE64Decoder;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

@Log4j
public class YuYueUtil {
    private static final String SESSIONID_KEY = "WE3434454TD232345688HDSTY963ND402RS256F";
    private static final String FROMTYPE_KEY = "w42w38423387hd33567";
    private static final String VERSION_KEY = "1.0";

    //晋医通94001
    private static final String SESSIONID_KEY_JYT = "JYT00018FA35C2767284B3AAB35B11EADDB8C4A";
    private static final String FROMTYPE_KEY_JYT = "jyt1b5671614d5f9295";
    private static final String VERSION_KEY_JYT = "1.0";

    //健康之路94002
    private static final String SESSIONID_KEY_JKZL = "JKZL00162CD461F252545A6885B9503DB3E84E0";
    private static final String FROMTYPE_KEY_JKZL = "jkzl1baa15627058eed";
    private static final String VERSION_KEY_JKZL = "1.0";

    //平安好医生94057
    private static final String SESSIONID_KEY_PAHYS = "PAHYS014B30BD143BF44811A215DC0B06483E66";
    private static final String FROMTYPE_KEY_PAHYS = "pahys1cdb472f63c7ca";
    private static final String VERSION_KEY_PAHYS = "1.0";

    /**
     * 截取倒数num位的str字符串
     *
     * @param str
     * @param num
     * @return
     */
    public static String getSubStr(String str, int num) {
        String result = "";
        int i = 0;
        while (i < num) {
            int lastFirst = str.lastIndexOf('/');
            result = str.substring(lastFirst) + result;
            str = str.substring(0, lastFirst);
            i++;
        }
        return result.substring(1);
    }

    /*
     * Base64请求返回解密,先URLDecoder再BASE64Decoder
     */
    @SuppressWarnings("deprecation")
    public static String decode(String body) {
        byte[] b = null;
        String result = null;
        if (body != null) {
            BASE64Decoder decoder = new BASE64Decoder();
            //b = Base64L.decodeBase64(body);
            try {
                while (body.contains("%")) {
                    body = URLDecoder.decode(body);
                }
                b = decoder.decodeBuffer(body);
                result = new String(b, "utf-8");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 得到head
     *
     * @return
     */
    public static Map<String, Object> getHeadMap() {
        Map<String, Object> headMap = new HashMap<String, Object>();
        Long time = System.currentTimeMillis();
        headMap.put("version", VERSION_KEY);
        headMap.put("fromtype", FROMTYPE_KEY);
        headMap.put("sessionid", SESSIONID_KEY);
        headMap.put("time", time);
        headMap.put("token",
                MD5.GetMD5Code(SESSIONID_KEY + FROMTYPE_KEY + time));
        return headMap;
    }

    /**
     * 校验head参数
     *
     * @param head
     * @param body
     * @return
     */
    public static Map<String, Object> verifyParamsStandard(String head, String body) {
        Map<String, Object> result = new HashMap<String, Object>();
        JSONObject bodyJson = JSON.parseObject(body);
        String opera = bodyJson.getString("userid");
        log.info("-------------进入head校验-------------");
        JSONObject headJson = JSON.parseObject(head);
        String token = headJson.getString("token");
        String fromtype = headJson.getString("fromtype");
        String time = headJson.getString("time");
        if (StringUtils.isEmpty(token) || StringUtils.isEmpty(fromtype)
                || StringUtils.isEmpty(time)) {
            result.put("code", 0);// 参数有误
            result.put("msg", "head参数有空值！");// 参数有误
            return result;
        }
        String check = SESSIONID_KEY + fromtype + time;
        if (StringUtils.isEmpty(opera)) {
            opera = "94003";
        }
        if (opera.trim().equals("94001")) {//近医通94001
            check = SESSIONID_KEY_JYT + fromtype + time;
        } else if (opera.trim().equals("94002")) {//健康之路94002
            check = SESSIONID_KEY_JKZL + fromtype + time;
        } else if (opera.trim().equals("94057")) {//平安好医生94057
            check = SESSIONID_KEY_PAHYS + fromtype + time;
        } else {
//			result.put("code", 0);// 参数有误
//			result.put("msg", "head参数校验未通过！");// 参数有误
//			return result;
        }
        // String check=SESSIONID_KEY+fromtype+time+"1";
        String newstr = MD5.GetMD5Code(check);
        log.info("head:" + head);
        log.info("token:" + token);
        log.info("newstr:" + newstr);
        if (!newstr.equals(token)) {
            result.put("code", 0);// 参数有误
            result.put("msg", "head参数校验未通过！");// 参数有误
            //return result;
        }
        log.info("-------------校验head完成-------------");
        result.put("code", 1);// 参数
        result.put("msg", "head参数校验通过！");// 参数
        return result;
    }

    /**
     * 获取异常message
     *
     * @param e
     * @return
     */
    public static String getStackMsg(Throwable e) {
        StringBuffer sb = new StringBuffer();
        StackTraceElement[] stackArray = e.getStackTrace();
        for (int i = 0; i < stackArray.length; i++) {
            StackTraceElement element = stackArray[i];
            sb.append(element.toString() + "\n");
        }
        return sb.toString();
    }
}
