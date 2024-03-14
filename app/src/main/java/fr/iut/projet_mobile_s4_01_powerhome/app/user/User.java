package fr.iut.projet_mobile_s4_01_powerhome.app.user;

public class User {
    private Integer id;
    private String firstname;
    private String lastname;
    private String email;
    private String password;

    public User(Integer id, String firstname, String lastname, String email, String password) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

}
