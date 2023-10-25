package org.ezone.room.controller;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.ezone.room.entity.AccommodationEntity;
import org.ezone.room.security.CustomUserDetails;
import org.ezone.room.dto.AccommodationDTO;
import org.ezone.room.dto.AccommodationImgDTO;
import org.ezone.room.entity.Member;
import org.ezone.room.repository.AccommodationRepository;
import org.ezone.room.repository.MemberRepository;
import org.ezone.room.service.AccommodationService;
import org.ezone.room.service.ImgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("seller/accommodation")
@Log4j2
@RequiredArgsConstructor
public class AccomodationController {

    // Seller에서의 Accommodation CRUD

    private final AccommodationService accommodationService;

    private final AccommodationRepository accommodationRepository;

    private final ImgService imgService;

    @Autowired
    private MemberRepository memberRepository; // 멤버조회가 필요하므로 추가

    @GetMapping("register")
    public String register(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = ((CustomUserDetails) authentication.getPrincipal()).getMember();
        if(member == null){
            model.addAttribute("errorMessage", "로그인이 필요합니다.");
            return "redirect:/member/login";
        }
        if (!accommodationRepository.existsAccommodationByMemberId(member.getId())) {
            model.addAttribute("message", "숙소등록을 하지 않으셨습니다. 등록이 필요합니다.");
            return "accommodation/register"; // 존재하지않으면 작성유도
        }

        return "accommodation/read"; // 존재하면 유도 // 애초에 숙소는 수정만 되지 등록은 안 되는 로직을 짤거임
    }

    @PostMapping("register")
    public String registerAccommodation(@RequestParam("images") List<MultipartFile> images,
                                        AccommodationDTO dto, RedirectAttributes redirectAttributes, Authentication authentication) {
        Member member = memberRepository.findByEmail(authentication.getName());

        dto.setEmail(member.getEmail());

        int firstSpaceIndex = dto.getAddress().indexOf(" ");
        int secondSpaceIndex = dto.getAddress().indexOf(" ", firstSpaceIndex + 1);
        dto.setRegion(dto.getAddress().substring(0, secondSpaceIndex));

        Long ano = accommodationService.register(dto); //새로 추가된 엔티티의 번호(dto)

        images.forEach(i -> {
            imgService.AccommodationRegister(i, ano);
        });

        redirectAttributes.addFlashAttribute("msg", ano);

        return "redirect:/seller/room/list";
    }

    @GetMapping("read")
    public String read(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = ((CustomUserDetails) authentication.getPrincipal()).getMember();
        if(member == null){
            model.addAttribute("errorMessage", "로그인이 필요합니다.");
            return "redirect:/member/login";
        }
        AccommodationDTO accommodationDTO = accommodationService.findAccommodationByMemberId(member.getId());

        if(accommodationDTO == null){
            return "redirect:/accommodation/register";
        }

        List<AccommodationImgDTO> accommodationImgDTOS = accommodationService.findAccommodationWithImages();
        model.addAttribute("dto", accommodationDTO);
        model.addAttribute("accommodationImgDTOS", accommodationImgDTOS);
        return "accommodation/read";
    }

    @GetMapping("update")
    public String update(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = ((CustomUserDetails) authentication.getPrincipal()).getMember();
        if(member == null){
            model.addAttribute("errorMessage", "로그인이 필요합니다.");
            return "redirect:/member/login";
        }

        AccommodationEntity accommodation = accommodationRepository.findAccommodationByMemberId(member.getId());
        model.addAttribute("dto", accommodation);

        return "accommodation/update";
    }

    @PostMapping("update")
    public String update(AccommodationDTO dto){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = ((CustomUserDetails) authentication.getPrincipal()).getMember();
        dto.setEmail(member.getEmail());
        accommodationService.modify(dto);
        return "redirect:/seller/accommodation/read";
    }

}
