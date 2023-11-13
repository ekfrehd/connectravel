package com.connectravel.controller;

import com.connectravel.domain.dto.AccommodationDTO;
import com.connectravel.domain.dto.ImgDTO;
import com.connectravel.domain.dto.OptionDTO;
import com.connectravel.domain.dto.RoomDTO;
import com.connectravel.domain.entity.Accommodation;
import com.connectravel.domain.entity.Member;
import com.connectravel.repository.AccommodationRepository;
import com.connectravel.service.ImgService;
import com.connectravel.service.OptionService;
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
    private final OptionService optionService;

    @GetMapping("")
    public String main(@AuthenticationPrincipal Member member, Model model){
        if (member == null) {
            // 인증된 사용자가 없으면 로그인 페이지로 리다이렉트
            return "redirect:/login";
        }

        // 인증된 사용자의 이메일로 숙소 정보 조회
        Optional<Accommodation> optionalAccommodation = accommodationRepository.findBySellerEmail(member.getEmail());
        if (!optionalAccommodation.isPresent()) {
            // 해당 판매자의 숙소 정보가 없으면 적절한 메시지를 담아 뷰에 전달
            model.addAttribute("message", "등록된 숙소가 없습니다. 숙소를 등록해주세요.");
            return "seller/main";
        }

        // 옵션 목록을 가져와 모델에 추가
        List<OptionDTO> options = optionService.getAllOptions();
        model.addAttribute("options", options);

        // 숙소 정보가 있으면 모델에 추가
        Accommodation accommodation = optionalAccommodation.get();
        model.addAttribute("accommodation", accommodation);


        return "seller/main";
    }

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

    @GetMapping("reservation")
    public void reservation(Authentication authentication,Model model){
        List<Object[]> list = roomRepository.SellersfindReservation(authentication.getName());
        list.forEach(i -> {
            System.out.println("rvno : " + i[0]);
            System.out.println("state : " + i[1]);
            System.out.println("regdate : " + i[2]);
            System.out.println("StartDate : " + i[3]);
            System.out.println("EndDate : " + i[4]);
            Member reserver = (Member) i[5];
        });
        model.addAttribute("list",list);
    }

*/
}
