package com.example.ninhdemo.repo;
import com.example.ninhdemo.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface ProductRepo extends JpaRepository<Product, Integer> {
    @Query("SELECT u FROM Product u WHERE u.name LIKE :x ")
    Page<Product> searchByName(@Param("x") String s, Pageable pageable);

    @Query("SELECT u FROM Product u " + "WHERE u.createdAt >= :start and u.createdAt <= :end")
    Page<Product> searchByDate(@Param("start") Date start, @Param("end") Date end, Pageable pageable);

    @Query("SELECT u FROM Product u " + "WHERE u.createdAt >= :start")
    Page<Product> searchByStartDate(@Param("start") Date start, Pageable pageable);

    @Query("SELECT u FROM Product u " + "WHERE u.createdAt <= :end")
    Page<Product> searchByEndDate(@Param("end") Date end, Pageable pageable);

    @Query("SELECT u FROM Product u WHERE u.name LIKE :x AND u.createdAt >= :start AND u.createdAt <= :end")
    Page<Product> searchByNameAndDate(@Param("x") String s, @Param("start") Date start, @Param("end") Date end,
                                       Pageable pageable);

    @Query("SELECT u FROM Product u WHERE u.name LIKE :x AND u.createdAt >= :start")
    Page<Product> searchByNameAndStartDate(@Param("x") String s, @Param("start") Date start, Pageable pageable);

    @Query("SELECT u FROM Product u WHERE u.name LIKE :x AND u.createdAt <= :end")
    Page<Product> searchByNameAndEndDate(@Param("x") String s, @Param("end") Date end, Pageable pageable);

}