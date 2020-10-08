package com.website.monitoring.tool.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Entity
@Table(name = "poll")
@Getter
@Setter
@NoArgsConstructor
public class Poll {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	// @JsonIgnore
	private Long id;

	private Availability availability;

	private ZonedDateTime time;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "checkid_fk", nullable = false)
	@JsonIgnore
	private Check chk;

	public Poll(Availability availability, ZonedDateTime time) {
		this.availability = availability;
		this.time = time;
	}

}
