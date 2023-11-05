package com.connectravel.config;

import com.connectravel.repository.AccessIpRepository;
import com.connectravel.repository.ResourcesRepository;
import com.connectravel.security.configs.MethodSecurityConfig;
import com.connectravel.service.RoleHierarchyService;
import com.connectravel.service.SecurityResourceService;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;

@Configuration
@AutoConfigureBefore({MethodSecurityConfig.class})
public class AppConfig {

    @Bean
    public SecurityResourceService securityResourceService(ResourcesRepository resourcesRepository, RoleHierarchyImpl roleHierarchy, RoleHierarchyService roleHierarchyService, AccessIpRepository accessIpRepository) {
        SecurityResourceService SecurityResourceService = new SecurityResourceService(resourcesRepository, roleHierarchy, roleHierarchyService, accessIpRepository);

        return SecurityResourceService;
    }

}