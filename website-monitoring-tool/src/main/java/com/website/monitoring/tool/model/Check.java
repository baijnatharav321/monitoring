package com.website.monitoring.tool.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
//@AllArgsConstructor
@ToString
@Entity
@Table(name = "webcheck")
//@Valid
@Component
public class Check {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@Column(name = "websitename")
	@NotNull(message = "Website Name should not be null....")
	@NotBlank(message = "Website Name should not be Blank....")
	private String websitename;
	@Column(name = "websiteurl")
	@NotNull(message = "Website URL should not be null....")
	@NotBlank(message = "Website URL should not be Blank....")
	private String websiteurl;
	@Column(name = "frequencytype", nullable = false)
	@NotNull(message = "Frequency Type should not be null....")
	@NotBlank(message = "Frequency Type should not be Blank....")
	private String frequencytype;
	@Column(name = "frequencyinterval", nullable = false)
	@Range(min = 1, max = 60, message = "Frequency interval should be within 1 to 60")
	private int frequencyinterval;
	@Column(name = "active", nullable = false)
	private boolean active;
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "userid_fk", nullable = false)
	@JsonIgnore
	private User user;

	@OneToMany(mappedBy = "chk", fetch = FetchType.LAZY)
	@JsonIgnore
	@Getter(value = AccessLevel.NONE)
	private Set<Poll> Polls = new HashSet<>();

}
