package com.connectravel.controller;

import com.connectravel.domain.dto.AccommodationDTO;
import com.connectravel.domain.entity.Accommodation;
import com.connectravel.domain.entity.Member;
import com.connectravel.repository.AccommodationRepository;
import com.connectravel.repository.MemberRepository;
import com.connectravel.service.AccommodationService;
import com.connectravel.service.ImgService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
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
public class AccommodationController {

    // Seller에서의 Accommodation CRUD

    private final AccommodationService accommodationService;

    private final AccommodationRepository accommodationRepository;

    private final ImgService imgService;

    @Autowired
    private MemberRepository memberRepository; // 멤버조회가 필요하므로 추가

    @GetMapping("register")
    public String register(Model model, Authentication authentication) {
        String email = authentication.getName(); // Authentication 객체에서 이메일을 직접 가져옴
        Member member = memberRepository.findByEmail(email);

        if(member == null){
            model.addAttribute("errorMessage", "로그인이 필요합니다.");
            return "redirect:/member/login";
        }

        // 사용자가 'ROLE_SELLER' 권한을 가지고 있는지 확인
        boolean isSeller = member.getMemberRoles().stream()
                .anyMatch(role -> role.getRoleName().equals("ROLE_SELLER"));

        if (!isSeller) {
            // 'ROLE_SELLER' 권한이 없으면 액세스 거부 메시지와 함께 메인 페이지로 리다이렉트
            model.addAttribute("errorMessage", "판매자 등록이 필요합니다.");
            return "redirect:/";
        }

        if (!accommodationRepository.existsByMemberId(member.getId())) {
            // 'ROLE_SELLER' 권한이 있지만 숙소를 등록하지 않았으면 등록 유도
            model.addAttribute("message", "숙소등록을 하지 않으셨습니다. 등록이 필요합니다.");
            return "accommodation/register";
        }

        // 숙소를 등록했으면 해당 정보 읽기 페이지로 이동
        return "accommodation/read";
    }


    @PostMapping("register")
    public String registerAccommodation(@RequestParam("images") List<MultipartFile> images,
                                        AccommodationDTO dto, RedirectAttributes redirectAttributes,
                                        Authentication authentication) {
        String email = authentication.getName();
        Member member = memberRepository.findByEmail(email);

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
    public String read(Model model, Authentication authentication) {
        String email = authentication.getName();
        Member member = memberRepository.findByEmail(email);

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
    public String update(Model model, Authentication authentication) {
        String email = authentication.getName();
        Member member = memberRepository.findByEmail(email);

        if(member == null){
            model.addAttribute("errorMessage", "로그인이 필요합니다.");
            return "redirect:/member/login";
        }

        Accommodation accommodation = accommodationRepository.findAccommodationByMemberId(member.getId());
        model.addAttribute("dto", accommodation);

        return "accommodation/update";
    }

    @PostMapping("update")
    public String update(AccommodationDTO dto, Authentication authentication){
        String email = authentication.getName();
        Member member = memberRepository.findByEmail(email);

        dto.setSellerEmail(member.getEmail());
        accommodationService.modifyAccommodationDetails(dto);
        return "redirect:/seller/accommodation/read";
    }

}
