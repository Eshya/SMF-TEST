package com.eshya.test.model;

import com.eshya.test.utils.Constant;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Setter
@Getter
@Entity
@Table(name = "user_portal", schema = Constant.API_NAME)
public class UserPortal {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "portal_sequence")
	@SequenceGenerator(name = "portal_sequence", sequenceName = "portal_sequence", allocationSize = 1, initialValue = 1)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "username")
	private String username;
	
	@Column(name = "password")
	private String password;
	
	@Column(name = "status")
	private String status;


	@Column(name = "creation_date")
	private Date creationDate;
	
	@Column(name = "modified_date")
	private Date modifiedDate;


	@Column(name = "token" , length = 1000)
	private String token;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "role_id")
	private Role role;
}
