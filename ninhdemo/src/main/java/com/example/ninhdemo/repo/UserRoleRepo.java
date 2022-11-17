package com.example.ninhdemo.repo;

import com.example.ninhdemo.entities.UserRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleRepo extends JpaRepository<UserRole,Integer> {
    @Query(value = "SELECT * FROM User_Role WHERE id = :id", nativeQuery = true)
    Page<UserRole> searchById(@Param("id") String n, Pageable pageable);
    @Query(value = "SELECT * FROM User_Role WHERE role LIKE ('%' :role '%')", nativeQuery = true)
    Page<UserRole> searchByRole(@Param("role") String n, Pageable pageable);
    @Query(value = "SELECT u.* FROM User_Role u WHERE u.user_id = :userId", nativeQuery = true)
    Page<UserRole> searchByUserId(@Param("userId") int id,
                                  Pageable pageable);
}

