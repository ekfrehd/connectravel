package com.connectravel.service.impl;

import com.connectravel.domain.entity.Resources;
import com.connectravel.repository.ResourcesRepository;
import com.connectravel.service.ResourcesService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Log4j2
public class ResourcesServiceImpl implements ResourcesService {

    @Autowired
    private ResourcesRepository ResourcesRepository;

    @Transactional
    public Resources getResources(long id) {
        return ResourcesRepository.findById(id).orElse(new Resources());
    }

    @Transactional
    public List<Resources> getResources() {
        return ResourcesRepository.findAll(Sort.by(Sort.Order.asc("orderNum")));
    }

    @Transactional
    public void createResources(Resources resources){
        ResourcesRepository.save(resources);
    }

    @Transactional
    public void deleteResources(long id) {
        ResourcesRepository.deleteById(id);
    }

}