package com.rushucloud.eip.models;

import java.util.ArrayList;
import java.util.Date;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "items")
public class Item extends ModelBase {
	// ApproveAhead,CostComsumed
	public enum ItemType {
		ApproveAhead, CostComsumed
	}

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

	// The item money amount
	@NotNull
	private double amount;

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	// The item name
	@NotNull
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	// The item owner name or id.
	@NotNull
	private String owner;

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	// The item type
	@Enumerated(EnumType.STRING)
	private ItemType type;

	public ItemType getType() {
		return type;
	}

	public void setType(ItemType type) {
		this.type = type;
	}

	// The item invoices
	// @CollectionOfElements,@see:
	// https://jazzy.id.au/2008/03/24/jpa_2_0_new_features_part_1.html
	private String invoices = "1";// sort of invoice ids:"1,2,3,4"

	public String getInvoices() {
		return invoices;
	}

	public void setInvoices(String invoices) {
		this.invoices = invoices;
	}

	// The item vendors
	private String vendors = "1";// sort of vendor ids:"1,2,3,4"

	public String getVendors() {
		return vendors;
	}

	public void setVendors(String vendors) {
		this.vendors = vendors;
	}

	// The item date
	@NotNull
	private Date date;

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	// ==============
	// PUBLIC METHODS
	// ==============
	public Item() {
	}

	public Item(long id) {
		this.id = id;
	}

	public Item(double amount, String name, ItemType type, Date date,
			String invoices,String vendors,String owner) {
		this.amount = amount;
		this.name = name;
		this.type = type;
		this.date = date;
		this.invoices = invoices;
		this.vendors = vendors;
		this.owner = owner;
	}
}
