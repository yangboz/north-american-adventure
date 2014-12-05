package com.rushucloud.eip.models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Type;

@MappedSuperclass
public class ModelBase {
//	@Version
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created", nullable = false)
	@Type(type="org.joda.time.contrib.hibernate.PersistentDateTime")
	private Date created;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updated", nullable = false)
	@Type(type="org.joda.time.contrib.hibernate.PersistentDateTime")
	private Date updated;

	@PrePersist
	protected void onCreate() {
		updated = created = new Date();
	}

	@PreUpdate
	protected void onUpdate() {
		updated = new Date();
	}

	public Date getCreated() {
		return created;
	}

	public Date getUpdated() {
		return updated;
	}
}
