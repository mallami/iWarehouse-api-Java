package com.iwarehouse.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo {

	private Integer userId;
	
	private String fullName;

	private String email;

	private String jobTitle;

}
