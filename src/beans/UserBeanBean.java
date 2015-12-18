package beans;

import Helper.HashHelper;
import hibernate.FrendzHibernateUtil;
import hibernate.UserEntity;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.ejb.*;
import javax.ejb.CreateException;
import java.io.Serializable;
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

    private SessionFactory sessionFactory;
    private Session session;

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
    public boolean handleSignUp(String firstName, String lastName, String password, String email, String school, byte confirmed, String authToken) {
        boolean success = false;
        String hashedPass = HashHelper.createHash(password);
        SessionFactory sessionFac = FrendzHibernateUtil.getSessionFactory();
        Session session = sessionFac.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            UserEntity user = new UserEntity();
            user.setFirstName(firstName);
            user.setSecondName(lastName);
            user.setEmail(email);
            user.setPassword(hashedPass);
            user.setAuthorisationToken(authToken);
            user.setSchool(school);
            user.setConfirmed(confirmed);
            session.save(user);
            setUSER_ID(user.getId());
            tx.commit();
            success = true;
        }catch (Exception ee){
            System.out.println("Error adding to DB : " +ee.getMessage());
        }finally {
            session.flush();
            session.close();
        }
        return success;
    }

    @Override
    public boolean handleLogin(String email, String password) {
        boolean authorise = false;
        String hashedPass = HashHelper.createHash(password);
        sessionFactory = FrendzHibernateUtil.getSessionFactory();
        session = sessionFactory.openSession();
        try{
            Query q2 = session.createQuery("from UserEntity as u where u.email = :email and u.password = :password");
            q2.setString("email", email);
            //q2.setString("password", hashedPass);
            q2.setString("password", password);
            List list = q2.list();
            if(list.size() == 1){
                UserEntity user = (UserEntity)list.get(0);
                setUSER_ID(user.getId());
                authorise = true;
            }
            else if(list.size() == 0){
                System.out.println("Wrong email or password");
                authorise = false;
            }

        }catch(RuntimeException ee){
            System.out.println("Error in log in");
            ee.printStackTrace();
        }finally {
            session.flush();
            session.close();
        }

        return authorise;
    }

    @Override
    public boolean handleConfirmation(String authToken) {
        boolean confirmed = false;
        sessionFactory = FrendzHibernateUtil.getSessionFactory();
        session = sessionFactory.openSession();
        UserEntity user = null;

        try{
            Query query = session.createQuery("from UserEntity as u where u.id = :id and u.authorisationToken = :authToken");

            //this can be used after the id is set properly for the first time
            //query.setInteger("id", getUSER_ID());
            query.setInteger("id", 1);
            query.setString("authToken", authToken);
            List <UserEntity> list = query.list();
            user = list.get(0);
            String retToken = user.getAuthorisationToken();
            System.out.println("token in DB " +retToken);
            System.out.println("token send to method " +authToken);

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
            session.flush();
            session.close();
        }
        return confirmed;
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

    //TODO: Add in methods for creating profile, add Profile table to FrendzDB.

}
