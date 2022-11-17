package com.example.ninhdemo.repo;

import com.example.ninhdemo.entities.Bill;
import com.example.ninhdemo.entities.BillStatistic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;


@Repository
public interface BillRepo extends JpaRepository<Bill, Integer> {

    @Modifying // cho những câu lệnh không có hàm sẵn.
    @Query("DELETE FROM Bill b WHERE b.createdAt >= :x ")
    void deleteByCreateaAt(@Param("x") Date s);

    @Query("SELECT b FROM Bill b " + "WHERE b.createdAt >= :start and b.createdAt <= :end")
    Page<Bill> searchByDate(@Param("start") Date start, @Param("end") Date end, Pageable pageable);
    @Query("SELECT b FROM Bill b " + "WHERE b.createdAt >= :start")
    Page<Bill> searchByStartDate(@Param("start") Date start, Pageable pageable);

    @Query("SELECT b FROM Bill b " + "WHERE b.createdAt <= :end")
    Page<Bill> searchByEndDate(@Param("end") Date end, Pageable pageable);

    @Query("SELECT b FROM Bill b WHERE b.createdAt >= :x ")
    List<Bill> searchByDate(@Param("x") Date s);

    ///Đếm số lượng đơn group by MONTH(buyDate)
    //- dùng custom object để build
    // SELECT id, MONTH(buyDate) from bill;
    // select count(*), MONTH(buyDate) from bill
    // group by MONTH(buyDate)
    @Query("SELECT count(b.id), month(b.createdAt)"
            + " FROM Bill b GROUP BY month(b.createdAt) ")
    List<Object[]> thongKeBill();

    @Query("SELECT new com.example.ninhdemo.entities.BillStatistic(count(b.id), month(b.createdAt)) "
            + " FROM Bill b GROUP BY month(b.createdAt) ")
    List<BillStatistic> thongKeBill2();
    @Query("SELECT b FROM Bill b JOIN b.user u WHERE u.email = :email ")
    public List<Bill> findByUser(@Param("email") String email);
}
