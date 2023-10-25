package org.ezone.room.controller;

import org.ezone.room.dto.ImgDTO;
import org.ezone.room.dto.ReservationDTO;
import org.ezone.room.dto.RoomDTO;
import org.ezone.room.entity.AccommodationEntity;
import org.ezone.room.entity.Member;
import org.ezone.room.manager.DateManager;
import org.ezone.room.repository.AccommodationRepository;
import org.ezone.room.repository.ReservationRepository;
import org.ezone.room.repository.RoomRepository;
import org.ezone.room.service.ImgService;
import org.ezone.room.service.RoomService;
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
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("seller/room")
@RequiredArgsConstructor //final
public class SellerController {
    final RoomService roomService; //알아서 impl 찾음
    final ImgService imgService;
    final AccommodationRepository accrepository; //test
    final RoomRepository roomRepository;

    @Autowired
    DateManager dateManager;

    @GetMapping("")
    public String main(Authentication authentication, Model model){
        AccommodationEntity entity = accrepository.findByEmail(authentication.getName());
        //acc-member 조인을 한다 on . / url 조작이 가능하니까
        //먼저 Seller만 들어올 수 있게 권한을 설정하고 Seller중에도 조작을 할 수 있으니까
        //ano 방식이 아닌 repository에서 끌고오는 방식으로 설정하는게 좋은듯.
        // accommodation register는 무조건 autjentication읉 통해 인증해야만함

        //roomregister -> authentication -> select * from acc where member_id = ?
        //숙소 번호 불러오기
        model.addAttribute("entity",entity); //test
        return "seller/main";
    }
    //어드민 컨트롤러는 맴버정보확인해서 필터링 적용 필수
    @GetMapping("register")
    public void register(){}
    //방 리스트
    @GetMapping("list")
    public void list(Model model){
        model.addAttribute("list",roomService.getRoomWithImg() );}

    //관리자가 입력한 생성하고 싶은 방의 정보 (방이름,가격...)을 dto에 매핑하고
    //관리자가 넣은 사진들을 images에 넣는다.
    @PostMapping("register")
    public String register(@RequestParam("images") List<MultipartFile> images, RoomDTO dto, Authentication authentication){ //여기에 사용자가 입력한 정보들이 매핑됨.
        Long rno = roomService.register(authentication,dto); //방 등록을 하고 rno에 방 번호를 넣는다.(리턴된 값)
        //그 이유는 이미지가 방 번호를 참조하는 구조를 가져서 이미지를 생성할때 방이 필요하기 때문이다
        //이미지를 컴퓨터(서버)에 저장하고 DB에 저장하는 코드
        //foeEach를 사용하는 이유는 images가 리스트 형태로 들어와서 하나씩 하나씩 저장하려고
        images.forEach(i -> {
            imgService.RoomRegister(i,rno);
        });
        return "redirect:/seller/room/list";

    } //방 생성
    //방 수정
    @GetMapping("update")
    public void update(Long rno,Model model){
        RoomDTO dto = roomService.get(rno);
        model.addAttribute("dto",dto);
    }
    //방 수정
    @PostMapping("update")
    public String update(RoomDTO dto){
        roomService.modify(dto);
        return "redirect:/seller/room/list";
    }
    //방 삭제
    @GetMapping("delete")
    public String delete(Long rno) {
        roomService.remove(rno);
        return "redirect:/seller/room/list";
    }

    //방 read
    @GetMapping("read")
    public void read(Long rno,Model model){
        //룸 정보 하나와 옵션 리스트를 보여주는 read
        //조인을 해버리면 코드도 길어지고 더 복잡해지기때문에 조인의 필요성을 못느낌! 같은 rno에서 따로 따로 처리함

        RoomDTO roomDTO = roomService.get(rno); //rno로 room select
        //List<OptionDTO> options = optionService.getList(rno); //rno와 연관 관계를 맺는 모든 option을 list화된것
        List<ImgDTO> imgs = roomService.getImgList(rno);
        //
        model.addAttribute("imgs",imgs);
        model.addAttribute("dto",roomDTO);
        //model.addAttribute("options",options); //프론트로 보낸다
    } //read end

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


}
