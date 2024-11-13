package com.kpi.routetracker.repo;

import com.kpi.routetracker.model.user.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

  Optional<Account> getByPhoneNumber(String phoneNumber);

}
