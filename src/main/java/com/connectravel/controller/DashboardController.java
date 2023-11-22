package com.connectravel.controller;

import com.connectravel.constant.SportEnum;
import com.connectravel.constant.StrictEnum;
import com.connectravel.domain.dto.dashboard.DashboardRequest;
import com.connectravel.service.DashboardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.transaction.Transactional;

@Controller
@RequestMapping("/view/v1")
@RequiredArgsConstructor
@Slf4j
@Transactional
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/dashboard")
    public String dashboard(Model model, @ModelAttribute DashboardRequest dashboardRequest, Authentication authentication){


        if(dashboardRequest.getStrict() != null){
            log.info("getStrict: {}", dashboardRequest.getStrict());
        }else{
            log.info("getStrict is null ");
        }

        if(dashboardRequest.getSport()!=null){
            log.info("getSportEnum: {}", dashboardRequest.getSport());

        }else{
            log.info("getSportEnum is null");
        }



        Long userCount = dashboardService.getUserCount();

        Long crewCount = dashboardService.getCrewCount();

        

        Long crewCountByStrict = dashboardService.getCrewCountByStrict(dashboardRequest.getStrict());
        Long crewCountBySportEnum = dashboardService.getCrewCountBySportEnum(dashboardRequest.getSport());
        model.addAttribute("userCount", userCount);
        model.addAttribute("crewCountBySportEnum", crewCountBySportEnum);
        model.addAttribute("crewCount", crewCount);
        model.addAttribute("crewCountByStrict", crewCountByStrict);


        return "dashboard/dashboard";
    }




    @ModelAttribute("sportEnums")
    private SportEnum[] sportEnums() {

        SportEnum[] sportEnum = SportEnum.values();
        System.out.println(sportEnum);

        return sportEnum;
    }

    @ModelAttribute("strictEnums")
    private StrictEnum[] strictEnums() {

        StrictEnum[] strictEnum = StrictEnum.values();
        System.out.println(strictEnum);

        return strictEnum;
    }



}
