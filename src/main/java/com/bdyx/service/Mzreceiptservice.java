package com.bdyx.service;

import com.bdyx.entity.Mzreceipt;
import com.bdyx.entity.MzreceiptKey;
import com.bdyx.mapper.MzreceiptMapper;

public class Mzreceiptservice implements MzreceiptMapper {
    @Override
    public int deleteByPrimaryKey(MzreceiptKey key) {
        return 0;
    }

    @Override
    public int insert(Mzreceipt record) {
        return 0;
    }

    @Override
    public int insertSelective(Mzreceipt record) {
        return 0;
    }

    @Override
    public Mzreceipt selectByPrimaryKey(MzreceiptKey key) {
        return null;
    }

    @Override
    public int updateByPrimaryKeySelective(Mzreceipt record) {
        return 0;
    }

    @Override
    public int updateByPrimaryKey(Mzreceipt record) {
        return 0;
    }
}
