package com.website.monitoring.tool.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.website.monitoring.tool.model.Check;

@Repository
public interface WebCheckRepository extends JpaRepository<Check, Long> {

	@Query("select c from Check c where c.user.userid = ?1 and c.id = ?2 and c.active = ?3")
	Optional<Check> findByUserId(String userId, long id, boolean active);

	@Query("select c from Check c where c.user.userid = ?1 and c.id = ?2 and c.active = ?3")
	Optional<Check> findCheckByIdStatus(String userId, long id, boolean active);

	@Query("select c from Check c where c.user.userid = ?1 and c.frequencyinterval = ?2 and c.active = ?3")
	List<Check> findByFrequencyinterval(String userId, int frequencyinterval, boolean active);

	@Query("select c from Check c where c.user.userid = ?1 and c.websitename like %?2% and c.active = ?3")
	List<Check> findByWebsiteName(String userId, String name, boolean active);

	@Query("select c from Check c where c.user.userid = ?1 and c.websitename = ?2 and c.active = ?3")
	Optional<Check> findCheckByWebsiteName(String userId, String name, boolean active);

}
