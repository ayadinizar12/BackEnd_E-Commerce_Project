package com.sid.demo.dtos;

import com.sid.demo.models.User;

import lombok.Getter;
@Getter
public class UserLoginDTO {

	private Long id;
    private User user;
    private String jwt;

    public UserLoginDTO() {
        super();
    }

    public UserLoginDTO(Long id, User user, String jwt) {
        this.id = id;
        this.user = user;
        this.jwt = jwt;
    }

    public void setId(Long id) {
        this.id = id;
    }

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getJwt() {
		return jwt;
	}

	public void setJwt(String jwt) {
		this.jwt = jwt;
	}

	public Long getId() {
		return id;
	}

    
}
