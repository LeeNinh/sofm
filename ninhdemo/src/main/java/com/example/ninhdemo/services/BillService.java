package com.example.ninhdemo.services;

import com.example.ninhdemo.entities.Bill;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.List;

@Service // dung entityManager để viết thêm hàm mình muốn
public class BillService {

    @PersistenceContext
    EntityManager entityManager;

    @SuppressWarnings("unchecked")
    public List<Bill> searchByDate(@Param("x") Date d) {
        String jpql = "SELECT u FROM Bill u WHERE u.createdAt >= :x ";
        return entityManager.createQuery(jpql)
                .setParameter("x", d)
                .getResultList();
    }

}