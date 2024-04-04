package com.village.bot.services;

import com.village.bot.components.RigComponent;
import com.village.bot.repositories.ApplicationDataRepository;
import com.village.bot.components.Notificator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class RigDataRequester {
    private final ApplicationDataRepository applicationDataRepository;
    private final Notificator notificator;
    private final RigComponent rigComponent;

    @Scheduled(fixedDelay = 15000)
    public void startSurvey() {
        log.debug("Опросы");
        if (applicationDataRepository.isSurveyEnabled()) {
            rigComponent.getAndSaveRigData();
            notificator.notifyIfNeeded();
        }
    }
}
