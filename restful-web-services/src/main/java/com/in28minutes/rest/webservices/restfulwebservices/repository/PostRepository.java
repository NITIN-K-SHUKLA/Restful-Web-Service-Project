package com.in28minutes.rest.webservices.restfulwebservices.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.in28minutes.rest.webservices.restfulwebservices.entities.Post;

public interface PostRepository extends JpaRepository<Post, Integer>{

//	List<Post> findByUserId(Integer userId);
}
