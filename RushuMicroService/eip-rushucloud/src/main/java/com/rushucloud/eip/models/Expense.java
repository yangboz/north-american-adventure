package com.rushucloud.eip.models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "expenses")
public class Expense extends ModelBase {
	//
	public enum ExpenseStatus {
		Approved, Saved, Submitted, Rejected, Completed
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

	// The expenses money amount(of expense item)
	@NotNull
	private double amount;

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	// The expense name
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
	private ExpenseStatus status;

	public ExpenseStatus getStatus() {
		return status;
	}

	public void setStatus(ExpenseStatus status) {
		this.status = status;
	}

	// The expense owner name or id.
	@NotNull
	private String owner;

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	// report manager id.
	@NotNull
	private String managerId;

	public String getManagerId() {
		return managerId;
	}

	public void setManagerId(String managerId) {
		this.managerId = managerId;
	}

	// The expense involves,id strings
	// @CollectionOfElements,@see:
	// https://jazzy.id.au/2008/03/24/jpa_2_0_new_features_part_1.html
	private String participantIds;// sort of participant IDs:"1,2,3,4"

	public String getParticipantIds() {
		return participantIds;
	}

	public void setParticipantIds(String participantIds) {
		this.participantIds = participantIds;
	}

	//
	@NotNull
	private String itemIds;// sort of expense item IDs:"1,2,3,4"

	public String getItemIds() {
		return itemIds;
	}

	public void setItemIds(String itemIds) {
		this.itemIds = itemIds;
	}

	// ==============
	// PUBLIC METHODS
	// ==============
	public Expense() {
	}

	public Expense(long id) {
		this.id = id;
	}

	public Expense(double amount, String name, ExpenseStatus status, Date date,
			String owner, String managerId, String participantIds,
			String itemIds) {
		this.amount = amount;
		this.name = name;
		this.status = status;
		this.date = date;
		this.owner = owner;
		this.managerId = managerId;
		this.participantIds = participantIds;
		this.itemIds = itemIds;
	}
}
