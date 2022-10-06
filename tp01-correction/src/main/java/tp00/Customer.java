package tp00;

public class Customer {
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
    private String mail;

    public Customer(String id, String firstname, String lastname) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
    }

    public Customer(String id, String firstname, String lastname, String telephone, String street1, String street2,
            String city, String state, String zipcode, String country, String mail) {
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
        this.mail = mail;
    }

    public String getId() {
        return id;
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

    public String getStreet1() {
        return street1;
    }

    public void setStreet1(String street1) {
        this.street1 = street1;
    }

    public String getStreet2() {
        return street2;
    }

    public void setStreet2(String street2) {
        this.street2 = street2;
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

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public boolean checkId() {
        return isNotEmpty(id);
    }

    public boolean checkData() {
        return isNotEmpty(firstname) && isNotEmpty(lastname) && checkId();
        
    }

    public String getCheckDataError() {
        if (! checkId()) {
            return "Invalid id";
        } else if (! isNotEmpty(firstname)) {
            return "Invalid first name";
        } else if (! isNotEmpty(lastname)) {
            return "Invalid last name";
        } else {
            return "";
        }
    }

    public boolean checkMail() {
        String[] parts = mail.split("@");
        if (parts.length != 2) {
            return false;
        } else {
            String domain = parts[1];
            String recipient = parts[0];
            if (!domain.contains("."))
                return false;
            if (recipient.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    private static boolean isNotEmpty(String s) {
        return s != null && ! s.isEmpty();
    }
}
