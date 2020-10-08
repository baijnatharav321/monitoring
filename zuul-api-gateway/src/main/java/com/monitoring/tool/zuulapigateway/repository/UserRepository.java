package com.monitoring.tool.zuulapigateway.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.monitoring.tool.zuulapigateway.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

	Optional<User> findUserByUserid(String userid);

}
