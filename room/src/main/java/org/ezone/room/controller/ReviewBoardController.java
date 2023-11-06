package org.ezone.room.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.ezone.room.dto.AccommodationDTO;
import org.ezone.room.dto.ImgDTO;
import org.ezone.room.dto.PageRequestDTO;
import org.ezone.room.dto.PageResultDTO;
import org.ezone.room.dto.ReservationDTO;
import org.ezone.room.dto.ReviewBoardDTO;
import org.ezone.room.entity.Accommodation;
import org.ezone.room.entity.Member;
import org.ezone.room.entity.Reservation;
import org.ezone.room.entity.ReviewBoard;
import org.ezone.room.manager.DateManager;
import org.ezone.room.repository.MemberRepository;
import org.ezone.room.repository.ReservationRepository;
import org.ezone.room.repository.ReviewBoardRepository;
import org.ezone.room.security.CustomUserDetails;
import org.ezone.room.service.ImgService;
import org.ezone.room.service.ReviewBoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/review")
public class ReviewBoardController {
    // list 출력은 ProductController에 있음

    private final ReservationRepository reservationRepository;

    private final MemberRepository memberRepository;

    private final ReviewBoardService reviewBoardService;

    private final ReviewBoardRepository reviewBoardRepository;

    private final ImgService imgService;

    @Autowired
    DateManager dateManager;


    @GetMapping("register/{rvno}")
    public String goRegister(@PathVariable("rvno") Long rvno, RedirectAttributes redirectAttributes,
                             ReviewBoardDTO reviewBoardDTO, Model model) throws NotFoundException {
//        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Member member = memberRepository.findByEmail("asd@asd");
//        if (principal instanceof CustomUserDetails) {
//            member = ((CustomUserDetails) principal).getMember();
//        }
        if (member == null) {
            model.addAttribute("errorMessage", "로그인이 필요합니다.");
            return "redirect:/member/login";
        }

        Optional<Reservation> reservation = Optional.ofNullable(reservationRepository.findByRvno(rvno));

        if (!reservation.get().getMember().getId().equals(member.getId())) {
            log.info("다른사람껀 일단 리다이렉트");
            return "redirect:/";
        }

        if (reservation.isPresent()) {
            Accommodation accommodation = reviewBoardService.findAccommodationByRoomId(
                    reservation.get().getRoom().getRno());

            reviewBoardDTO.setWriterEmail(member.getEmail());
            reviewBoardDTO.setAno(accommodation.getAno());
            reviewBoardDTO.setRno(reservation.get().getRoom().getRno());
            reviewBoardDTO.setRvno(reservation.get().getRvno());
            model.addAttribute("dto", reviewBoardDTO);
            model.addAttribute("accommodationName", accommodation.getName());
            model.addAttribute("roomName", reservation.get().getRoom().getRoomName());
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Reservation not found");
        }
        return "member/review";
    }

    @PostMapping("register")
    public String register(@RequestParam("images") List<MultipartFile> images,
                           RedirectAttributes redirectAttributes, ReviewBoardDTO reviewBoardDTO)
            throws NotFoundException {
        if (reviewBoardRepository.existsByReviewBoard(reviewBoardDTO.getRvno())) {
            redirectAttributes.addFlashAttribute("errorMessage", "리뷰가 이미 존재합니다.");
            return "redirect:/";
        }
        System.out.println("@@@@@@@@@@@@@@@@@" + reviewBoardDTO);
        log.info("@@@@@@@@@@@@@@@@ ReviewBoardDTO is null");
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        Member member = ((CustomUserDetails) authentication.getPrincipal()).getMember();
        Member member = memberRepository.findByEmail("asd@asd");
        Optional<Reservation> reservation = reservationRepository.findById(reviewBoardDTO.getRvno());
        log.info("reservation : " + reservation);
        System.out.println("@@@@@@@@@@@@@@@@@");

        if (reservation.isPresent()) {
            Accommodation accommodation = reviewBoardService.findAccommodationByRoomId(
                    reservation.get().getRoom().getRno());

            reviewBoardDTO.setWriterEmail(member.getEmail());
            reviewBoardDTO.setAno(accommodation.getAno());
            reviewBoardDTO.setRno(reservation.get().getRoom().getRno());
            reviewBoardDTO.setRvno(reservation.get().getRvno());
            Long rbno = reviewBoardService.register(reviewBoardDTO);
            log.info("리뷰 저장 완료");
            images.forEach(i -> {
                imgService.ReviewBoardRegister(i, rbno);
            });
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Reservation not found");
        }

        // 파일등록 여부에따른 포인트 반영
        if (images == null) {
            member.setPoint(member.getPoint() + 200);
        } else {
            member.setPoint(member.getPoint() + 300);
        }
        CustomUserDetails customUserDetails = new CustomUserDetails(memberRepository.save(member));
        SecurityContextHolder.getContext().setAuthentication
                (new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities()));
        System.out.println("@@@@@@@@@@@@@@@@@");
        return "redirect:/";
    }

    @GetMapping("{ano}")
    public String Accommodation(@PathVariable("ano") Long ano,
                                @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                Model model, ReservationDTO datedto) {
        int pageSize = 5;

//         숙소 data 추출
//        AccommodationDTO accommodation = accommodationService.findByAno(ano);
//        double grade = Math.round(accommodation.getGrade() * 100) / 100.0;
//        accommodation.setGrade(grade);
//        List<ImgDTO> accommodationImgDTOS = accommodationService.getImgList(ano);

        // ReviewBoard data 추출
        PageRequestDTO pageRequestDTO = new PageRequestDTO(page, pageSize);
        PageResultDTO<ReviewBoardDTO, ReviewBoard> pageResultDTO = reviewBoardService.getReviewBoardsAndPageInfoByAccommodationId(ano, pageRequestDTO);
        if(pageResultDTO.getTotalPage()==0){ pageResultDTO.setTotalPage(1);} // 글이 하나도 없을 땐 0으로 인식하므로

        Map<Long, List<ImgDTO>> reviewBoardImgMap = new HashMap<>();
        for (ReviewBoardDTO reviewBoardDTO : pageResultDTO.getDtoList()) {
            List<ImgDTO> reviewBoardImgDTOS = reviewBoardService.getImgList(reviewBoardDTO.getRbno());
            reviewBoardImgMap.put(reviewBoardDTO.getRbno(), reviewBoardImgDTOS);
        }

        //여기부터는 방 정보임
//        dateManager.CheckDate(datedto.getStartDate(),datedto.getEndDate()); //CheckDate
//        datedto = ReservationDTO.builder().StartDate(dateManager.getStartDate()).EndDate(dateManager.getEndDate()).build();
//        List<Object[]> list = roomService.getRvList(ano,datedto); //ano/date에 해당하는 list불러오기
        //인덱스 0번 방정보 1번 이미지정보 2번 예약정보

//        model.addAttribute("R_list",list);
        model.addAttribute("date",datedto);
//        model.addAttribute("accommodation", accommodation);
        model.addAttribute("pageResultDTO", pageResultDTO);
        model.addAttribute("reviewBoardImgMap", reviewBoardImgMap);
//        model.addAttribute("accommodationImgDTOS", accommodationImgDTOS);

        return "product/review";
    }
}

