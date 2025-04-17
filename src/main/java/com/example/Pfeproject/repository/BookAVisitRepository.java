package com.example.Pfeproject.repository;

import com.example.Pfeproject.entity.BookAVisit;
import com.example.Pfeproject.enums.BookAVisitEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookAVisitRepository extends JpaRepository<BookAVisit,Long> {
    List<BookAVisit> findAllByUserId(Long userId);

    List<BookAVisit> findAllByOrderByVisitDateAsc();


    Long countByUserId(Long userId);

    Long countByBookAVisitEnumAndUserId(BookAVisitEnum bookAVisitEnum,Long userId);

    Long countByBookAVisitEnum(BookAVisitEnum bookAVisitEnum);
}
