package com.connectravel.controller;

import com.connectravel.domain.dto.AccommodationDTO;
import com.connectravel.domain.dto.ImgDTO;
import com.connectravel.domain.dto.ReservationDTO;
import com.connectravel.domain.dto.RoomDTO;
import com.connectravel.domain.entity.Accommodation;
import com.connectravel.domain.entity.Member;
import com.connectravel.repository.AccommodationRepository;
import com.connectravel.service.ImgService;
import com.connectravel.service.ReservationService;
import com.connectravel.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("seller")
@RequiredArgsConstructor //final
public class SellerController {

    private final RoomService roomService;
    private final ImgService imgService;
    private final AccommodationRepository accommodationRepository;
    private final ReservationService reservationService;


    @GetMapping("list")
    public String list(@AuthenticationPrincipal Member member, Model model) {
        Optional<Accommodation> accommodationOpt = accommodationRepository.findBySellerEmail(member.getEmail());
        accommodationOpt.ifPresent(accommodation -> {
            List<RoomDTO> rooms = roomService.listRoomsByAccommodation(accommodation.getAno());
            model.addAttribute("list", rooms);
        });
        return "seller/list"; // 뷰 이름을 반환
    }

    @GetMapping("register")
    public void register(@AuthenticationPrincipal Member member, Model model) {
        Optional<Accommodation> accommodationOpt = accommodationRepository.findBySellerEmail(member.getEmail());
        accommodationOpt.ifPresent(accommodation -> {
            AccommodationDTO accommodationDTO = new AccommodationDTO();
            accommodationDTO.setAno(accommodation.getAno());
            model.addAttribute("dto", accommodationDTO);
        });
    }

    @PostMapping("register")
    public String register(@RequestParam("images") List<MultipartFile> images,
                           RoomDTO dto, // 폼에서 받은 데이터
                           @ModelAttribute("accommodationDTO") AccommodationDTO accommodationDTO) {
        dto.setAccommodationDTO(accommodationDTO);

        Long rno = roomService.registerRoom(dto);
        images.forEach(i -> imgService.addRoomImg(i, rno));
        return "redirect:/seller/list";
    }

    //방 수정
    @GetMapping("update")
    public String updateRoomForm(@RequestParam("rno") Long rno, Model model) {
        RoomDTO roomDTO = roomService.getRoom(rno);
        if (roomDTO == null) {
            return "redirect:/seller/list";
        }
        model.addAttribute("roomDTO", roomDTO);
        return "seller/update";
    }

    //방 수정
    @PostMapping("update")
    public String updateRoom(@ModelAttribute("roomDTO") RoomDTO roomDTO, RedirectAttributes redirectAttributes) {
        try {
            roomService.modifyRoom(roomDTO.getRno(), roomDTO);
            redirectAttributes.addFlashAttribute("successMessage", "방 정보가 성공적으로 업데이트되었습니다.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "정보 업데이트 중 오류가 발생했습니다.");
        }
        return "redirect:/seller/list";
    }

    // 방 정보 읽기
    @GetMapping("read")
    public String read(@RequestParam("rno") Long rno, Model model) {
        RoomDTO roomDTO = roomService.getRoom(rno);
        if (roomDTO == null) {
            return "redirect:/seller/list"; // 방 정보가 없으면 목록으로 리다이렉트
        }

        List<ImgDTO> imgs = roomService.getImgList(rno);
        model.addAttribute("imgs", imgs);
        model.addAttribute("roomDTO", roomDTO);

        return "seller/read";
    }

    //방 삭제
    @GetMapping("delete")
    public String delete(Long rno) {
        roomService.removeRoom(rno);
        return "redirect:/seller/list";
    }

    @GetMapping("reservation")
    public void reservation(@AuthenticationPrincipal Member member ,Model model){
        List<ReservationDTO> list = reservationService.listRoomBookings(member.getId());

        model.addAttribute("list",list);
    }

    @GetMapping("/cancellation/approval/{rvno}")
    public String approveCancellation(@PathVariable Long rvno) {
        // 예약 상태를 CANCELLED로 업데이트하는 로직
        reservationService.approveCancellation(rvno);
        return "redirect:/seller/reservation";
    }


   /*
    @GetMapping("sales")
    public void sales(ReservationDTO datedto, Authentication authentication, Model model){
        Long ano = accrepository.findByEmail(authentication.getName()).getAno();
        dateManager.CheckDate(datedto.getStartDate(),datedto.getEndDate());
        datedto = ReservationDTO.builder().StartDate(dateManager.getStartDate()).EndDate(dateManager.getEndDate()).build();
        model.addAttribute("sales",roomService.get_salesByDate(Date.valueOf(datedto.getStartDate()),Date.valueOf(datedto.getEndDate()),ano));
        model.addAttribute("StartDate",datedto.getStartDate());
        model.addAttribute("EndDate",datedto.getEndDate());
    }

*/
}
