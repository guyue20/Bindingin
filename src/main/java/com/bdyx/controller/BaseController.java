package com.bdyx.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bdyx.service.CommonService;
import com.bdyx.util.YuYueUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BaseController {
    @Autowired
    CommonService commonService;
    public final Boolean deBug = false;

    /**
     * @param :加密的request
     * @return
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     * @调试模式 deBug=False,发布时需要deBug=True.
     * @author:薛建行 邢玉博
     * @创建日期:2017年08月16日 上午11:25:00
     * @功能说明:获得返回map集合 *
     */
    public Map<String, Object> getResultMapDebug(HttpServletRequest request, Boolean deBug)
            throws NoSuchAlgorithmException, UnsupportedEncodingException {
        /**
         * Map key定义=url,code,msg,result,id,interfaceName,head,body,resHead
         */
        Map<String, Object> result = new HashMap<String, Object>();
        Map<String, Object> msgMap = new HashMap<String, Object>();

        StringBuilder url = new StringBuilder(request.getScheme());
        url.append("://");
        url.append(request.getServerName());
        url.append(":");
        url.append(request.getServerPort());
        url.append(request.getContentLength());
        //截取请求的接口名称
        StringBuffer requestURL = request.getRequestURL();
        String interfaceName = YuYueUtil.getSubStr(requestURL.toString(), 2);
        //向日志表插入初始请求时间
        String id = UUID.randomUUID().toString();
        try {
            commonService.insertYyptHappenTime(id, interfaceName);
        } catch (Exception e) {
            System.out.println("写入数据库日志异常" + e.getMessage());
            result.put("code", 0);
            result.put("msg", "数据写入异常");
            return result;
            //需要时，保存本地日志
        }
        //解析请求参数----begin----
        String decodeHead = request.getParameter("head");
        String decodeBody = request.getParameter("body");
        String decodeVisit = "";
        String decodeReceiptCharge = "";
        if ("pay/scanpay".equals(interfaceName)) {
            decodeVisit = request.getParameter("visittable");
            decodeReceiptCharge = request.getParameter("receiptcharge");
        }
        if (decodeHead == null && decodeBody == null) {
            Map<String, String[]> parameterMap = request.getParameterMap();
            for (String items : parameterMap.keySet()) {
                String[] values = parameterMap.get(items);
                for (int i = 0; i < values.length; i++) {
                    String val = values[i];
                    JSONObject jsonObject = JSONObject.parseObject(val);
                    decodeHead = jsonObject.getString("head");
                    decodeBody = jsonObject.getString("body");
                    System.out.println(val);
                }
            }
        }
        result.put("decodeHead", decodeHead); //设置山西挂号网传过来的head
        //未收到参数head的值
        if (decodeHead == null) {
            msgMap.put("msg", "未收到参数head的值！");
            msgMap.put("code", "0");
            result.put("rmsg", "未收到参数head的值！");
            result.put("code", 0);
            result.put("msg", msgMap);

            //更新数据库日志
            try {
                commonService.insertYyptParams(id, "未收到参数head的值！", "未收到参数body的值！",
                        "未收到参数head的值！", result.get("msg").toString(), "", interfaceName);
            } catch (Exception e) {
                System.out.println("数据库日志更新异常" + e.getMessage());
                //需要时，保存本地日志
            } finally {
                return result;
            }
        }
        //String head = YuYueUtil.decode(decodeHead);
        //String body = YuYueUtil.decode(decodeBody);
        String head = decodeHead;
        String body = decodeBody;
        String visit = "";
        String ReceiptCharge = "";
        if ("pay/scanpay".equals(interfaceName)) {
            visit = YuYueUtil.decode(decodeVisit);
            ReceiptCharge = YuYueUtil.decode(decodeReceiptCharge);
        }
        //设置response的head参数加密
        Map<String, Object> resHead = YuYueUtil.getHeadMap();
        //判断接口是否开启
        Integer status = new Integer(1);
        //先解析一下userid，获取接口开启关闭的需要用该参数
        String opera = "";
        if ("pay/scanpay".equals(interfaceName) || "pay/orderquery".equals(interfaceName) || "pay/refund".equals(interfaceName) ||
                "pay/hispaynotice".equals(interfaceName) || "pay/ordercancel".equals(interfaceName) || "lab/report".equals(interfaceName) || "deposit/pay".equals(interfaceName)) {
            opera = "94003";
        } else {
            JSONObject bodyJson = JSON.parseObject(body);
            opera = bodyJson.getString("userid");
            if (StringUtils.isEmpty(opera)) {
                opera = "94003";
            }
        }
        try {
            //status = commonService.selectInterfaceOpenStatus(interfaceName,1);	//V1版本
            //status = commonService.selectInterfaceOpenStatusProc(interfaceName,opera,1);
        } catch (Exception e) {
            System.out.println(e);
            status = 0;
        }
        //设置返回参数
        result.put("url", requestURL);
        result.put("head", head);
        result.put("body", body);
        result.put("visit", visit);
        result.put("ReceiptCharge", ReceiptCharge);
        result.put("id", id);
        result.put("interfaceName", interfaceName);
        result.put("resHead", resHead);

        if (null == status || 0 == status) {
            msgMap.put("msg", "该接口服务已关闭！");
            msgMap.put("code", "0");
            result.put("rmsg", "该接口服务已关闭！");
            result.put("code", 0);
            result.put("msg", msgMap);
            //更新数据库日志
            try {
                commonService.insertYyptParams(id, head, body,
                        head,
                        result.get("msg").toString(), "", interfaceName);
            } catch (Exception e) {
                System.out.println("数据库日志更新异常" + e.getMessage());
                //需要时，保存本地日志
            } finally {
                return result;
            }
        }
        //debug 模式的话直接返回信息
        if (deBug) {
            result.put("code", 1);
            return result;
        }
        if ("pay/scanpay".equals(interfaceName) || "pay/orderquery".equals(interfaceName) || "pay/hispaynotice".equals(interfaceName)
                || "pay/refund".equals(interfaceName) || "pay/ordercancel".equals(interfaceName)) {
            //窗口扫码支付 不校验head
            result.put("code", 1);
        } else {
            //校验head
            Map<String, Object> verifyMap = YuYueUtil.verifyParamsStandard(head, body);
            if (null == verifyMap.get("code") || (1 != (Integer) verifyMap.get("code"))) {
                msgMap.put("msg", verifyMap.get("msg"));
                msgMap.put("code", "0");
                result.put("rmsg", verifyMap.get("msg"));
                result.put("code", 0);
                result.put("msg", msgMap);
                //更新数据库日志
                try {
                    commonService.insertYyptParams(id, head.replace(" ", ""), body.replace(" ", ""),
                            head.replace(" ", ""), verifyMap.toString(),
                            JSONObject.toJSON(msgMap).toString(), interfaceName);
                } catch (Exception e) {
                    System.out.println("数据库日志更新异常" + e.getMessage());
                    //需要时，保存本地日志
                } finally {
                    return result;
                }
            } else {
                result.put("code", 1);
            }
        }
        return result;
    }
}
