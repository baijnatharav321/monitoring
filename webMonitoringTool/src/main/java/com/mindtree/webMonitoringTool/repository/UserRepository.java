package com.mindtree.webMonitoringTool.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mindtree.webMonitoringTool.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {

}
