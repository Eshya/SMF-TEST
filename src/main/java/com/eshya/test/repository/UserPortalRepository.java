package com.eshya.test.repository;

import com.eshya.test.model.UserPortal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserPortalRepository extends JpaRepository<UserPortal, Long> {
    Optional<UserPortal> findByUsername(String username);
    @Query("SELECT DISTINCT u FROM UserPortal u LEFT JOIN FETCH u.role WHERE u.username = :username")
    Optional<UserPortal> findByUsernameWithRoles(@Param("username") String username);

    Optional<Object> findByToken(String token);
}
