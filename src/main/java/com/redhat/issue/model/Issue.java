package com.redhat.issue.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;



@Document (collection = "Issue")
public class Issue {
	@Transient
    public static final String SEQUENCE_NAME = "users_sequence";
	
	@Id
	private long id;
	
    @Indexed(unique=true)
	private String title;
    private String description;
    private String email;
    private String project;
    private Date createdAt;
    private int estado;
	
    public Issue() {
    	this.createdAt = new Date();
    }
    
    public Issue(long id, String title, String description, String email, String project) {
		this.id = id;
		this.title = title;
		this.description = description;
		this.email = email;
		this.project = project;
		this.createdAt = new Date();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getProject() {
		return project;
	}

	public void setProject(String project) {
		this.project = project;
	}

	public static String getSequenceName() {
		return SEQUENCE_NAME;
	}
	

	public int getEstado() {
		return estado;
	}

	public void setEstado(int estado) {
		this.estado = estado;
	}

	@Override
	public String toString() {
		return "Issue [id=" + id + ", title=" + title + ", description=" + description + ", email=" + email
				+ ", project=" + project + "]";
	}
    
    
    
    
    
    
}
