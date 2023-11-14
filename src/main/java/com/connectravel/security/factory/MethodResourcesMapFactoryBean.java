package com.connectravel.security.factory;


import com.connectravel.service.SecurityResourceService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.security.access.ConfigAttribute;

import java.util.LinkedHashMap;
import java.util.List;

@Log4j2
public class MethodResourcesMapFactoryBean implements FactoryBean<LinkedHashMap<String, List<ConfigAttribute>>> {

    private SecurityResourceService securityResourceService;
    private String resourceType;

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public void setSecurityResourceService(SecurityResourceService securityResourceService) {
        this.securityResourceService = securityResourceService;
    }

    private LinkedHashMap<String, List<ConfigAttribute>> resourcesMap;

    public void init() {
        if ("method".equals(resourceType)) {
            resourcesMap = securityResourceService.getMethodResourceList();
        }else if ("pointcut".equals(resourceType)) {
            resourcesMap = securityResourceService.getPointcutResourceList();
        } else {
            log.error("resourceType must be 'method' or 'pointcut'");
        }
    }

    public LinkedHashMap<String, List<ConfigAttribute>> getObject() {
        if (resourcesMap == null) {
            init();
        }
        return resourcesMap;
    }

    @SuppressWarnings("rawtypes")
    public Class<LinkedHashMap> getObjectType() {
        return LinkedHashMap.class;
    }

    public boolean isSingleton() {
        return true;
    }

}