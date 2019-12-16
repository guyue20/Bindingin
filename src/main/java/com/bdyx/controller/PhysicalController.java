package com.bdyx.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bdyx.entity.HisResponse;
import com.bdyx.mapper.Tkmapper;

import com.bdyx.util.PhysicalCheckUtil;
import com.bdyx.util.YuYueUtil;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.ResponseBody;


import javax.servlet.http.HttpServletRequest;

import java.util.*;

@Log4j
@Controller
public class PhysicalController extends BaseController {
    @Autowired
    Tkmapper tkmapper;


    /**
     * 体检中心有患者进行体检时，将患者基本信息提交给HIS(过程：p_wa_patient_mi)
     *
     * @param request
     * @return
     */
    //建党接口
    @RequestMapping("/app/basic/visitsave")
    @ResponseBody
    public HisResponse visitSave(HttpServletRequest request) throws Exception {
        Map<String, Object> resultMap = getResultMapDebug(request, deBug);//分解参数、校验参数
        Integer code = (Integer) resultMap.get("code");//获取校验成功失败的标记
        String decodeHead = "";
        if (null != resultMap.get("decodeHead")) {
            decodeHead = resultMap.get("decodeHead").toString();//山西挂号网传递过来的加密head
        }
        if (null == code || code == 0) {//校验失败，直接返回，这时候失败的日志在baseController中已经写了
            Object object = resultMap.get("msg");
            HisResponse hr = new HisResponse(decodeHead, JSONObject.toJSON(object));
            return hr;
        }
        String head = resultMap.get("head").toString();
        String body = resultMap.get("body").toString();
        String id = resultMap.get("id").toString();//获取日志表中的主键（uuid）
        String interfaceName = resultMap.get("interfaceName").toString();//获取接口参数
        //执行业务
        JSONObject json = JSON.parseObject(body);
        List<String> params = new ArrayList<String>();
        String patient_id = json.getString("patient_id");//门诊号
        params.add(patient_id);
        String apply_unit = json.getString("apply_unit");//申请科室
        params.add(apply_unit);
        String social_no = json.getString("social_no");//证件号码
        params.add(social_no);
        String name = json.getString("name");//姓名
        params.add(name);
        String sex = json.getString("sex");//性别
        params.add(sex);
        String birth_place = json.getString("birth_place");//联系地址
        params.add(birth_place);
        String birthday = json.getString("birthday");//出生日期
        params.add(birthday);
        String tel = json.getString("tel");//电话
        params.add(tel);
        String response_type = json.getString("response_type");//患者类别（类型）
        params.add(response_type);
        String apply_opera = json.getString("apply_opera");//申请人
        params.add(apply_opera);

        String flag = json.getString("flag");//团检个标识
        params.add(flag);

        String employer_name = json.getString("employer_name");// 单位名称
        params.add(employer_name);
        log.info("参数");
        log.info("通知医院HIS同步体检患者基本信息basic/visitsave接口入参：" + params);
        Map<String, Object> bodyMap = new HashMap<String, Object>();
        String message = "";
        //校验body
        Map<String, Object> checkJCSave = PhysicalCheckUtil.checkJCSave(patient_id);
        if (((Integer) checkJCSave.get("code")) != 1) {//校验body失败
            bodyMap.put("code", 0);
            bodyMap.put("msg", checkJCSave.get("msg"));
            commonService.insertYyptParams(id, head, body, head, bodyMap.toString(), message, interfaceName);//保存日志
            HisResponse hr = new HisResponse(decodeHead, bodyMap);
            return hr;
        }
        String logbody = "";
        try {
            //log.info("通知医院诊间加号成功，写入患者信息、挂号信息、以及费用信息：参数"+params.toString());
            //在这进行替换
            String hsql = "exec p_wa_patient_mi '" + patient_id + "','" + apply_unit + "','" + social_no + "','" + name + "','" + sex + "','" + birth_place + "','" + birthday + "','" + tel + "','" + response_type + "','" + apply_opera + "','" + flag + "','" + employer_name + "'";
            log.info(hsql);
            Map<String, Object> mess = tkmapper.callPronamesql(hsql);
            bodyMap.put("code", 1);
            bodyMap.put("msg", "检查申请取消");
            bodyMap.put("items", mess);
            logbody = bodyMap.toString();
        } catch (Exception e) {
            bodyMap.put("code", 0);
            bodyMap.put("msg", e.getMessage());
            message = e.toString().replace("'", "''") + ":" + YuYueUtil.getStackMsg(e).replace("'", "'");
            logbody = bodyMap.toString().replace("'", "*");
            e.printStackTrace();
            e.printStackTrace();
        } finally {
            HisResponse hr = new HisResponse(decodeHead, bodyMap);
            return hr;
        }
    }

    //收费申请
    @RequestMapping("/app/charge/apply")
    @ResponseBody
    public HisResponse chargeApply(HttpServletRequest request) throws Exception {
        Map<String, Object> resultMap = getResultMapDebug(request, deBug);//分解参数、校验参数
        Integer code = (Integer) resultMap.get("code");//获取校验成功失败的标记
        String decodeHead = "";
        if (null != resultMap.get("decodeHead")) {
            decodeHead = resultMap.get("decodeHead").toString();//山西挂号网传递过来的加密head
        }
        if (null == code || code == 0) {//校验失败，直接返回，这时候失败的日志在baseController中已经写了
            Object object = resultMap.get("msg");
            HisResponse hr = new HisResponse(decodeHead, JSONObject.toJSON(object));
            return hr;
        }
        String head = resultMap.get("head").toString();
        String body = resultMap.get("body").toString();
        String id = resultMap.get("id").toString();//获取日志表中的主键（uuid）
        String interfaceName = resultMap.get("interfaceName").toString();//获取接口参数
        //执行业务
        JSONObject json = JSON.parseObject(body);
        List<String> params = new ArrayList<String>();
        String patient_id = json.getString("patient_id");//门诊号
        params.add(patient_id);
        String times = json.getString("times");//就诊次数
        params.add(times);
        String charge_no = json.getString("charge_no");//申请单号
        params.add(charge_no);
        String apply_unit = json.getString("apply_unit");//申请科室
        params.add(apply_unit);
        String apply_opera = json.getString("apply_opera");//申请人
        params.add(apply_opera);
        String flag = json.getString("flag");//检查检验标志
        params.add(flag);
        String codejc = json.getString("code");//检查检验标志
        params.add(codejc);
        String charge_total = json.getString("charge_total");//总金额
        params.add(charge_total);

        log.info("参数");
        log.info("通知医院HIS同步体检患者基本信息basic/visitsave接口入参：" + params);
        Map<String, Object> bodyMap = new HashMap<String, Object>();
        String message = "";
        //校验body
        Map<String, Object> checkJCSave = PhysicalCheckUtil.checkJCSave(patient_id);
        if (((Integer) checkJCSave.get("code")) != 1) {//校验body失败
            bodyMap.put("code", 0);
            bodyMap.put("msg", checkJCSave.get("msg"));
            commonService.insertYyptParams(id, head, body, head, bodyMap.toString(), message, interfaceName);//保存日志
            HisResponse hr = new HisResponse(decodeHead, bodyMap);
            return hr;
        }
        String logbody = "";
        try {
            String hsql = "exec p_wa_charge_apply '" + patient_id + "','" + times + "','" + charge_no
                    + "','" + apply_unit + "','" + apply_opera + "','" + flag + "','" + code + "','" + charge_total + "'";
            log.info(hsql);
            Map<String, Object> mess = tkmapper.callPronamesql(hsql);
            //执行另外一个存储过程

            String sql = "exec p_wa_charge_to_detail '" + patient_id + "','" + charge_no + "','" + charge_total + "'";
            log.info(sql);
            // Map<String, Object> messsql=tkmapper.callPronamesql(sql);

            bodyMap.put("code", 1);
            bodyMap.put("msg", "检查申请取消");
            bodyMap.put("items", mess);
            logbody = bodyMap.toString();
        } catch (Exception e) {
            bodyMap.put("code", 0);
            bodyMap.put("msg", e.getMessage());
            message = e.toString().replace("'", "''") + ":" + YuYueUtil.getStackMsg(e).replace("'", "'");
            logbody = bodyMap.toString().replace("'", "*");
            e.printStackTrace();
            e.printStackTrace();
        } finally {
            HisResponse hr = new HisResponse(decodeHead, bodyMap);
            return hr;
        }
    }

    //退费申请
    @RequestMapping("/app/cancel/apply")
    @ResponseBody
    public HisResponse cancelApply(HttpServletRequest request) throws Exception {
        Map<String, Object> resultMap = getResultMapDebug(request, deBug);//分解参数、校验参数
        Integer code = (Integer) resultMap.get("code");//获取校验成功失败的标记
        String decodeHead = "";
        if (null != resultMap.get("decodeHead")) {
            decodeHead = resultMap.get("decodeHead").toString();//山西挂号网传递过来的加密head
        }
        if (null == code || code == 0) {//校验失败，直接返回，这时候失败的日志在baseController中已经写了
            Object object = resultMap.get("msg");
            HisResponse hr = new HisResponse(decodeHead, JSONObject.toJSON(object));
            return hr;
        }
        String head = resultMap.get("head").toString();
        String body = resultMap.get("body").toString();
        String id = resultMap.get("id").toString();//获取日志表中的主键（uuid）
        String interfaceName = resultMap.get("interfaceName").toString();//获取接口参数
        //执行业务
        JSONObject json = JSON.parseObject(body);
        List<String> params = new ArrayList<String>();
        String charge_no = json.getString("charge_no");//申请单号
        params.add(charge_no);
        String apply_opera = json.getString("apply_opera");//申请人
        params.add(apply_opera);
        log.info("参数");
        log.info("通知医院HIS同步体检患者基本信息basic/visitsave接口入参：" + params);
        Map<String, Object> bodyMap = new HashMap<String, Object>();
        String message = "";
        String logbody = "";
        try {
            String hsql = "exec p_wa_charge_back '" + charge_no + "','" + apply_opera + "'";

            log.info(hsql);
            Map<String, Object> mess = tkmapper.callPronamesql(hsql);
            bodyMap.put("code", 1);
            bodyMap.put("msg", "检查申请取消");
            bodyMap.put("items", mess);
            logbody = bodyMap.toString();
        } catch (Exception e) {
            bodyMap.put("code", 0);
            bodyMap.put("msg", e.getMessage());
            message = e.toString().replace("'", "''") + ":" + YuYueUtil.getStackMsg(e).replace("'", "'");
            logbody = bodyMap.toString().replace("'", "*");
            e.printStackTrace();
            e.printStackTrace();
        } finally {
            HisResponse hr = new HisResponse(decodeHead, bodyMap);
            return hr;
        }
    }

    //撤销收费申请
    @RequestMapping("/app/charge/cancel")
    @ResponseBody
    public HisResponse chargeCancel(HttpServletRequest request) throws Exception {
        Map<String, Object> resultMap = getResultMapDebug(request, deBug);//分解参数、校验参数
        Integer code = (Integer) resultMap.get("code");//获取校验成功失败的标记
        String decodeHead = "";
        if (null != resultMap.get("decodeHead")) {
            decodeHead = resultMap.get("decodeHead").toString();//山西挂号网传递过来的加密head
        }
        if (null == code || code == 0) {//校验失败，直接返回，这时候失败的日志在baseController中已经写了
            Object object = resultMap.get("msg");
            HisResponse hr = new HisResponse(decodeHead, JSONObject.toJSON(object));
            return hr;
        }
        String head = resultMap.get("head").toString();
        String body = resultMap.get("body").toString();
        String id = resultMap.get("id").toString();//获取日志表中的主键（uuid）
        String interfaceName = resultMap.get("interfaceName").toString();//获取接口参数
        //执行业务
        JSONObject json = JSON.parseObject(body);
        List<String> params = new ArrayList<String>();
        String charge_no = json.getString("charge_no");//申请单号
        params.add(charge_no);
        String apply_opera = json.getString("apply_opera");//申请人
        params.add(apply_opera);
        log.info("参数");
        log.info("通知医院HIS同步体检患者基本信息basic/visitsave接口入参：" + params);
        Map<String, Object> bodyMap = new HashMap<String, Object>();
        String message = "";
        String logbody = "";
        try {
            String hsql = "exec p_wa_charge_cancel '" + charge_no + "','" + apply_opera + "'";
            log.info(hsql);
            Map<String, Object> mess = tkmapper.callPronamesql(hsql);
            bodyMap.put("code", 1);
            bodyMap.put("msg", "检查申请取消");
            bodyMap.put("items", mess);
            logbody = bodyMap.toString();
        } catch (Exception e) {
            bodyMap.put("code", 0);
            bodyMap.put("msg", e.getMessage());
            message = e.toString().replace("'", "''") + ":" + YuYueUtil.getStackMsg(e).replace("'", "'");
            logbody = bodyMap.toString().replace("'", "*");
            e.printStackTrace();
            e.printStackTrace();
        } finally {
            HisResponse hr = new HisResponse(decodeHead, bodyMap);
            return hr;
        }
    }

}
