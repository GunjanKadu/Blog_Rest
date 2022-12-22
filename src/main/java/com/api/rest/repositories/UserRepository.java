package com.api.rest.repositories;

import com.api.rest.BlogUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<BlogUser,Long> {
}
