package com.village.bot.services;

import com.village.bot.repositories.RigDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
@Transactional
@RequiredArgsConstructor
public class DatabaseCleaner {
    private final RigDataRepository rigDataRepository;

    @Scheduled(cron = "@daily")
    public void deleteOldData() {
        rigDataRepository.deleteByDataTimeLessThan(LocalDateTime.now(ZoneOffset.UTC).minusDays(1));
    }
}
