package com.example.demo.service;

import com.example.demo.entity.Account;

public interface AccountService {
    public void transfer(int outter, int inner, Integer money);

    public void save(Account account);
}