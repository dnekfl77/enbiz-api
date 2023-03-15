package com.enbiz.api.common.app.repository.replica.account;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import com.enbiz.api.common.app.entity.Account;
import com.enbiz.api.common.app.entity.criteria.AccountCriteria;

@Repository
@Lazy
public interface AccountMapper {

	Account findByCriteria(AccountCriteria criteria);
}
