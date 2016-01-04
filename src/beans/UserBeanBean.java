package beans;

import Helper.HashHelper;
import hibernate.FrendzHibernateUtil;
import hibernate.UserEntity;
import hibernate.UserProfileEntity;
import org.hibernate.*;

import javax.ejb.*;
import javax.ejb.CreateException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.SessionContext;

/**
 * Created by davidmunro on 11/12/2015.
 */
@Stateful(name = "UserSession")
@Local
public class UserBeanBean implements LocalUser, Serializable{

    private int USER_ID;
    private SessionContext sessionContext;
    private static final long serialVersionUID = 1L;

    private SessionFactory sessionFactory = FrendzHibernateUtil.getSessionFactory();

    public UserBeanBean() {}

    @Override
    public void initializeSessionBean(int userID){
        this.USER_ID = userID;
    }

    @Override
    public int getUSER_ID(){
        return USER_ID;
    }

    @Override
    public void setUSER_ID(int userID) {
        this.USER_ID = userID;
    }

    @Override
    public boolean handleSignUp(String firstName, String lastName, String email, String school, byte confirmed, String authToken) {
        boolean success = false;

        Session session = sessionFactory.openSession();

        Transaction tx;
        UserEntity user = new UserEntity();
        try {
            tx = session.beginTransaction();
            user.setFirstName(firstName);
            user.setSecondName(lastName);
            user.setEmail(email);
            user.setAuthorisationToken(authToken);
            user.setSchool(school);
            user.setConfirmed(confirmed);
            session.save(user);
            System.out.println("user id :" +user.getId());
            setUSER_ID(user.getId());
            tx.commit();
            success = true;
        }catch (Exception ee){
            System.out.println("Error adding to DB : " +ee.getMessage());
        }finally {
            session.close();
        }
        return success;
    }

    @Override
    public boolean handleLogin(String email, String password) {
        boolean authorise = false;
        //TODO: after sign up is created, change to use hashed pass
        String hashedPass = HashHelper.createHash(password);

        Session session = sessionFactory.openSession();

        try{
            Query q2 = session.createQuery("from UserEntity as u where u.email = :email and u.password = :password");
            q2.setString("email", email);
            q2.setString("password", password);
            //q2.setString("password", hashedPass);
            List list = q2.list();
            if(list.size() == 1){
                UserEntity user = (UserEntity)list.get(0);
                setUSER_ID(user.getId());

                if(user.getConfirmed()==1){
                    authorise = true;
                }
                else if(user.getConfirmed()==0){
                    authorise = false;
                }
            }
            else if(list.size() == 0){
                System.out.println("BEAN - Wrong email or password");
                authorise = false;
            }

        }catch(RuntimeException ee){
            System.out.println("Error in log in");
            ee.printStackTrace();
        }finally {
            session.close();
        }

        return authorise;
    }

    @Override
    public boolean handleConfirmation(String authToken, String email) {
        boolean confirmed = false;

        Session session = sessionFactory.openSession();

        UserEntity user = null;
        String retToken = "";

        try{
            Query query = session.createQuery("from UserEntity as u where u.email = :email and u.authorisationToken = :authToken");

            query.setString("email", email);
            query.setString("authToken", authToken);
            List <UserEntity> list = query.list();
            if(list.size() > 0){
                user = list.get(0);
                retToken = user.getAuthorisationToken();
                setUSER_ID(user.getId());

                System.out.println("token in DB " +retToken);
                System.out.println("token send to method " +authToken);
            }
            if(authToken.equals(retToken)){
                session.beginTransaction();
                user.setConfirmed((byte)1);
                session.save(user);
                session.getTransaction().commit();
                confirmed = true;
            }
            else if(!authToken.equals(retToken)){
                confirmed = false;
            }

        }catch(Exception ee){
            System.out.println("Error handling confirmation");
            ee.printStackTrace();
        }finally {
            session.close();
        }
        return confirmed;
    }

    //TODO: Add pictures as parameters
    @Override
    public boolean createProfile(int age, String bio, String gender, String soughtGender, String programme){
        Session session = sessionFactory.openSession();
        boolean success = false;
        Transaction tx = null;

        try{
            tx = session.beginTransaction();
            UserProfileEntity profile = new UserProfileEntity();
            profile.setUserId(getUSER_ID());
            profile.setAge(age);
            profile.setBio(bio);
            profile.setGender(gender);
            profile.setSoughtGender(soughtGender);
            profile.setImage1("i1");
            profile.setImage2("i2");
            profile.setProgramme(programme);
            session.save(profile);
            tx.commit();
            success = true;
        }catch (HibernateException ee){
            System.out.println(ee.getMessage());
        }finally {
            session.close();
        }

        return success;
    }

    @Override
    public UserProfileEntity getProfile(){
        Session session = sessionFactory.openSession();
        UserProfileEntity profile = null;

        try{
            Query query = session.createQuery("from UserProfileEntity as p where p.userId = :id");
            query.setInteger("id", getUSER_ID());
            List<UserProfileEntity> list = query.list();
            if(list.size() > 0){
                profile = list.get(0);
            }
            else{
                System.out.println("error in retrieving profile");
            }
        }catch (Exception ee){
            System.out.println(ee.getMessage());
        }finally {
            session.close();
        }

        return profile;
    }

    public void ejbCreate(int userID) throws CreateException {
        this.USER_ID = userID;
    }

    public void setSessionContext(SessionContext context){
        this.sessionContext = context;
    }

    public SessionContext getSessionContext(){
        return this.sessionContext;
    }

    public String addImage(String blobKeyString, int imageNumber){

        Session session = sessionFactory.openSession();
        Transaction tx;

        try{
            tx = session.beginTransaction();
            UserProfileEntity user = (UserProfileEntity)session.get(UserProfileEntity.class, getUSER_ID());
            switch (imageNumber){
                case 1:
                    user.setImage1(blobKeyString);
                    break;
                case 2:
                    user.setImage2(blobKeyString);
                    break;
                case 3:
                    user.setImage3(blobKeyString);
                    break;
                case 4:
                    user.setImage4(blobKeyString);
                    break;
                case 5:
                    user.setImage5(blobKeyString);
                    break;
                default:
                    break;
            }

            session.update(user);
            tx.commit();
        }catch (HibernateException ee){
            System.out.println("Error in hibernate syntax");
            ee.printStackTrace();
        }finally {
            session.close();
        }

        return blobKeyString;
    }

    public String getImage(){
        return getProfile().getImage1();
    }

    public List<UserProfileEntity> listAllUsers(){
        List<UserProfileEntity> users;

        Session session = sessionFactory.openSession();
        Query query = session.createQuery("from UserProfileEntity");
        users = query.list();


        return users;
    }


}
