package edu.carleton.syncronizedtodolists;


import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable{
	private ArrayList<List> lists;
	private ArrayList<Item> items;
	private String userName;
	private String displayName;
	private String password;
	
	public User(String userName, String displayName, String password) {
		this.userName = userName;
		this.displayName = displayName;
		this.password = password;
		lists = new ArrayList<List>();
		items = new ArrayList<Item>();
	}
	public User(String userName, String displayName) {
		this.userName = userName;
		this.displayName = displayName;
	}
	public User(String userName) {
		this.userName = userName;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	private String getPassword() {
		return password;
	}
	public boolean equals(User user){
		return user.getUserName().equals(userName);
	}
	public ArrayList<List> getLists() {
		return lists;
	}
	public void setLists(ArrayList<List> lists) {
		this.lists = lists;
	}
	public ArrayList<Item> getItems() {
		return items;
	}
	public void setItems(ArrayList<Item> items) {
		this.items = items;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}
