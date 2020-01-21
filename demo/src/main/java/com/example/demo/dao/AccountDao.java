package com.example.demo.dao;

import org.apache.ibatis.annotations.Param;

public interface AccountDao {

    public void moveIn(@Param("id") int id, @Param("money") float money); // 转入

    public void moveOut(@Param("id") int id, @Param("money") float money); // 转出

    public void save(@Param("name") String name, @Param("balance") float balance);
}
