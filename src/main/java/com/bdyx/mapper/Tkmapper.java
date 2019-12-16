package com.bdyx.mapper;

import com.bdyx.entity.Mzreceipt;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

import java.util.List;
import java.util.Map;

public interface Tkmapper extends Mapper<Mzreceipt>, MySqlMapper<Mzreceipt> {
    @Select("select * from mz_receipt where ledger_sn=${ledger_sn} and windows_no=${windows_no} and cash_opera=${cash_opera}")
    List<Mzreceipt> getList(@Param("ledger_sn") String ledger_sn, @Param("windows_no") String windows_no, @Param("cash_opera") String cash_opera);

    @Select({"exec ${proname} ${proparmls}"})
    Map<String, Object> callProname(@Param("proname") String proname, @Param("proparmls") String proparmls);

    @Select("${pronamesql}")
    Map<String, Object> callPronamesql(@Param("pronamesql") String pronamesql);

    @Select("${allselectandupdate}")
    void allselectandupdate(@Param("allselectandupdate") String allselectandupdate);

    @Select("${getprpString}")
    String getprpString(@Param("getprpString") String getprpString);
}
