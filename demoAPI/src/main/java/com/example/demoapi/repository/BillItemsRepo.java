package com.example.demoapi.repository;

import com.example.demoapi.entities.BillItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface BillItemsRepo extends JpaRepository<BillItems, Integer> {


}
