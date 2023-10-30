package com.connectravel.manager;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Getter
@Setter
@Component
public class DateManager {

    private LocalDate startDate; // 시작 날짜
    private LocalDate endDate; // 종료 날짜

    // Null일 경우 오늘 날짜로 설정
    public LocalDate[] checkDateIfNull(LocalDate startDate, LocalDate endDate){
        if (startDate == null || endDate == null) {
            LocalDate[] dates = setTodayAndTomorrow();
            setDates(dates[0], dates[1]);
            return dates;
        }
        setDates(startDate, endDate);
        return new LocalDate[] {startDate, endDate};
    }

    // 오늘과 내일 날짜 설정
    public LocalDate[] setTodayAndTomorrow(){
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);
        return new LocalDate[]{today, tomorrow};
    }

    // 시작 날짜가 종료 날짜보다 이후인지 확인
    public LocalDate[] checkDateOverlapping(LocalDate startDate, LocalDate endDate){
        if (endDate.compareTo(startDate) < 0) {
            LocalDate[] dates = setTodayAndTomorrow();
            setDates(dates[0], dates[1]);
            return dates;
        }
        setDates(startDate, endDate);
        return new LocalDate[]{startDate, endDate};
    }

    // 널 확인 및 날짜 겹침 확인
    public LocalDate[] checkDate(LocalDate startDate, LocalDate endDate){
        checkDateIfNull(startDate, endDate);
        return checkDateOverlapping(getStartDate(), getEndDate());
    }

    private void setDates(LocalDate startDate, LocalDate endDate){
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
