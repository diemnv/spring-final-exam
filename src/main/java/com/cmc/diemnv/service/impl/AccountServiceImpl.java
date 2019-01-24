package com.cmc.diemnv.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmc.diemnv.entity.Account;
import com.cmc.diemnv.repository.AccountRepository;

@Service
public class AccountServiceImpl {
	@Autowired
	private AccountRepository accountRepository;

	public boolean login(Account account) {
		List<Account> list = accountRepository.findByEmailAndPassword(account.getEmail(), account.getPassword());
		return list.size() != 0;
	}
}
