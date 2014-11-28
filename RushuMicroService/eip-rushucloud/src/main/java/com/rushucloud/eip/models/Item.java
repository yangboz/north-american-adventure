package com.rushucloud.eip.models;

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
	// @CollectionOfElements

	// ==============
	// PUBLIC METHODS
	// ==============
	public Item() {
	}

	public Item(long id) {
		this.id = id;
	}

	public Item(double amount, String name, ItemType type) {
		this.amount = amount;
		this.name = name;
		this.type = type;
	}
}
