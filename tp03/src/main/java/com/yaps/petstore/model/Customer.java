package com.yaps.petstore.model;

import com.yaps.petstore.exceptions.ValidationException;
import com.yaps.petstore.validation.Validator;
import com.yaps.petstore.validation.annotation.IsNotEmpty;
import com.yaps.petstore.validation.annotation.ContainsOnly;

/**
 * This class represents a customer for the YAPS company.
 */
public final class Customer  {

	// ======================================
	// = Attributes =
	// ======================================
	private String id;
	private String firstname;
	private String lastname;
	private String telephone;
	private String street1;
	private String street2;
	private String city;
	private String state;
	private String zipcode;
	private String country;

	// ======================================
	// = Constructors =
	// ======================================
	public Customer() {
	}

	public Customer(final String id) {
		this.id = id;
	}

	/**
	 * Copy constructor.
	 * (cheap but dangerous alternative : use clone())
	 * 
	 * @param other
	 */
	public Customer(Customer other) {
		this(other.id, other.firstname, other.lastname, other.telephone,
				other.street1, other.street2, other.city,
				other.state, other.zipcode, other.country);
	}

	/**
	 * @param id
	 * @param firstname
	 * @param lastname
	 */
	public Customer(final String id, final String firstname, final String lastname) {
		this.id = id;
		this.firstname = firstname;
		this.lastname = lastname;
	}

	/**
	 * @param id
	 * @param firstname
	 * @param lastname
	 * @param telephone
	 * @param street1
	 * @param street2
	 * @param city
	 * @param state
	 * @param zipcode
	 * @param country
	 */

	public Customer(String id, String firstname, String lastname, String telephone, String street1, String street2,
			String city, String state, String zipcode, String country) {
		this.id = id;
		this.firstname = firstname;
		this.lastname = lastname;
		this.telephone = telephone;
		this.street1 = street1;
		this.street2 = street2;
		this.city = city;
		this.state = state;
		this.zipcode = zipcode;
		this.country = country;
	}

	// ======================================
	// = check methods =
	// ======================================
	/**
	 * This method checks the integrity of the object data.
	 * 
	 * DO NOT MODIFY THIS METHOD !
	 * @throws ValidationException if data is invalid
	 */
	public void checkData() throws ValidationException {	
		new Validator().validateObject(this);
	}
	

	@IsNotEmpty(message = "Invalid customer id")
	public String getId() {
		return this.id;
	}

	@IsNotEmpty(message = "Invalid customer first name")
	public String getFirstname() {
		return this.firstname;
	}

	public void setFirstname(final String firstname) {
		this.firstname = firstname;
	}
	@IsNotEmpty(message = "Invalid customer last name")
	public String getLastname() {
		return this.lastname;
	}

	public void setLastname(final String lastname) {
		this.lastname = lastname;
	}

	@ContainsOnly(value = "0123456789 ", message = "telephone should only contain digits")
	public String getTelephone() {
		return this.telephone;
	}

	public void setTelephone(final String telephone) {
		this.telephone = telephone;
	}

	public String getStreet1() {
		return this.street1;
	}

	public void setStreet1(final String street1) {
		this.street1 = street1;
	}

	public String getStreet2() {
		return this.street2;
	}

	public void setStreet2(final String street2) {
		this.street2 = street2;
	}

	public String getCity() {
		return this.city;
	}

	public void setCity(final String city) {
		this.city = city;
	}

	public String getState() {
		return this.state;
	}

	public void setState(final String state) {
		this.state = state;
	}

	public String getZipcode() {
		return this.zipcode;
	}

	public void setZipcode(final String zipcode) {
		this.zipcode = zipcode;
	}

	public String getCountry() {
		return this.country;
	}

	public void setCountry(final String country) {
		this.country = country;
	}

	public String toString() {
		final StringBuilder buf = new StringBuilder();
		buf.append("\n\tCustomer {");
		buf.append("\n\t\tId=").append(this.id);
		buf.append("\n\t\tFirst Name=").append(this.firstname);
		buf.append("\n\t\tLast Name=").append(this.lastname);
		buf.append("\n\t\tTelephone=").append(this.telephone);
		buf.append("\n\t\tStreet 1=").append(this.street1);
		buf.append("\n\t\tStreet 2=").append(this.street2);
		buf.append("\n\t\tCity=").append(this.city);
		buf.append("\n\t\tState=").append(this.state);
		buf.append("\n\t\tZipcode=").append(this.zipcode);
		buf.append("\n\t\tCountry=").append(this.country);
		buf.append("\n\t}");
		return buf.toString();
	}

}
