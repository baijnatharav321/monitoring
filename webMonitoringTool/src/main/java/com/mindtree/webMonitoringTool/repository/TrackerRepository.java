package com.mindtree.webMonitoringTool.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mindtree.webMonitoringTool.entity.Tracker;

@Repository
public interface TrackerRepository extends JpaRepository<Tracker,Integer>{

}
