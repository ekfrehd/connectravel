package org.ezone.room.manager;

import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class DateManager {

    private LocalDate StartDate; //인덱스는 불편함. 클래스로 끌고오는게 좋음
    private LocalDate EndDate;
    //들어온값이 Null이면 오늘날짜로 변경.

    public LocalDate[] CheckDate_Null(LocalDate StartDate,LocalDate EndDate){
        boolean IsDateNull = (StartDate == null || EndDate==null); //조건
        if(IsDateNull){
            LocalDate[] list = SetNowDate(StartDate,EndDate);
            SetDate(list[0],list[1]);
            return list;
        } //널이면 오늘+내일 날짜 리턴
        SetDate(StartDate,EndDate);
        return new LocalDate[] {StartDate,EndDate}; //그냥 리턴
    }
    //StartDate = 오늘 EndDate = 오늘 + 1
    public LocalDate[] SetNowDate(LocalDate StartDate,LocalDate EndDate){
        StartDate = LocalDate.now();
        EndDate = StartDate.plusDays(1L);
        return new LocalDate[]{StartDate,EndDate};
    }

    //StartDate < EndDate 체크
    public LocalDate[] CheckDate_Over(LocalDate StartDate,LocalDate EndDate){
        boolean IsOverDate = EndDate.compareTo(StartDate) < 0;
        if(IsOverDate){LocalDate[] list = SetNowDate(StartDate,EndDate);
            SetDate(list[0],list[1]); return list;} //DateOver발생시 오늘 + 내일 날짜로 리턴함
        SetDate(StartDate,EndDate);
        return new LocalDate[]{StartDate,EndDate}; //그냥 리턴
    }

    //보통 두개 다 처리하니까. 최종적으로 모두 체크하는부분
    public LocalDate[] CheckDate(LocalDate StartDate,LocalDate EndDate){
        CheckDate_Null(StartDate,EndDate); //항상 널 체크부터
        return CheckDate_Over(getStartDate(),getEndDate()); //널일수도있으니까.
    }
    private void SetDate(LocalDate startDate,LocalDate endDate){
        StartDate = startDate;
        EndDate = endDate;
    }

}
