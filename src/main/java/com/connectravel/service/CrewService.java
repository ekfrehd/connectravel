package com.connectravel.service;


import com.connectravel.constant.SportEnum;
import com.connectravel.domain.dto.crew.CrewDetailResponse;
import com.connectravel.domain.dto.crew.CrewRequest;
import com.connectravel.domain.dto.crew.CrewResponse;
import com.connectravel.domain.dto.crew.CrewSportRequest;
import com.connectravel.domain.entity.Accommodation;
import com.connectravel.domain.entity.Crew;
import com.connectravel.domain.entity.Member;
import com.connectravel.domain.entity.Participation;
import com.connectravel.exception.AppException;
import com.connectravel.exception.ErrorCode;
import com.connectravel.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CrewService {

    private final CrewRepository crewRepository;
    private final MemberRepository userRepository;
    private final ParticipationRepository participationRepository;
    private final ReservationRepository reservationRepository;
    private final AccommodationRepository accommodationRepository;
//    private final AlarmRepository alarmRepository;


    // 크루 게시글 등록
    public CrewResponse addCrew(CrewRequest crewRequest, String userName) {

        log.info("imagePath: {} ", crewRequest.getImagePath());
        log.info("datePick: {}", crewRequest.getDatepick());

        Member user = findByUserName(userName);



        Accommodation accommodation = accommodationRepository.findByAno(crewRequest.getAccommodationAno());
        Crew crew = crewRepository.save(crewRequest.toEntity(user,accommodation));

        return new CrewResponse("Crew 등록 완료", crew.getId());
    }
//
    // 크루 게시글 수정
    public CrewResponse modifyCrew(Long crewId, CrewRequest crewRequest, String userName) {

        Member user = findByUserName(userName);
        Crew crew = findByCrewId(crewId);
        findByUserAndCrewContaining(user, crew);

        crew.of(crewRequest);
        crewRepository.save(crew);

        return new CrewResponse("Crew 수정 완료", crewId);
    }

    // 크루 게시글 삭제
    @Transactional
    public CrewResponse deleteCrew(Long crewId, String userName) {

        Member user = findByUserName(userName);
        Crew crew = findByCrewId(crewId);
        findByUserAndCrewContaining(user, crew);

        crew.deleteSoftly(LocalDateTime.now());
        crewRepository.delete(crew);

        return new CrewResponse("Crew 삭제 완료", crewId);
    }

    @Transactional
    public CrewResponse finishCrew(Long crewId, String userName) {
        Member user = findByUserName(userName);
        Crew crew = findByCrewId(crewId);
        findByUserAndCrewContaining(user, crew);

        List<Participation> participations = crew.getParticipations();
        List<String> userList = new ArrayList<>();
        for (Participation participation : participations) {
            userList.add(participation.getUser().getUsername());
//            alarmRepository.save(Alarm.toEntityFromFinishCrew(participation.getUser(), crew, AlarmType.LIKE_COMMENT, "모임이 종료되었습니다."));
            log.info("모임 종류 후 username입니다 = {}",participation.getUser().getUsername());
        }
        //sse 로직
//        for (int i = 0; i < userList.size(); i++) {
//            if (sseEmitters.containsKey(userList.get(i))) {
//                log.info("모임 종료 후 작동");
//                SseEmitter sseEmitter = sseEmitters.get(userList.get(i));
//                try {
//                    sseEmitter.send(SseEmitter.event().name("alarm").data(
//                            "모임이 종료되었습니다! 같이 고생한 크루들에게 후기를 남겨보세요!"));
//                } catch (Exception e) {
//                    sseEmitters.remove(userList.get(i));
//                }
//            }
//        }

        crew.setFinish(1);
        return new CrewResponse("Crew 상태변경 완료", crewId);
    }
//
//
    // 크루 게시물 상세 조회
    public CrewDetailResponse detailCrew(Long crewId) {

//        Member user = findByUserName(userName);
        Crew crew = findByCrewId(crewId);

        return CrewDetailResponse.of(crew);
    }

    // 크루 게시물 전체조회, 지역조회, 운동종목 조회
//    @Transactional
    public Page<CrewDetailResponse> findAllCrewsByStrictAndSportEnum2(CrewSportRequest crewSportRequest,  Pageable pageable) {

        //지역검색 null 확인
        if (crewSportRequest.getStrict() != null) log.info("!!!!!!!!!!!!!!!!!InService strict is not null");
        else crewSportRequest.setStrict("");
        log.info("!!!!!!!!!!!!!!!!!InService strict2 : {}", crewSportRequest.getStrict());

        // 운동종목 비선택
        if (CollectionUtils.isEmpty(crewSportRequest.getSportsList())) {
            return findAllCrewsByStrict(crewSportRequest, pageable);
        }
        // 운동종목 선택
        else {
            List<SportEnum> sportEnums = new ArrayList<>();
            for (String s : crewSportRequest.getSportsList()) sportEnums.add(SportEnum.valueOf(s));

            log.info("!!!!!!!!!!!!!!!!!SportEnums : {}", sportEnums);
            return crewRepository.findByStrictContainsAndSportEnumIn(crewSportRequest.getStrict(), sportEnums, pageable).map(CrewDetailResponse::of);
        }

    }
//
//
    // 크루 게시물 전체 조회
    @Transactional
    public Page<CrewDetailResponse> findAllCrews(Pageable pageable) {

        Page<Crew> crews = crewRepository.findAll(pageable);

        return crews.map(CrewDetailResponse::of);
    }
//
    // 크루 게시물 조회 By 지역 검색어
    @Transactional
    public Page<CrewDetailResponse> findByDeletedAtIsNullAndStrictContaining(CrewSportRequest crewSportRequest, Pageable pageable) {

        Page<Crew> crews = crewRepository.findByStrictContaining(pageable, crewSportRequest.getStrict());

        return crews.map(CrewDetailResponse::of);
    }
    @Transactional
    public Page<CrewDetailResponse> findAllCrewsByStrict(CrewSportRequest crewSportRequest, Pageable pageable) {

        Page<Crew> crews = crewRepository.findByStrictContaining(pageable, crewSportRequest.getStrict());

        return crews.map(CrewDetailResponse::of);
    }
//
//
    // 크루 게시물 조회 By 운동종목
    @Transactional
    public Page<CrewDetailResponse> findAllCrewsBySport(List<String> sportsList, Pageable pageable) {

        Page<Crew> crews;

        if (CollectionUtils.isEmpty(sportsList)) {
            crews = crewRepository.findAll(pageable);
        } else {
            SportEnum[] sports = new SportEnum[3];

            for (int i = 0; i < sportsList.size(); i++) {
                sports[i] = SportEnum.valueOf(sportsList.get(i)); // null
                log.info("Service sports List : {}", sports[i]);
            }

            crews = crewRepository.findBySportEnum(pageable, sports[0], sports[1], sports[2]);
        }
        return crews.map(CrewDetailResponse::of);
    }
//
    // User 존재 확인
    public Member findByUserName(String userName) {
        return userRepository.findByUsername(userName)
                .orElseThrow(() -> new AppException(ErrorCode.USERID_NOT_FOUND, ErrorCode.USERID_NOT_FOUND.getMessage()));
    }
//
    // 크루 게시글 존재 확인
    public Crew findByCrewId(Long crewId) {
        return crewRepository.findById(crewId)
                .orElseThrow(() -> new AppException(ErrorCode.CREW_NOT_FOUND, ErrorCode.CREW_NOT_FOUND.getMessage()));
    }
//
    // 해당 게시글 작성자 확인
    @Transactional
    public void findByUserAndCrewContaining(Member user, Crew crew) {


            if (!crew.getUser().equals(user)) {
                throw new AppException(ErrorCode.INVALID_PERMISSION, "해당 게시글에 접근 권한이 없습니다.");
            }



    }
//
//    // 유저 선호 운동종목 확인
//    @Transactional
//    public List<String> getUserSports(Authentication authentication, Boolean sportsListIsEmpty) {
//
//        List<String> userSportsList = new ArrayList<>();
//
//        if (authentication != null && sportsListIsEmpty) {
//
//            Member user = findByUserName(authentication.getName());
//
//            if (user.getSport().getSport1() != null) {
//                userSportsList.add(String.valueOf(user.getSport().getSport1()));
//            }
//            if (user.getSport().getSport2() != null) {
//                userSportsList.add(String.valueOf(user.getSport().getSport2()));
//            }
//            if (user.getSport().getSport3() != null) {
//                userSportsList.add(String.valueOf(user.getSport().getSport3()));
//            }
//
//
//        }
//        return userSportsList;
//    }
//
//    // 유저 등록된 지역 확인
//    public String getUserStrict(Authentication authentication) {
//
//        String strict = "";
//        if (authentication != null) {
//            strict = findByUserName(authentication.getName()).getAddress();
//            if (strict != null && strict.length() > 2)
//                strict = strict.split(" ")[0].substring(0, 2);
//        }
//        return strict;
//    }
//
//    @Transactional
//    public void readAlarms(Long crewId, String username) {
//        Member user = userRepository.findByUserName(username).orElseThrow(() -> new AppException(ErrorCode.USERID_NOT_FOUND, ErrorCode.USERID_NOT_FOUND.getMessage()));
//        List<Alarm> alarms = user.getAlarms();
//        for (Alarm alarm : alarms) {
//            boolean readOrNot = alarm.getReadOrNot();
//            if (alarm.getTargetId() == crewId && !readOrNot) {
//                alarm.setReadOrNot();
//                log.info("알람을 읽었습니다 : {}        알림 : {}", alarm.getId(), alarm.getReadOrNot());
//            }
//        }
//    }
//
//    @Transactional
//    public void readAlarmsReview(Long reviewId, String username) {
//        Member user = userRepository.findByUserName(username).orElseThrow(() -> new AppException(ErrorCode.USERID_NOT_FOUND, ErrorCode.USERID_NOT_FOUND.getMessage()));
//        List<Alarm> alarms = user.getAlarms();
//        for (Alarm alarm : alarms) {
//            boolean readOrNot = alarm.getReadOrNot();
//            if (alarm.getTargetId() == reviewId && !readOrNot) {
//                alarm.setReadOrNot();
//                log.info("알람을 읽었습니다 : {}        알림 : {}", alarm.getId(), alarm.getReadOrNot());
//            }
//        }
//    }
//
//
    // 내가 참여한 crew list
    public Page<CrewDetailResponse> findAllCrew(Integer status, String userName, Pageable pageable) {
        Member user = userRepository.findByUsername(userName).orElse(null);
        List<Participation> participations = participationRepository.findByStatusAndUser(status, user);
        Page<Crew> crewList = crewRepository.findByAndParticipationsIn(participations, pageable);
        return crewList.map(CrewDetailResponse::of);
    }
//
    // 내가 참여한 crew list
    public long getCrewByUserAndStatus(Integer status, String userName) {
        Member user = userRepository.findByUsername(userName).orElse(null);
        List<Participation> participations = participationRepository.findByStatusAndUser(status, user);
        return crewRepository.countByAndParticipationsIn(participations);
    }
//
//    // 강퇴하면 Participation을 지우는 방법 이용
    @Transactional
    public void deleteUserAtCrew(Long userId, Long crewId, String authenticationName) {

        Optional<Member> actingUserOptional = userRepository.findByUsername(authenticationName);

        Optional<Crew> crewOptional = crewRepository.findById(crewId);

        Optional<Member> userOptional = userRepository.findById(userId);

        if (actingUserOptional.isEmpty()) {
            throw new AppException(ErrorCode.FORBIDDEN_REQUEST, ErrorCode.FORBIDDEN_REQUEST.getMessage());
        }

        if (crewOptional.isEmpty()) {
            throw new AppException(ErrorCode.CREW_NOT_FOUND, ErrorCode.CREW_NOT_FOUND.getMessage());
        }

        if (userOptional.isEmpty()) {
            throw new AppException(ErrorCode.USERID_NOT_FOUND, ErrorCode.USERID_NOT_FOUND.getMessage());
        }

        Member user = userOptional.get();

        Member actingUser = actingUserOptional.get();

        Crew crew = crewOptional.get();

        Optional<Participation> participationOptional = participationRepository.findByCrewAndUser(crew, user);

        if (participationOptional.isEmpty()) {
            throw new AppException(ErrorCode.NOT_FOUND_PARTICIPATION, ErrorCode.NOT_FOUND_PARTICIPATION.getMessage());
        }

        Participation befParticipation = participationOptional.get();

//        // 방장 강퇴 못하도록 막음
//        if (befParticipation.getUser().getId().equals(actingUser.getId())) {
//            throw new AppException(ErrorCode.NOT_AUTHORIZED, "방장은 강퇴할 수 없습니다.");
//        } else {
//            participationRepository.delete(befParticipation);
//        }

        participationRepository.delete(befParticipation);

//
//         방장이거나 운영자인 경우만 삭제할 수 있는 기능 주석처리
//        if((actingUser.getRole() == UserRole.ROLE_ADMIN) || (crew.getUser().getId().equals(actingUser.getId()))){
//            Optional<Participation> participationOptional = participationRepository.findByCrewAndUser(crew, user);
//
//            if(participationOptional.isEmpty()){
//                throw new AppException(ErrorCode.NOT_FOUND_PARTICIPATION, ErrorCode.NOT_FOUND_PARTICIPATION.getMessage());
//            }
//
//            Participation befParticipation = participationOptional.get();
//
//            if(befParticipation.getUser().getId().equals(actingUser.getId())){
//                throw new AppException(ErrorCode.NOT_AUTHORIZED, "방장은 강퇴할 수 없습니다.");
//            } else{
//
//            }
//
//
//        }else{
//            throw new AppException(ErrorCode.NOT_AUTHORIZED, ErrorCode.NOT_AUTHORIZED.getMessage());
        }

    }
//
//
//}
