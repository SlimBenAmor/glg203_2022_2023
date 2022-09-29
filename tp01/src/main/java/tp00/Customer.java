package tp00;
import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        this.setFirstname(firstname);
        this.setLastname(lastname);
    }

    

    public Customer(String id, String firstname, String lastname, String telephone, String street1, String street2,
            String city, String state, String zipcode, String country, String mail) {
        this.id = id;
        this.setFirstname(firstname);
        this.setLastname(lastname);
        this.setTelephone(telephone);
        this.setStreet1(street1);
        this.setStreet2(street2);
        this.setCity(city);
        this.setState(state);
        this.setZipcode(zipcode);
        this.setCountry(country);
        this.setMail(mail);
    }
    
    public String getId() {
        return this.id;
    }

    public String getFirstname() {
        return this.firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return this.lastname;
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

    private boolean checkStr(String string) {
        if (string == null || string.isEmpty() || string.trim().isEmpty()) {
            return false;
        }
        return true;
    }

    public boolean checkId() {
        return this.checkStr(this.id);
    }

    public boolean checkData() {
        Field[] declaredFields = this.getClass().getDeclaredFields();
        try{
            for (Field field : declaredFields) {
                if(!this.checkStr((String) field.get(this))) {
                    return false;
                }
            }
        }
        catch(IllegalAccessException e){
            return false;
        }
        return true;
        
    }

    public String getCheckDataError() {
        Field[] declaredFields = this.getClass().getDeclaredFields();
        try{
            for (Field field : declaredFields) {
                if (!this.checkStr((String) field.get(this))) {
                    String varName = field.getName();
                    if (varName.endsWith("name")) {
                        varName = varName.substring(0,varName.length() - 4) + " name";
                    }
                    return "Invalid "+varName;
                }
            }
        }
        catch(IllegalAccessException e){
            return "Invalid ";
        }
        return "Valid";
    }

    public boolean checkMail() {
        String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(getMail());
        return matcher.matches();
    }

}
