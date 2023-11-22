package com.connectravel.service;


import com.connectravel.domain.dto.UserManageResponse;
import com.connectravel.domain.dto.crew.CrewManageResponse;
import com.connectravel.domain.entity.Crew;
import com.connectravel.domain.entity.Member;
import com.connectravel.repository.CrewRepository;
import com.connectravel.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ManagerService {

    private final MemberRepository userRepository;
    private final CrewRepository crewRepository;

    public Page<UserManageResponse> getUsersInfo(){
        List<Member> userList = userRepository.findAll();

        List<UserManageResponse> userManageResponses = userList.stream()
                .map(user -> UserManageResponse.fromEntity(user))
                .collect(Collectors.toList());

        Page<UserManageResponse> userManageResponsePage = new PageImpl<>(userManageResponses);


        return userManageResponsePage;
    }


    @Transactional
    public Page<CrewManageResponse> getCrewInfo(Pageable pageable){

        Page<Crew> crewPage = crewRepository.findByDeletedAtNull(pageable);


        log.info("전체 모임 size: {}", crewPage.getPageable().getPageSize());


        Page<CrewManageResponse> crewManageResponsePage = crewPage.map(crew -> CrewManageResponse.fromEntity(crew));


        return crewManageResponsePage;
    }






}
