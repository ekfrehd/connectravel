package org.ezone.room.repository.search;

import org.ezone.room.entity.QnaBoard;
import org.ezone.room.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface SearchBoardRepository {

    QnaBoard search1();

    Page<Object[]> searchPage(String[] type, String keyword, Pageable pageable);

    Page<Object[]> searchPageByWriter(String[] type, Member member, String keyword, Pageable pageable);

    public Page<Object[]> searchPageAdminBaord(String[] type, String category, String keyword, Pageable pageable);

    public Page<Object[]> searchPageAccommodation(String[] type, String keyword, String category, String region,
                                                  LocalDate startDate, LocalDate endDate, Integer inputedMinPrice, Integer inputedMaxPrice,
                                                  Pageable pageable);

    public Page<Object[]> searchTourBoard(String[] type,  String keyword, String category, String region, Pageable pageable);
}
