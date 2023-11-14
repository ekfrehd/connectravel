package com.connectravel.service.impl;

import com.connectravel.constant.ReservationStatus;
import com.connectravel.domain.dto.MemberDTO;
import com.connectravel.domain.dto.ReservationDTO;
import com.connectravel.domain.dto.RoomDTO;
import com.connectravel.domain.entity.Member;
import com.connectravel.domain.entity.Reservation;
import com.connectravel.domain.entity.Room;
import com.connectravel.exception.EntityNotAvailableException;
import com.connectravel.repository.MemberRepository;
import com.connectravel.repository.ReservationRepository;
import com.connectravel.repository.RoomRepository;
import com.connectravel.service.MemberService;
import com.connectravel.service.ReservationService;
import com.connectravel.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;

    private final RoomRepository roomRepository;

    private final MemberRepository memberRepository;

    private final RoomService roomService;

    private final MemberService memberService;

    @Override
    @Transactional
    public ReservationDTO bookRoom(ReservationDTO reservationDTO) {

        // 예약하기 전에 원하는 날짜에 방이 이용 가능한지 확인합니다.
        if (!checkRoomAvailability(
                reservationDTO.getRoomDTO().getRno(),
                reservationDTO.getStartDate(),
                reservationDTO.getEndDate())) {

            throw new EntityNotAvailableException("선택한 날짜에 방이 이용 불가능합니다.");
        }

        // 예약 객체 생성 전 상태를 ACTIVE로 설정
        reservationDTO.setStatus(ReservationStatus.ACTIVE);

        Reservation reservation = dtoToEntity(reservationDTO);
        Reservation savedReservation = reservationRepository.save(reservation);

        return entityToDTO(savedReservation);
    }


    @Override
    @Transactional(readOnly = true)
    public ReservationDTO getRoomBookingDetails(Long rvno) {

        // 예약 엔티티를 ID를 이용해 조회
        Reservation reservation = reservationRepository.findById(rvno)
                .orElseThrow(() -> new EntityNotFoundException("해당 예약을 찾을 수 없습니다. 예약 번호: " + rvno));

        // 조회된 엔티티를 DTO로 변환하여 반환
        return entityToDTO(reservation);
    }

    @Override
    @Transactional
    public ReservationDTO modifyRoomBooking(Long rvno, ReservationDTO reservationDTO) {

        Reservation reservation = reservationRepository.findById(rvno)
                .orElseThrow(() -> new EntityNotFoundException("해당 예약을 찾을 수 없습니다. 예약 번호: " + rvno));

        if (!reservation.getStartDate().isEqual(reservationDTO.getStartDate()) ||
                !reservation.getEndDate().isEqual(reservationDTO.getEndDate())) {
            if (!checkRoomAvailability(reservation.getRoom().getRno(), reservationDTO.getStartDate(), reservationDTO.getEndDate())) {
                throw new EntityNotAvailableException("새로운 날짜에 방이 이용 불가능합니다.");
            }
        }

        reservation.setMessage(reservationDTO.getMessage());
        reservation.setMoney(reservationDTO.getMoney());
        reservation.setNumberOfGuests(reservationDTO.getNumberOfGuests());
        reservation.setStartDate(reservationDTO.getStartDate());
        reservation.setEndDate(reservationDTO.getEndDate());
        reservation.setStatus(reservationDTO.getStatus()); // 상태 업데이트 부분 변경

        // 업데이트된 예약 저장
        Reservation updatedReservation = reservationRepository.save(reservation);

        // 업데이트된 예약을 DTO로 변환하여 반환
        return entityToDTO(updatedReservation);
    }

    @Override
    @Transactional
    public boolean requestCancel(Long rvno, String userEmail) {

        // 예약 정보를 ID를 통해 조회
        Reservation reservation = reservationRepository.findById(rvno)
                .orElseThrow(() -> new EntityNotFoundException("예약 정보를 찾을 수 없습니다. 예약 ID: " + rvno));

        // 예약한 사용자 이메일이 요청한 이메일과 같은지 확인
        if (!reservation.getMember().getEmail().equals(userEmail)) {
            throw new SecurityException("자신의 예약만 취소 요청을 할 수 있습니다.");
        }

        // 예약 상태가 활성화된 상태인지 확인 후 취소 요청 상태로 변경
        if (reservation.getStatus() == ReservationStatus.ACTIVE) {
            reservation.setStatus(ReservationStatus.CANCELLATION_REQUESTED);
            reservationRepository.save(reservation); // 변경된 예약 상태를 저장
            return true;
        }

        return false;
    }

    @Override
    @Transactional
    public boolean approveCancellation(Long rvno) {
        // 해당 메서드는 관리자나 숙박업소의 주인과 같은 적절한 권한을 가진 사람만 호출해야 합니다.

        // 예약 정보를 ID를 통해 조회
        Reservation reservation = reservationRepository.findById(rvno)
                .orElseThrow(() -> new EntityNotFoundException("예약 정보를 찾을 수 없습니다. 예약 ID: " + rvno));

        // 예약 상태가 취소 요청 중인지 확인 후 취소 상태로 변경
        if (reservation.getStatus() == ReservationStatus.CANCELLATION_REQUESTED) {
            reservation.setStatus(ReservationStatus.CANCELLED);
            reservationRepository.save(reservation); // 변경된 예약 상태를 저장
            return true;
        }

        return false;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservationDTO> listUserRoomBookings(String username) {
        List<Reservation> reservations = reservationRepository.findByMember_Username(username);
        return reservations.stream().map(this::entityToDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservationDTO> listAccommodationRoomBookings(Long ano) {
        List<Reservation> reservations = reservationRepository.findByRoomAccommodationAno(ano);
        return reservations.stream().map(this::entityToDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservationDTO> listRoomBookings(Long rno) {
        List<Reservation> reservations = reservationRepository.findByRoomRno(rno);
        return reservations.stream().map(this::entityToDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean checkRoomAvailability(Long rno, LocalDate startDate, LocalDate endDate) {
        // 방의 예약 가능 여부를 확인하기 위한 로직
        // 날짜 범위가 겹치는 기존 예약이 있는지 확인
        List<Reservation> existingReservations = reservationRepository.findAvailableReservations(rno, endDate, startDate);

        // 겹치는 예약이 있으면 방은 이용 불가능
        return existingReservations.isEmpty();
    }


    /* 변환 메서드 */
    private ReservationDTO entityToDTO(Reservation reservation) {

        RoomDTO roomDTO = roomService.entityToDTO(reservation.getRoom());
        MemberDTO memberDTO = memberService.entityToDTO(reservation.getMember()); // Member 변환

        return ReservationDTO.builder()
                .rvno(reservation.getRvno())
                .message(reservation.getMessage())
                .money(reservation.getMoney())
                .numberOfGuests(reservation.getNumberOfGuests())
                .startDate(reservation.getStartDate())
                .endDate(reservation.getEndDate())
                .status(reservation.getStatus()) // 상태 변환
                .roomDTO(roomDTO) // Room 엔티티를 DTO로 변환
                .memberDTO(memberDTO) // Member 엔티티를 DTO로 변환
                .build();
    }

    private Reservation dtoToEntity(ReservationDTO reservationDTO) {

        Room room = roomRepository.findById(reservationDTO.getRoomDTO().getRno())
                .orElseThrow(() -> new EntityNotFoundException("방을 찾을 수 없습니다."));

        Member member = memberRepository.findByEmail(reservationDTO.getMemberDTO().getEmail());
        if (member == null) {
            throw new EntityNotFoundException("회원을 찾을 수 없습니다.");
        }

        return Reservation.builder()
                .rvno(reservationDTO.getRvno()) // 예약 번호 추가
                .message(reservationDTO.getMessage())
                .money(reservationDTO.getMoney())
                .numberOfGuests(reservationDTO.getNumberOfGuests())
                .startDate(reservationDTO.getStartDate())
                .endDate(reservationDTO.getEndDate())
                .status(reservationDTO.getStatus()) // 상태 변환
                .room(room) // 여기서 Room 엔티티를 설정합니다.
                .member(member) // 여기서 Member 엔티티를 설정합니다.
                .build();
    }

}