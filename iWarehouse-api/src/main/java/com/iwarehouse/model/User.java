package com.iwarehouse.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_table")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
	
	@Id
	@SequenceGenerator(name = "userseq", sequenceName = "user_seq", initialValue = 201, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "userseq")
	@Column(name = "user_id")
	private Integer userId;
	
	@Column(name = "first_name", nullable = false)
	private String firstName;

	@Column(name = "last_name", nullable = false)
	private String lastName;

	@Column(name = "phone", nullable = false, unique = true)
	private String phone;

	@Column(name = "email", nullable = false, unique = true)
	private String email;

	@Column(name = "passwd", nullable = false)
	private String password;
	
	@Column(name = "pass_reset")
	private Boolean passReset;
	
	@Column(name = "job_title", nullable = false)
	private String jobTitle;
	
	@Column(name = "emp_type", nullable = false)
	private String empType;
	
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
	@JoinColumn(name = "group_id")
	private AccessGroup accessGroup;

	@Column(name = "emp_status", nullable = false)
	private Boolean empStatus;

}
