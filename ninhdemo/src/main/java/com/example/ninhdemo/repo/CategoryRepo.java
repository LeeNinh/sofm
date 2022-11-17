package com.example.ninhdemo.repo;

import com.example.ninhdemo.entities.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
@Repository
public interface CategoryRepo extends JpaRepository<Category, Integer> {
    @Query("SELECT u FROM Category u WHERE u.name LIKE :x ")
    Page<Category> searchByName(@Param("x") String s, Pageable pageable);

    @Query("SELECT u FROM Category u " + "WHERE u.createdAt >= :start and u.createdAt <= :end")
    Page<Category> searchByDate(@Param("start") Date start, @Param("end") Date end, Pageable pageable);

    @Query("SELECT u FROM Category u " + "WHERE u.createdAt >= :start")
    Page<Category> searchByStartDate(@Param("start") Date start, Pageable pageable);

    @Query("SELECT u FROM Category u " + "WHERE u.createdAt <= :end")
    Page<Category> searchByEndDate(@Param("end") Date end, Pageable pageable);

    @Query("SELECT u FROM Category u WHERE u.name LIKE :x AND u.createdAt >= :start AND u.createdAt <= :end")
    Page<Category> searchByNameAndDate(@Param("x") String s, @Param("start") Date start, @Param("end") Date end,
                                       Pageable pageable);

    @Query("SELECT u FROM Category u WHERE u.name LIKE :x AND u.createdAt >= :start")
    Page<Category> searchByNameAndStartDate(@Param("x") String s, @Param("start") Date start, Pageable pageable);

    @Query("SELECT u FROM Category u WHERE u.name LIKE :x AND u.createdAt <= :end")
    Page<Category> searchByNameAndEndDate(@Param("x") String s, @Param("end") Date end, Pageable pageable);

}