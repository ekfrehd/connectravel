package com.connectravel.service;

import com.connectravel.domain.dto.ErrorResponse;
import com.connectravel.domain.dto.Response;
import com.connectravel.domain.dto.Review.ReviewResponse;
import com.connectravel.domain.dto.part.PartDto;
import com.connectravel.domain.dto.part.PartJoinDto;
import com.connectravel.domain.dto.part.PartJoinResponse;
import com.connectravel.domain.dto.part.PartResponse;
import com.connectravel.domain.entity.Crew;
import com.connectravel.domain.entity.Member;
import com.connectravel.domain.entity.Participation;
import com.connectravel.exception.AppException;
import com.connectravel.exception.ErrorCode;
import com.connectravel.repository.CrewRepository;
import com.connectravel.repository.MemberRepository;
import com.connectravel.repository.ParticipationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
@Log4j2
public class ParticipationService {
    private final ParticipationRepository participationRepository;
    private final MemberRepository userRepository;
    private final CrewRepository crewRepository;


    //참여 기능
    @Transactional
    public Response participate(PartJoinDto partJoinDto) {
        Crew crew = crewRepository.findById(partJoinDto.getCrewId()).orElseThrow(() -> new AppException(ErrorCode.CREW_NOT_FOUND, ErrorCode.CREW_NOT_FOUND.getMessage()));
        Member user = userRepository.findByUsername(partJoinDto.getUserName()).orElseThrow(() -> new AppException(ErrorCode.USERID_NOT_FOUND, ErrorCode.USERID_NOT_FOUND.getMessage()));

        int size = 0;
        for (Participation participation : crew.getParticipations()) {
            if (participation.getStatus() == 2) {
                size++;
            }
        }

        //같거나 클 경우에는 참여 못함
        if(size >= crew.getCrewLimit()){
            return Response.error(new  ErrorResponse(ErrorCode.NOT_ALLOWED_PARTICIPATION,ErrorCode.NOT_ALLOWED_PARTICIPATION.getMessage()));
        }

        Participation participation = participationRepository.findByCrewAndUser(crew, user).orElseThrow(() -> new AppException(ErrorCode.DB_ERROR, ErrorCode.DB_ERROR.getMessage()));
        participation.setStatus(2);
        //sse 로직
//        if (sseEmitters.containsKey(user.getUsername())) {
//            SseEmitter sseEmitter = sseEmitters.get(user.getUsername());
//            try {
//                sseEmitter.send(SseEmitter.event().name("alarm").data(
//                        crew.getTitle() + "모임에 참여신청이 수락되었습니다🔥 채팅방에서 인사를 건네보세요!"));
//            } catch (Exception e) {
//                sseEmitters.remove(user.getUsername());
//            }
//        }
        return Response.success("참여하기 성공");
    }

    @Transactional
    public Response reject(PartJoinDto partJoinDto) {
        Crew crew = crewRepository.findById(partJoinDto.getCrewId()).orElseThrow(() -> new AppException(ErrorCode.CREW_NOT_FOUND, ErrorCode.CREW_NOT_FOUND.getMessage()));
        Member user = userRepository.findByUsername(partJoinDto.getUserName()).orElseThrow(() -> new AppException(ErrorCode.USERID_NOT_FOUND, ErrorCode.USERID_NOT_FOUND.getMessage()));

        Participation participation = participationRepository.findByCrewAndUser(crew, user).orElseThrow(() -> new AppException(ErrorCode.DB_ERROR, ErrorCode.DB_ERROR.getMessage()));
        participationRepository.delete(participation);
        //hard Delete
        return Response.success("신청이 취소됨");
    }

    @Transactional
    public Response generatePart(PartDto partDto, String userName) {
        Member user = userRepository.findByUsername(userName).orElseThrow(() -> new AppException(ErrorCode.USERID_NOT_FOUND, ErrorCode.USERID_NOT_FOUND.getMessage()));
        Crew crew = crewRepository.findById(partDto.getCrewId()).orElseThrow(() -> new AppException(ErrorCode.CREW_NOT_FOUND, ErrorCode.CREW_NOT_FOUND.getMessage()));

        if(crew.getFinish() == 1){
            return Response.success("이미 종료된 모임입니다");
        }

        if (participationRepository.existsByCrewAndAndUser(crew, user)) {
            Participation participation = participationRepository.findByCrewAndUser(crew, user).orElseThrow(() -> new AppException(ErrorCode.DB_ERROR, ErrorCode.DB_ERROR.getMessage()));
            participationRepository.delete(participation);
            return Response.success("이미 존재하는 참여 엔티티 취소됨");
        }
        Participation savedParticipation = Participation.builder().crew(crew).title(crew.getTitle()).body(partDto.getBody()).user(user).status(1).build();
        Crew userCrew = crewRepository.findByUserAndAndId(user, crew.getId());

        if (userCrew != null) {
            // 사용자가 해당 크루에 이미 참여 중인 경우
            savedParticipation = Participation.builder()
                    .crew(crew)
                    .title(crew.getTitle())
                    .body(partDto.getBody())
                    .user(user)
                    .status(2)  // 이미 참여 중인 경우 상태를 2로 설정
                    .build();
        } else {
            // 사용자가 해당 크루에 참여 중이지 않은 경우
            savedParticipation = Participation.builder()
                    .crew(crew)
                    .title(crew.getTitle())
                    .body(partDto.getBody())
                    .user(user)
                    .status(1)  // 참여 요청 제출된 경우 상태를 1로 설정
                    .build();
        }
        participationRepository.save(savedParticipation);
        //sse 로직
//        if (sseEmitters.containsKey(crew.getUser().getUsername())) {
//            SseEmitter sseEmitter = sseEmitters.get(crew.getUser().getUsername());
//            try {
//                sseEmitter.send(SseEmitter.event().name("alarm").data(
//                        user.getNickName() + "님이 모임 참여신청을 했습니다🔥" +
//                                "crew 참여신청 내역을 확인해주세요!"));
//            } catch (Exception e) {
//                sseEmitters.remove(crew.getUser().getUsername());
//            }
//        }
        return Response.success("참여하기 동작");
    }
//
    //참여유무확인
    @Transactional
    public PartResponse findParticipate(Long crewId, String userName) {
        Member user = userRepository.findByUsername(userName).orElseThrow(() -> new AppException(ErrorCode.USERID_NOT_FOUND, ErrorCode.USERID_NOT_FOUND.getMessage()));
        Crew crew = crewRepository.findById(crewId).orElseThrow(() -> new AppException(ErrorCode.CREW_NOT_FOUND, ErrorCode.CREW_NOT_FOUND.getMessage()));

        if (!participationRepository.existsByCrewAndAndUser(crew, user)) {
            return PartResponse.builder().status(0).build();
        }
        Participation participation = participationRepository.findByCrewAndUser(crew, user).orElseThrow(() -> new AppException(ErrorCode.DB_ERROR, ErrorCode.DB_ERROR.getMessage()));

        return PartResponse.builder()
                .now(crew.getParticipations().size())
                .limit(crew.getCrewLimit())
                .status(participation.getStatus())
                .build();
    }


    //현재 크루 참여자 수 확인
    @Transactional
    public PartResponse findCrewInfo(Long crewId) {
        Crew crew = crewRepository.findById(crewId).orElseThrow(() -> new AppException(ErrorCode.CREW_NOT_FOUND, ErrorCode.CREW_NOT_FOUND.getMessage()));
        int size = 0;
        for (Participation p : crew.getParticipations()) {
            if (p.getStatus() == 2 || p.getStatus() == 3) {
                size++;
            }
        }
        return PartResponse.builder().now(size).build();
    }


    //미승인된 멤버 조회
    @Transactional
    public List<PartJoinResponse> notAllowedMember(String userName) {
        Member user = userRepository.findByUsername(userName).orElseThrow(() -> new AppException(ErrorCode.USERID_NOT_FOUND, ErrorCode.USERID_NOT_FOUND.getMessage()));
        List<PartJoinResponse> participations = new ArrayList<>();
        // 사용자가 생성한 Crew 목록을 가져옵니다.
        List<Crew> userCrews = crewRepository.findCrewsByUserUsername(userName);

        for (Crew crew : userCrews) {
            for (Participation participation : crew.getParticipations()) {
                if (participation.getStatus() == 1 && participation.getDeletedAt() == null) {
                    participations.add(PartJoinResponse.builder()
                            .crewId(participation.getCrew().getId())
                            .body(participation.getBody())
                            .title(participation.getTitle())
                            .joinUserName(participation.getUser().getUsername())
                            .writerUserName(participation.getCrew().getUser().getUsername())
                            .crewTitle(participation.getCrew().getTitle())
                            .status(participation.getStatus()).build());
                }
            }
        }
        return participations;
    }

    //승인된 멤버 조회
    @Transactional
    public List<PartJoinResponse> AllowedMember(long crewId) {
        Crew crew = crewRepository.findById(crewId).orElseThrow(() -> new AppException(ErrorCode.CREW_NOT_FOUND, ErrorCode.CREW_NOT_FOUND.getMessage()));
        List<PartJoinResponse> list = new ArrayList<>();
        for (Participation p : crew.getParticipations()) {

            if ((p.getStatus() == 2 || p.getStatus() == 3) && p.getDeletedAt() == null) {
                PartJoinResponse partJoinResponse = PartJoinResponse.builder()
                        .crewTitle(crew.getTitle())
                        .crewId(crewId)
                        .status(p.getStatus())
                        .writerUserName(crew.getUser().getUsername())
                        .joinUserName(p.getUser().getUsername())
                        .joinUserId(p.getUser().getId())
                        .writerUserNickName(crew.getUser().getNickName())
                        .joinUserNickName(p.getUser().getNickName())
                        .now(crew.getParticipations().size())
                        .limit(crew.getCrewLimit())
                        .build();
                list.add(partJoinResponse);
            }
        }
        return list;
    }

//    승인된 멤버 조회 return List<ReviewResponse>
    @Transactional
    public List<ReviewResponse> findAllPartMember(long crewId) {
        System.out.println("findAllPartMember crewId: "+crewId);
        Crew crew = crewRepository.findById(crewId).orElseThrow(() -> new AppException(ErrorCode.CREW_NOT_FOUND, ErrorCode.CREW_NOT_FOUND.getMessage()));
        List<ReviewResponse> list = new ArrayList<>();
        for (Participation p : crew.getParticipations()) {
            if (p.getStatus() == 2 || p.getStatus() == 3) {
                ReviewResponse reviewResponse = ReviewResponse.builder()
                        .crewId(crewId)
                        .joinUserId(p.getUser().getId())
                        .joinUserNickName(p.getUser().getNickName())
                        .userName(p.getUser().getUsername())
//                        .userMannerScore(p.getUser().getMannerScore())
//                        .sports(p.getUser().getSport().toList())
                        .build();
                list.add(reviewResponse);
            }
        }
        System.out.println("findAllPartMember crewId: "+crewId);
        return list;
    }

    @Transactional
    public boolean isPartUser(long crewId, Member user) {
        Crew crew = crewRepository.findById(crewId).orElseThrow(() -> new AppException(ErrorCode.CREW_NOT_FOUND, ErrorCode.CREW_NOT_FOUND.getMessage()));
        for (Participation p : crew.getParticipations()) {
            if ((p.getStatus() == 2 || p.getStatus() ==3) && p.getUser().getId() == user.getId()) {
                return true;
            }
        }
        return false;
    }

    @Transactional
    public String finishPart(Long crewId){
        Crew crew = crewRepository.findById(crewId).orElseThrow(() -> new AppException(ErrorCode.CREW_NOT_FOUND, ErrorCode.CREW_NOT_FOUND.getMessage()));
        for(Participation p : crew.getParticipations()){
            p.setStatus(3);
        }
        return "status 변경완료";
    }
}
