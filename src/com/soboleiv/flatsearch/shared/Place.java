package com.soboleiv.flatsearch.shared;

import java.io.Serializable;


public class Place implements Serializable{
	public static final Place INVALID = new Place();
	
	private String originalUrl;
	private String address;
	private Location coordinates;
	private String postingDate;
	private String price;
	private int roomsNumber;
	public String getOriginalUrl() {
		return originalUrl;
	}
	public void setOriginalUrl(String originalUrl) {
		this.originalUrl = originalUrl;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public Location getCoordinates() {
		return coordinates;
	}
	public void setCoordinates(Location coordinates) {
		this.coordinates = coordinates;
	}
	public String getPostingDate() {
		return postingDate;
	}
	public void setPostingDate(String postingDate) {
		this.postingDate = postingDate;
	}
	public int getRoomsNumber() {
		return roomsNumber;
	}
	public void setRoomsNumber(int roomsNumber) {
		this.roomsNumber = roomsNumber;
	}	
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
}