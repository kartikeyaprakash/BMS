package com.cg.challenge4.webapp.model;


// TODO: Auto-generated Javadoc
/**
 * To string.
 *
 * @return the java.lang. string
 */


/**
 * Instantiates a new address.
 *
 * @param addressLine1 the address line 1
 * @param addressLine2 the address line 2
 * @param city the city
 * @param state the state
 * @param zipcode the zipcode
 */


/**
 * Instantiates a new address.
 */

public class Address {

	
	/** The address line 1. */
	private String addressLine1;
	
	/** The address line 2. */
	private String addressLine2;
	
	/** The city. */
	private String city;
	
	/** The state. */
	private String state;
	
	/** The zipcode. */
	private int zipcode;

	public String getAddressLine1() {
		return addressLine1;
	}

	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}

	public String getAddressLine2() {
		return addressLine2;
	}

	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public int getZipcode() {
		return zipcode;
	}

	public void setZipcode(int zipcode) {
		this.zipcode = zipcode;
	}

	public Address(String addressLine1, String addressLine2, String city, String state, int zipcode) {
		super();
		this.addressLine1 = addressLine1;
		this.addressLine2 = addressLine2;
		this.city = city;
		this.state = state;
		this.zipcode = zipcode;
	}

	public Address() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
}
