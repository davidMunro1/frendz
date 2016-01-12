package beans;

import hibernate.NextUser;
import hibernate.UserProfileEntity;

import javax.ejb.CreateException;
import javax.ejb.Local;
import javax.ejb.SessionContext;
import java.util.List;

/**
 * Created by davidmunro on 14/12/2015.
 */
@Local
public interface LocalUser{

    void initializeSessionBean(int userID);
    int getUSER_ID();
    void setUSER_ID(int userID);
    boolean handleSignUp(String firstName, String lastName, String email, String school, String Authtoken);
    boolean handleLogin(String userName, String password);
    boolean handleConfirmation(String authToken, String email);
    boolean setPassword(String password);
    boolean createProfile(int age, String gender, String soughtGender, String programme, String bio);
    boolean handleEditProfile(String secondName, String password, String programme, String bio);
    UserProfileEntity getProfile();
    UserProfileEntity getUserProfile(int userID);
    boolean addImage(String blobKeyString, int imageNumber);
    String getImage();
    String getImage(int userID);
    List<NextUser> browseAllUsers();
    List<NextUser> getRandomUsers();
    void handleLike(int likedUserID);
    void handleDislike(int dislikedUserID);

}
