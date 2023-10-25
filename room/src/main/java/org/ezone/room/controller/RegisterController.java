package org.ezone.room.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.ezone.room.repository.ReviewBoardRepository;
import org.ezone.room.security.CustomUserDetails;
import org.ezone.room.dto.AccommodationDTO;
import org.ezone.room.dto.AccommodationImgDTO;
import org.ezone.room.dto.ReservationDTO;
import org.ezone.room.dto.RoomDTO;
import org.ezone.room.entity.Member;
import org.ezone.room.repository.MemberRepository;
import org.ezone.room.repository.ReservationRepository;
import org.ezone.room.repository.RoomRepository;
import org.ezone.room.service.AccommodationService;
import org.ezone.room.service.RegisterService;
import org.ezone.room.service.RoomService;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@Controller
@RequestMapping("reservation")
@RequiredArgsConstructor
@Log4j2
@EnableJpaAuditing
public class RegisterController {
    final ReservationRepository reservationRepository;
    final RoomRepository repository;
    final ReviewBoardRepository reviewBoardRepository;
    final MemberRepository memberRepository;
    final AccommodationService accommodationService;
    final RoomService roomService;

    final RegisterService service;
    ModelMapper modelMapper = new ModelMapper();

    @GetMapping("register")
    public String register(Long rno, Model model,ReservationDTO dto){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Member member = null;
        if (principal instanceof CustomUserDetails) {
            member = ((CustomUserDetails) principal).getMember();
        }
        if(member == null){
            model.addAttribute("errorMessage", "로그인이 필요합니다.");
            return "redirect:/member/login";
        }

        RoomDTO roomDTO =  roomService.get(rno);
        int price = roomDTO.getPrice();
        LocalDate startDate = dto.getStartDate();
        LocalDate endDate = dto.getEndDate();

        // Period : 날짜의 계산을 도와주는 클래스
        Period period = Period.between(startDate, endDate);
        int days = period.getDays();

        int totalPrice = price * days;
        roomDTO.setPrice(totalPrice);
        AccommodationDTO accommodationDTO = accommodationService.findByAno(roomDTO.getAcc_id().getAno());

        model.addAttribute("days", days);
        model.addAttribute("acc", accommodationDTO);
        model.addAttribute("dto", roomDTO);
        model.addAttribute("rv",dto);
        model.addAttribute("member", member);

        return "reservation/register";
    }


    //사용자가 입력시 다시한번 검증
    @PostMapping("register")
    public String register(ReservationDTO rvDTO, RoomDTO roomDTO , Authentication authentication,
                           RedirectAttributes redirectAttributes) throws Exception{
        //rtts.addAttribute("StartDate",rvDTO.getStartDate().toString());
        //rtts.addAttribute("EndDate",rvDTO.getEndDate().toString());
        //사용자가 입력한 s,e date 가지고 redirect함--그러면 초기화안됨.

        //예약검사후 예약함.
        rvDTO.setMoney(roomDTO.getPrice());
        Long rvno = service.register(rvDTO,roomDTO,authentication);

        redirectAttributes.addFlashAttribute("rvno", rvno);
        return "redirect:/reservation/success";
    }

    @GetMapping("list")
    public void list(Model model){

        // authentication 객체를 이용하여 사용자 정보를 가져옴
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 사용자가 예약한 숙소 리스트를 가져옴
        List<ReservationDTO> list = service.getlist(authentication);
        for (ReservationDTO reservationDTO : list) {
            reservationDTO.setExistReview(reviewBoardRepository.existsByReviewBoard(reservationDTO.getRvno()));
        }

        // 시작일 기준으로 오름차순 정렬
        Collections.sort(list);

        List<AccommodationDTO> accommodationList = new ArrayList<>();
        // 각 예약 정보에 대해 숙소 정보를 가져옴
        for (ReservationDTO reservationDTO : list) {
            RoomDTO roomDTO = roomService.get(reservationDTO.getRoom_id().getRno());
            AccommodationDTO accommodationDTO = accommodationService.findByAno(roomDTO.getAcc_id().getAno());
            accommodationList.add(accommodationDTO);
        }

        List<AccommodationImgDTO> accommodationImgDTOS = accommodationService.findAccommodationWithImages(); // 이미지 출력

        model.addAttribute("list", list);
        model.addAttribute("accommodation", accommodationList);
        model.addAttribute("accommodationImgDTOS", accommodationImgDTOS);
        model.addAttribute("Today", LocalDate.now());
    }

    @GetMapping("cancel")
    public String cancel(Long rvno, Authentication authentication, RedirectAttributes rtts){
        //예약 취소는 상태를 취소로 바꾸는것. 삭제x
        //예약 취소는 체크인 미만인날만 가능.
        //여기서 검증할필요없이 검증은 db한테 맡겨서 알아서 척척해주자.
        //repository.findByRvnoAndDate(Date.valueOf(LocalDate.now()),31L);
        rtts.addAttribute("IsCancel",service.cancel(rvno,authentication));
        return "redirect:/reservation/list"; //목록으로 돌아가깅
    }

    @GetMapping("success")
    public void success(Model model) {

    }
}

