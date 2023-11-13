package com.connectravel.controller;

import com.connectravel.domain.dto.AccommodationDTO;
import com.connectravel.domain.dto.OptionDTO;
import com.connectravel.domain.entity.Member;
import com.connectravel.repository.AccommodationRepository;
import com.connectravel.repository.MemberRepository;
import com.connectravel.service.AccommodationService;
import com.connectravel.service.ImgService;
import com.connectravel.service.OptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    private final AccommodationService accommodationService;

    private final OptionService optionService;

    private final AccommodationRepository accommodationRepository;

    private final ImgService imgService;

    private final MemberRepository memberRepository;

    @GetMapping("register")
    public String register(Model model, @AuthenticationPrincipal Member member) {
        // @AuthenticationPrincipal에서 직접 Member 객체를 사용
        if (member == null) {
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
            model.addAttribute("message", "숙소 등록을 하지 않았습니다. 등록이 필요합니다.");

            // 옵션 목록을 가져와 모델에 추가
            List<OptionDTO> options = optionService.getAllOptions();
            model.addAttribute("options", options);

            return "accommodation/register";
        }

        // 숙소를 등록했으면 해당 정보 읽기 페이지로 이동
        return "accommodation/read";
    }


    @PostMapping("register")
    public String registerAccommodation(@RequestParam("images") List<MultipartFile> images,
                                        @RequestParam("optionIds") List<Long> optionIds, // 옵션 아이디 리스트 추가
                                        AccommodationDTO dto, RedirectAttributes redirectAttributes,
                                        @AuthenticationPrincipal Member member) {

        int firstSpaceIndex = dto.getAddress().indexOf(" ");
        int secondSpaceIndex = dto.getAddress().indexOf(" ", firstSpaceIndex + 1);
        dto.setRegion(dto.getAddress().substring(0, secondSpaceIndex));

        // 숙박업소 등록 시 옵션 아이디 리스트를 DTO에 설정
        dto.setOptionIds(optionIds);
        dto.setSellerEmail(member.getEmail());

        Long ano = accommodationService.registerAccommodation(dto); // 새로 추가된 엔티티의 번호(dto)

        images.forEach(i -> {
            imgService.AccommodationRegister(i, ano);
        });

        redirectAttributes.addFlashAttribute("msg", ano);

        return "redirect:/seller/list";
    }

  /*

 @GetMapping("read")
    public String read(Model model, @AuthenticationPrincipal Member member)  {

        if (member == null) {
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
    }*/

}
