package com.yaps.petstore.customer.dto;


/**
 * A client as exported (and imported) from the service layer.
 * 
 * It could be interesting to distinguish requests (client creation/update) from
 * query results.
 */
public class CustomerDTO {

    // ======================================
    // =             Attributes             =
    // ======================================
    private String id = "";
    private String firstname = "";
    private String lastname = "";
    private String telephone = "";
    private String email = "";
    private final AddressDTO address = new AddressDTO();

    // ======================================
    // =            Constructors            =
    // ======================================
    public CustomerDTO() {
    }

    public CustomerDTO(final String id, final String firstname, final String lastname) {
        setId(id);
        setFirstname(firstname);
        setLastname(lastname);
    }

    // ======================================
    // =         Getters and Setters        =
    // ======================================
    
    

    public String getStreet1() {
        return address.getStreet1();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setStreet1(final String street1) {
        address.setStreet1(street1);
    }

    public String getStreet2() {
        return address.getStreet2();
    }

    public void setStreet2(final String street2) {
        address.setStreet2(street2);
    }

    public String getCity() {
        return address.getCity();
    }

    public void setCity(final String city) {
        address.setCity(city);
    }

    public String getState() {
        return address.getState();
    }

    public void setState(final String state) {
        address.setState(state);
    }

    public String getZipcode() {
        return address.getZipcode();
    }

    public void setZipcode(final String zipcode) {
        address.setZipcode(zipcode);
    }

    public String getCountry() {
        return address.getCountry();
    }

    public void setCountry(final String country) {
        address.setCountry(country);
    }


    public String toString() {
        final StringBuilder buf = new StringBuilder();
        buf.append("CustomerDTO{");
        buf.append("id=").append(getId());
        buf.append(",firstname=").append(getFirstname());
        buf.append(",lastname=").append(getLastname());
        buf.append(",telephone=").append(getTelephone());
        buf.append(",email=").append(getEmail());
        buf.append(",street1=").append(getStreet1());
        buf.append(",street2=").append(getStreet2());
        buf.append(",city=").append(getCity());
        buf.append(",state=").append(getState());
        buf.append(",zipcode=").append(getZipcode());
        buf.append(",country=").append(getCountry());
        buf.append('}');
        return buf.toString();
    }
}
