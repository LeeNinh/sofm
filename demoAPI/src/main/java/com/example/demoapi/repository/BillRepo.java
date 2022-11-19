package com.example.demoapi.repository;


import com.example.demoapi.entities.Bill;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface BillRepo extends JpaRepository<Bill, Integer> {

    @Query("SELECT b FROM Bill b WHERE b.user.id = :x ")
    Page<Bill> searchByUserId(@Param("x") int x , Pageable pageable);

    @Query("SELECT b FROM Bill b")
    Page<Bill> getAll(Pageable pageable);
}
