package com.example.housesearch.reposity;

import com.example.housesearch.domain.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
}
