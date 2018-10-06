package io.geewit.session.controller;

import io.geewit.core.Profiles;
import io.geewit.session.service.SessionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author geewit
 */
@Profile({Profiles.INTEGRATION})
@RequestMapping(value = {"/api/session"})
@RestController
public class SessionController {
    private final static Logger logger = LoggerFactory.getLogger(SessionController.class);

    private final SessionService sessionService;

    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    /**
     * 清理session
     *
     * @return
     */
    @RequestMapping(name = "清理session", value = "/clean", method = RequestMethod.GET)
    public String clean() {
        sessionService.cleanSession();
        return "清理成功";
    }
}
