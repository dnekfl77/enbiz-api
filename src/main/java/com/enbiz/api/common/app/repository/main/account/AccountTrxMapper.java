package com.enbiz.api.common.app.repository.main.account;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import com.enbiz.api.common.app.entity.Account;

@Repository
@Lazy
public interface AccountTrxMapper {

	int save(Account account);
}
