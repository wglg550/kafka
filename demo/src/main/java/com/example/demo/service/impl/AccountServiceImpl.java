package com.example.demo.service.impl;

import com.example.demo.dao.AccountDao;
import com.example.demo.entity.Account;
import com.example.demo.service.AccountService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
public class AccountServiceImpl implements AccountService {

    @Resource
    private AccountDao accountDao;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void transfer(int outter, int inner, Integer money) {
        accountDao.moveOut(outter, money); //转出
        int i = 1 / 0;
        accountDao.moveIn(inner, money); //转入
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(Account account) {
        accountDao.save(account.getName(), account.getBalance());
        int i = 1 / 0;
    }
}
