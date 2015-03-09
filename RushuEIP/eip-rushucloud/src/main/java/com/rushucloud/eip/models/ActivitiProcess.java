/**
 * @author yangboz
 * @see http://blog.netgloo.com/2014/10/27/using-mysql-in-spring-boot-via-spring-data-jpa-and-hibernate/
 */
package com.rushucloud.eip.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "activiti_processes")
public class ActivitiProcess {

	// ==============
	// PRIVATE FIELDS
	// ==============

	// An auto-generated id (unique for each user in the db)
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	public long getId() {
		return id;
	}

	public void setId(long value) {
		this.id = value;
	}

	// The company id
	@NotNull
	private long companyId;

	public long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(long companyId) {
		this.companyId = companyId;
	}

	// The process name as Activiti process instance key.
	@NotNull
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	// The BPM artifact of Activiti,xxx.bpmn20.xml+xxx.bpmn20.png or xxx.bpmn20.bar
	@NotNull
	private String bpm;

	public String getBpm() {
		return bpm;
	}

	public void setBpm(String bpm) {
		this.bpm = bpm;
	}

	// ==============
	// PUBLIC METHODS
	// ==============

	public ActivitiProcess() {
	}

	public ActivitiProcess(long id) {
		this.id = id;
	}

	public ActivitiProcess(String name, String bpm) {
		this.bpm = bpm;
		this.name = name;
	}
}

