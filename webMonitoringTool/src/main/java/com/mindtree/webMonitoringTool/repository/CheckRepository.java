package com.mindtree.webMonitoringTool.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mindtree.webMonitoringTool.entity.CheckWeb;

@Repository
public interface CheckRepository extends JpaRepository<CheckWeb,Integer>{

}
