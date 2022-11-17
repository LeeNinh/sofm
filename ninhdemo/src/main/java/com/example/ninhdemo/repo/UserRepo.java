package com.example.ninhdemo.repo;

import com.example.ninhdemo.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface UserRepo extends JpaRepository<User, Integer> {

    public User findByEmail(String email);
    @Query("SELECT u FROM User u " + "WHERE u.birthdate >= :date")
    List<User> searchByBirthdate(@Param("date") Date s);
    @Query("SELECT u FROM User u WHERE u.name LIKE :x ")
    Page<User> searchByName(@Param("x") String s, Pageable pageable);

    @Query("SELECT u FROM User u " + "WHERE u.birthdate >= :start and u.birthdate <= :end")
    Page<User> searchByDate(@Param("start") Date start, @Param("end") Date end, Pageable pageable);

    @Query("SELECT u FROM User u " + "WHERE u.birthdate >= :start")
    Page<User> searchByStartDate(@Param("start") Date start, Pageable pageable);

    @Query("SELECT u FROM User u " + "WHERE u.birthdate <= :end")
    Page<User> searchByEndDate(@Param("end") Date end, Pageable pageable);

    @Query("SELECT u FROM User u WHERE u.name LIKE :x AND u.birthdate >= :start AND u.birthdate <= :end")
    Page<User> searchByNameAndDate(@Param("x") String s, @Param("start") Date start, @Param("end") Date end,
                                   Pageable pageable);

    @Query("SELECT u FROM User u WHERE u.name LIKE :x AND u.birthdate >= :start")
    Page<User> searchByNameAndStartDate(@Param("x") String s, @Param("start") Date start, Pageable pageable);

    @Query("SELECT u FROM User u WHERE u.name LIKE :x AND u.birthdate <= :end")
    Page<User> searchByNameAndEndDate(@Param("x") String s, @Param("end") Date end, Pageable pageable);


    User findByUsername( String username);

}