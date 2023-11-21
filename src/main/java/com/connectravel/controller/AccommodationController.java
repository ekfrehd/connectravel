package com.connectravel.controller;

import com.connectravel.domain.dto.AccommodationDTO;
import com.connectravel.domain.dto.ImgDTO;
import com.connectravel.domain.dto.OptionDTO;
import com.connectravel.domain.entity.Member;
import com.connectravel.repository.AccommodationRepository;
import com.connectravel.service.AccommodationService;
import com.connectravel.service.ImgService;
import com.connectravel.service.OptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
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

    @GetMapping("read")
    public String read(Model model, @AuthenticationPrincipal Member member) {
        if (member == null) {
            model.addAttribute("errorMessage", "로그인이 필요합니다.");
            return "redirect:/member/login";
        }

        AccommodationDTO accommodationDTO = accommodationService.findAccommodationByMemberId(member.getId());

        if (accommodationDTO == null) {
            return "redirect:/accommodation/register";
        }

        List<ImgDTO> imgDTOS = accommodationService.getImgList(accommodationDTO.getAno());
        model.addAttribute("dto", accommodationDTO);
        model.addAttribute("images", imgDTOS);

        return "accommodation/read";
    }

    @GetMapping("update")
    public String update(Model model, @AuthenticationPrincipal Member member) {
        if (member == null) {
            model.addAttribute("errorMessage", "로그인이 필요합니다.");
            return "redirect:/member/login";
        }

        AccommodationDTO accommodationDTO = accommodationService.findAccommodationByMemberId(member.getId());

        if (accommodationDTO == null) {
            model.addAttribute("errorMessage", "숙박업소 정보가 없습니다.");
            return "redirect:/seller/accommodation/register";
        }

        model.addAttribute("dto", accommodationDTO);
        return "accommodation/update";
    }

    @PostMapping("update")
    public String update(@ModelAttribute AccommodationDTO dto, RedirectAttributes redirectAttributes,
                         @AuthenticationPrincipal Member member) {
        if (member == null || !member.getEmail().equals(dto.getSellerEmail())) {
            redirectAttributes.addFlashAttribute("errorMessage", "수정 권한이 없습니다.");
            return "redirect:/seller/accommodation";
        }

        try {
            accommodationService.modifyAccommodationDetails(dto);
            redirectAttributes.addFlashAttribute("successMessage", "숙박업소 정보가 성공적으로 업데이트되었습니다.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "정보 업데이트 중 오류가 발생했습니다.");
        }

        return "redirect:/seller/accommodation/read";
    }


}
