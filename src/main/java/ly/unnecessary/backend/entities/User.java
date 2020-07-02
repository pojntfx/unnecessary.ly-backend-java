package ly.unnecessary.backend.entities;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue
    private long id;

    private String displayName;

    @Column(unique = true)
    private String email;

    private String password;

    @OneToOne(cascade = CascadeType.ALL)
    private UserSignUpRequest userSignupRequest;

    @OneToOne(cascade = CascadeType.ALL)
    private UserPasswordResetRequest userPasswordResetRequest;

    @OneToMany(mappedBy = "owner")
    private List<Community> ownedCommunities;

    @ManyToMany(mappedBy = "members")
    private List<Community> memberCommunities;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserSignUpRequest getUserSignupRequest() {
        return userSignupRequest;
    }

    public void setUserSignupRequest(UserSignUpRequest userSignupRequest) {
        this.userSignupRequest = userSignupRequest;
    }

    public UserPasswordResetRequest getUserPasswordResetRequest() {
        return userPasswordResetRequest;
    }

    public void setUserPasswordResetRequest(UserPasswordResetRequest userPasswordResetRequest) {
        this.userPasswordResetRequest = userPasswordResetRequest;
    }
}