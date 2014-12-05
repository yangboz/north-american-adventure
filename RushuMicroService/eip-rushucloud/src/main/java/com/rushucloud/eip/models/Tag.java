package com.rushucloud.eip.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "tags")
public class Tag extends ModelBase {
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

	// The category icon
	@NotNull
	private String icon;

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	// The category name
	@NotNull
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	// Foreign key(item_id)
//	@CollectionTable(name = "tag_items", joinColumns = @JoinColumn(name = "idtag"))
	@NotNull
	private long itemId;
	public long getItemId() {
		return itemId;
	}

	public void setItemId(long itemId) {
		this.itemId = itemId;
	}

	// ==============
	// PUBLIC METHODS
	// ==============

	public Tag() {
	}

	public Tag(long id) {
		this.id = id;
	}

	public Tag(String icon, String name, long itemId) {
		this.icon = icon;
		this.name = name;
		this.itemId = itemId;
	}
}
