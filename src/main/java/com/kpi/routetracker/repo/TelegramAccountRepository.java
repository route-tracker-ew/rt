package com.kpi.routetracker.repo;

import com.kpi.routetracker.model.user.TelegramAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TelegramAccountRepository extends JpaRepository<TelegramAccount, Long> {

    Optional<TelegramAccount> findByTelegramChatId(Long telegramChatId);

    @Query(nativeQuery = true, value = "SELECT telegram_account.telegram_chat_id from telegram_account join account a on a.id = telegram_account.account_id where phone_number =:phoneNumber")
    Long findChatIdByPhoneNumber(String phoneNumber);
}
