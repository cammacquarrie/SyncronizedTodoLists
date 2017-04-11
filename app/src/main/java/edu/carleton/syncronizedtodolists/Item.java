package edu.carleton.syncronizedtodolists;

import java.io.Serializable;
import java.util.ArrayList;

public class Item implements Serializable{
	private int id;
	private String title;
	private String description;
	private String createdBy;
	private boolean completed;
	private int listID;
	ArrayList<User> assigned;
	private int points;

	public Item(int id, String title, String description, String createdBy, int listID) {
		this.id = id;
		this.title = title;
		this.description = description;
		this.createdBy = createdBy;
		this.listID = listID;
		completed = false;
		assigned = new ArrayList<User>();
		points = 0;
	}
	public Item(int id, String title, String description, String createdBy, int listID, int p) {
		this.id = id;
		this.title = title;
		this.description = description;
		this.createdBy = createdBy;
		this.listID = listID;
		completed = false;
		assigned = new ArrayList<User>();
		points = p;
	}

	public void downVote(){

	}
	public void upVote(){
		points++;
	}
	public int getPoints(){
		return points;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
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
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public boolean isCompleted() {
		return completed;
	}
	public void setCompleted(boolean completed) {
		this.completed = completed;
	}
	public int getListID() {
		return listID;
	}
	public void setListID(int listID) {
		this.listID = listID;
	}

	public ArrayList<User> getAssigned() {
		return assigned;
	}

	public void setAssigned(ArrayList<User> assigned) {
		this.assigned = assigned;
	}

	@Override
	public String toString(){
		return title;
	}
}
