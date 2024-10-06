package com.javateam.healthyFoodProject.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.javateam.healthyFoodProject.domain.SocialUser;

public interface SocialUserDAO extends JpaRepository<SocialUser, Long> {
	
	Optional<SocialUser> findByEmail(String strEmail);

}
