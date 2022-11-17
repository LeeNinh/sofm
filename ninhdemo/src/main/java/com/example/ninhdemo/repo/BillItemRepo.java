package com.example.ninhdemo.repo;


import com.example.ninhdemo.entities.BillItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BillItemRepo extends JpaRepository<BillItem, Integer> {
    @Query("SELECT  b FROM  BillItem b WHERE  b.bill.id= :id")
    public List<BillItem> findbyBillId(@Param("id") Integer id);

}