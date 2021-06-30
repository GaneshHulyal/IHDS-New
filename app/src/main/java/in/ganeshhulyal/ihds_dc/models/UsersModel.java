package in.ganeshhulyal.ihds_dc.models;

public class UsersModel {

    private int mobileNumber;
    private String firstName;
    private String lastName;
    private String email;
    private String password;

    public UsersModel(int mobileNumber, String firstName, String lastName, String email, String password) {
        this.mobileNumber = mobileNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    public int getMobileNumber() {
        return mobileNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

}
