package beans;

import hibernate.UserProfileEntity;

import javax.ejb.CreateException;
import javax.ejb.Local;
import javax.ejb.SessionContext;

/**
 * Created by davidmunro on 14/12/2015.
 */
@Local
public interface LocalUser{

    void initializeSessionBean(int userID);
    int getUSER_ID();
    void setUSER_ID(int userID);
    boolean handleSignUp(String firstName, String lastName, String email, String school, byte confirmed, String Authtoken);
    boolean handleLogin(String userName, String password);
    boolean handleConfirmation(String authToken, String email, String password);
    boolean createProfile(int age, String gender, String soughtGender, String programme, String bio);
    UserProfileEntity getProfile();
    UserProfileEntity getUserProfile(int userID);

}
