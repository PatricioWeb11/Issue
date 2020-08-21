package com.redhat.issue.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.redhat.issue.model.UserModel;

public interface UserRepository extends MongoRepository<UserModel, Long>{
	UserModel findByEmail(String email);
}
