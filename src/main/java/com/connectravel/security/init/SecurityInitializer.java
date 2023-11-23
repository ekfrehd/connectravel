package com.connectravel.security.init;

import com.connectravel.service.SecurityResourceService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Log4j2
public class SecurityInitializer implements ApplicationRunner {

    @Autowired
    private SecurityResourceService securityResourceService;

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        securityResourceService.setRoleHierarchy();
    }

}