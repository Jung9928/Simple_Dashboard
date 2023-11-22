package com.jung9928.dashboardproject.repository;

import com.jung9928.dashboardproject.domain.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {
}
