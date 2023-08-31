package com.api.rest.repositories;

import com.api.rest.BlogUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<BlogUser,Long> {
    Optional<BlogUser> findByEmail(String email);
}
