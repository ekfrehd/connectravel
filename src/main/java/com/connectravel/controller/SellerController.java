package com.connectravel.controller;

import com.connectravel.domain.dto.MemberDTO;
import com.connectravel.domain.dto.RoomDTO;
import com.connectravel.domain.entity.Accommodation;
import com.connectravel.repository.AccommodationRepository;
import com.connectravel.repository.RoomRepository;
import com.connectravel.service.ImgService;
import com.connectravel.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Date;
import java.util.List;

@Controller
@RequestMapping("seller")
@RequiredArgsConstructor
public class SellerController {

    @GetMapping("/list")
    //@PreAuthorize("hasRole('ROLE_SELLER')")
    public void list(Model model, MemberDTO member){

    }

    final RoomService roomService;

    final ImgService imgService;

    final AccommodationRepository accommodationRepository;

    final RoomRepository roomRepository;

    @GetMapping("")
    public String main(Authentication authentication, Model model) {

        Accommodation entity = accommodationRepository.findBySellerEmail(authentication.getName());

        model.addAttribute("entity",entity);

        return "seller/main";
    }

    @GetMapping("register")
    public void register() {}

    @GetMapping("list")
    public void list(Model model) {
        model.addAttribute("list",roomService.getRoomWithImg() );
    }

    @PostMapping("register")
    public String register(@RequestParam("images") List<MultipartFile> images, RoomDTO dto, Authentication authentication) { //여기에 사용자가 입력한 정보들이 매핑됨.

        Long rno = roomService.register(authentication,dto); //방 등록을 하고 rno에 방 번호를 넣는다.(리턴된 값)

        images.forEach(i -> {
            imgService.RoomRegister(i,rno);
        });

        return "redirect:/seller/list";
    }

    @GetMapping("update")
    public void update(Long rno,Model model) {
        RoomDTO dto = roomService.get(rno);
        model.addAttribute("dto",dto);
    }

    @PostMapping("update")
    public String update(RoomDTO dto) {
        roomService.modify(dto);
        return "redirect:/seller/room/list";
    }

    @GetMapping("delete")
    public String delete(Long rno) {
        roomService.remove(rno);
        return "redirect:/seller/room/list";
    }

    @GetMapping("read")
    public void read(Long rno,Model model) {

        RoomDTO roomDTO = roomService.get(rno);
        List<ImgDTO> imgs = roomService.getImgList(rno);

        model.addAttribute("imgs",imgs);
        model.addAttribute("dto",roomDTO);

    }

    @GetMapping("sales")
    public void sales(ReservationDTO datedto, Authentication authentication, Model model) {

        Long ano = accrepository.findByEmail(authentication.getName()).getAno();

        dateManager.CheckDate(datedto.getStartDate(),datedto.getEndDate());
        datedto = ReservationDTO.builder().StartDate(dateManager.getStartDate()).EndDate(dateManager.getEndDate()).build();

        model.addAttribute("sales",roomService.get_salesByDate(Date.valueOf(datedto.getStartDate()),Date.valueOf(datedto.getEndDate()),ano));
        model.addAttribute("StartDate",datedto.getStartDate());
        model.addAttribute("EndDate",datedto.getEndDate());

    }

    @GetMapping("reservation")
    public void reservation(Authentication authentication,Model model) {

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

}