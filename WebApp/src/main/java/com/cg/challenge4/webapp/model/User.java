package com.cg.challenge4.webapp.model;




public class User {
	private String firstName;
	private String lastName;
	private String password;
	
	/** The is vaccinated. */
	private boolean isVaccinated;
	
	/** The address. */
//	private Address address;
	
	
	private String email;
	
	
	private String phoneNumber;
	
	private Address address;


	public String getFirstName() {
		return firstName;
	}


	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}


	public String getLastName() {
		return lastName;
	}


	public void setLastName(String lastName) {
		this.lastName = lastName;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public boolean isVaccinated() {
		return isVaccinated;
	}


	public void setVaccinated(boolean isVaccinated) {
		this.isVaccinated = isVaccinated;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getPhoneNumber() {
		return phoneNumber;
	}


	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	


	public Address getAddress() {
		return address;
	}


	public void setAddress(Address address) {
		this.address = address;
	}


	public User(String firstName, String lastName, String password, boolean isVaccinated, String email,
			String phoneNumber, Address address) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.password = password;
		this.isVaccinated = isVaccinated;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.address = address;
	}


	public User(String firstName, String lastName, String password, boolean isVaccinated, String email,
			String phoneNumber) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.password = password;
		this.isVaccinated = isVaccinated;
		this.email = email;
		this.phoneNumber = phoneNumber;
	}


	public User() {
		super();
	}
	
	
	
	
	
	

}
