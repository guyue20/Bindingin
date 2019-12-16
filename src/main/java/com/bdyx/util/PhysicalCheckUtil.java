package com.bdyx.util;

import com.alibaba.druid.util.StringUtils;

import java.util.*;

public class PhysicalCheckUtil {
    public static Map<String, Object> checkJCSave(String patientId) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (StringUtils.isEmpty(patientId)) {
            map.put("code", -1);
//			map.put("msg", "ypbh为空");
//			return map;
        }
        map.put("code", 1);
        return map;
    }
}
