package io.geewit.session.service;

import io.geewit.core.Profiles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.session.data.redis.RedisOperationsSessionRepository;
import org.springframework.stereotype.Service;

/**
 * @author geewit
 */
@Profile({Profiles.INTEGRATION})
@Service
public class SessionService {
    private final static Logger logger = LoggerFactory.getLogger(SessionService.class);

    private final RedisOperationsSessionRepository sessionRepository;

    public SessionService(RedisOperationsSessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    public void cleanSession() {
        sessionRepository.cleanupExpiredSessions();
    }
}
