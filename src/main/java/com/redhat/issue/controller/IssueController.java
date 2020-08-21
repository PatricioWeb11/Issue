package com.redhat.issue.controller;

import java.util.HashMap;

//import jakarta.validation.Validated;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.redhat.issue.repository.IssueRepository;

import com.redhat.issue.service.SequenceGeneratorService;
import com.google.gson.Gson;
import com.redhat.issue.exception.ResourceNotFoundException;
import com.redhat.issue.model.Issue;
import com.redhat.issue.model.Noti;
import org.springframework.web.bind.annotation.RequestMethod;


//@CrossOrigin(origins = "http://localhost:8080")
@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT,
RequestMethod.DELETE })
@RestController
@RequestMapping("/api/v1")
public class IssueController {
	@Autowired
	private IssueRepository issueRepository;
	
	@Autowired
	private SequenceGeneratorService sequenceGeneratorService;
	
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	
	@GetMapping("/issues")
	public List<Issue> getAllIssue() {
		/*
		Query query = new Query();
		query.addCriteria(Criteria.where("name").regex("^A"));
		List<Issue> users = issueRepository.findAll(query,Issue.class);
		*/
		return issueRepository.findAll();
	}
	
	
	@GetMapping("/issue/{id}")
	public ResponseEntity<Issue> getIssueByid(@PathVariable(value = "id") Long issueId)
			throws ResourceNotFoundException {
		Issue issue = issueRepository.findById(issueId)
				.orElseThrow(() -> new ResourceNotFoundException("Issue not found for this id :: " + issueId));
		return ResponseEntity.ok().body(issue);
	}
	
	
	
	@PostMapping("/create")
	public Issue createIssue(@Validated @RequestBody Issue issue) {
		issue.setId(sequenceGeneratorService.generateSequence(Issue.SEQUENCE_NAME));
		storeNotification(0, issue);
		return issueRepository.save(issue);
	}
	
	//http://notifications-app-notifications-app.apps.na311.openshift.opentlc.com
	@PostMapping("http://notifications-app-notifications-app.apps.na311.openshift.opentlc.com/api/notifications/")
	public void storeNotification(@PathVariable int typeMsj, @RequestBody Issue issueDetails) {
		Gson g = new Gson();
		String notification = "";
		int typeUser = 1;
		String title = issueDetails.getTitle();
		String descripcion = issueDetails.getDescription();
		String email = issueDetails.getEmail();
		if (typeMsj == 0) {
			notification = "The title "+title+" has created a new issue: "+descripcion;
		} else if (typeMsj == 1) {
			notification = "The title "+title+" has modified an issue: "+descripcion;
		} else {
			notification = "The title "+title+" has deleted an issue: "+descripcion;
		}
		System.out.println(notification+"esta es la notificacion final");
		Noti noti = new Noti(typeUser, email, notification);
		System.out.println(g.toJson(noti));
		String url = "http://notifications-app-notifications-app.apps.na311.openshift.opentlc.com/api/notifications/";
		Map<String, String> params = new HashMap<>(); // put here your params.
		RestTemplate template = new RestTemplate();
		template.postForLocation(url, noti, params);
	}
	
	
	
	@PutMapping("/issue/{id}")
	public ResponseEntity<Issue> updateIssue(@PathVariable(value = "id") Long issueId,
			@Validated @RequestBody Issue issueDetails) throws ResourceNotFoundException {
		Issue issue = issueRepository.findById(issueId)
				.orElseThrow(() -> new ResourceNotFoundException("Issue not found for this id :: " + issueId));

		
		issue.setTitle(issueDetails.getTitle());
		issue.setEmail(issueDetails.getEmail());
		issue.setProject(issueDetails.getProject());
		issue.setEstado(issueDetails.getEstado());
		issue.setDescription(issueDetails.getDescription());
		
		final Issue updatedIssue = issueRepository.save(issue);
		storeNotification(1, issue);
		return ResponseEntity.ok(updatedIssue);
	}
	
	
	
	@DeleteMapping("/issue/{id}")
	public Map<String, Boolean> deleteIssue(@PathVariable(value = "id") Long issueId)
			throws ResourceNotFoundException {
		Issue issue = issueRepository.findById(issueId)
				.orElseThrow(() -> new ResourceNotFoundException("Issue not found for this id :: " + issueId));
		issue.setEstado(3);
		issueRepository.save(issue);
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		storeNotification(2, issue);
		return response;
	}
	
	
	
	
	
	
	
}
