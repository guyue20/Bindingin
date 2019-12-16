package com.bdyx.service;

import com.bdyx.mapper.Tkmapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class CommonService {
    protected final SimpleDateFormat datefmtForYMDHms = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @Autowired
    Tkmapper tkmapper;

    //初始化日志表V1
    public void insertYyptHappenTime(String id, String interfaceName) {
        //insert into physical_params_log_table(id,happen_time,interface_name)
        //values(/*paramsBean.id*/,/*paramsBean.happenTime*/,/*paramsBean.interfaceName*/)
        try {
            String format = datefmtForYMDHms.format(new Date());
            String sql = "insert into physical_params_log_table(id,happen_time,interface_name) values('" + id + "','" + format + "','" + interfaceName + "')";
            tkmapper.allselectandupdate(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //更新日志表V1
    public void insertYyptParams(String id, String reqHead, String reqBody, String respHead, String respBody,
                                 String exMess, String interfaceName) {
//        update physical_params_log_table
//        set
//        request_data_head=/*paramsBean.requestDataHead*/,
//                request_data_body=/*paramsBean.requestDataBody*/,
//                response_data_head=/*paramsBean.responseDataHead*/,
//                response_data_body=/*paramsBean.responseDataBody*/,
//                response_time=/*paramsBean.responseTime*/,
//                exceptionMess=/*paramsBean.exceptionMess*/,
//                interface_name=/*paramsBean.interfaceName*/
//                        where id=/*paramsBean.id*/
        try {
            String sql = "update physical_params_log_table "
                    + "set"
                    + "request_data_head='" + reqHead + "',"
                    + "request_data_body='" + reqBody + "',"
                    + "response_data_head='" + respHead + "',"
                    + "response_data_body='" + respBody + "',"
                    + "response_time='" + datefmtForYMDHms.format(new Date()) + "',"
                    + "exceptionMess='" + exMess + "',"
                    + "interface_name='" + interfaceName + "'"
                    + "where id='" + id + "'";
            tkmapper.allselectandupdate(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
