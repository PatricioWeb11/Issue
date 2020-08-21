package com.redhat.issue.repository;

import org.springframework.stereotype.Repository;
import com.redhat.issue.model.Issue;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

@Repository
public interface IssueRepository extends MongoRepository<Issue, Long> {
	//@Query("{ 'estado' : ?0 }")
	//List<Issue> findByState(int state);
}
