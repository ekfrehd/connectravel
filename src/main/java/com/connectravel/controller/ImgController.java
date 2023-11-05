package com.connectravel.controller;

import com.connectravel.service.ImgService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("img")
@RequiredArgsConstructor
public class ImgController {

    final ImgService imgService;

    /*@GetMapping("remove")
    public void delete(Long ino) {
        imgService.remove(ino);
    }*/

    @GetMapping("update")
    public void update() {
    }

   /* @PostMapping("update")
    public void update(Long ino, MultipartFile file) {

        //파일 업데이트 과정은
        //원래 파일 UUID 검출
        //원래 파일 삭제
        //원래 파일 UUID + file생성
        //생성된 파일이름,파일넘버로 엔티티로 구성해서
        //repository에서 save를 통해 update를 하면됨
        imgService.update(ino, file);
    }*/
}
