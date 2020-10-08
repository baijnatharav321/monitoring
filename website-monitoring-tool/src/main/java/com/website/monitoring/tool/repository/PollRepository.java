package com.website.monitoring.tool.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.website.monitoring.tool.model.Availability;
import com.website.monitoring.tool.model.AvailableStatus;
import com.website.monitoring.tool.model.Check;
import com.website.monitoring.tool.model.DownResponse;
import com.website.monitoring.tool.model.Poll;
import com.website.monitoring.tool.model.UpResponse;

@Repository
public interface PollRepository extends JpaRepository<Poll, Long> {

	List<Poll> findAllByChk(Check chk);

	void deleteAllByChk(Check chk);

	@Query("select new  com.website.monitoring.tool.model.UpResponse(max(time), min(time), availability)  from Poll where availability = ?1 and checkid_fk = ?2")
	Optional<UpResponse> findUpPollByTime(Availability availability, int checkId);

	@Query("select new com.website.monitoring.tool.model.DownResponse(max(time), min(time), availability)  from Poll where availability = ?1 and checkid_fk =?2")
	Optional<DownResponse> findDownPollByTime(Availability availability, int checkId);

	@Query("select distinct availability from Poll where checkid_fk = ?1")
	List<Integer> findAvailability(int checkId);

	@Query("select new com.website.monitoring.tool.model.AvailableStatus(availability , max(time)) from Poll where id = (select max(id) from Poll where checkid_fk = ?1)")
	AvailableStatus findStatus(int checkId);

	@Query("select distinct p.chk.id from Poll p where p.chk.id IN (select id from Check where active = 1)")
	List<Long> findCheckId(boolean active);

}
