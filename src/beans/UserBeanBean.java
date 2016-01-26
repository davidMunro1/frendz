package beans;

import Helper.HashHelper;
//import com.google.api.server.spi.auth.common.User;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.images.*;
import hibernate.*;
import org.hibernate.*;
import org.hibernate.criterion.*;

import javax.ejb.*;
import javax.ejb.CreateException;
import java.io.Serializable;
import java.net.NetPermission;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.SessionContext;
import javax.jws.soap.SOAPBinding;

/**
 * Created by davidmunro on 11/12/2015.
 */
@Stateful(name = "UserSession")
@Local
public class UserBeanBean implements LocalUser, Serializable {

    private int USER_ID;
    private SessionContext sessionContext;
    private static final long serialVersionUID = 1L;

    public SessionFactory sessionFactory = FrendzHibernateUtil.getSessionFactory();

    private int browseIndex = 0;
    private int matchedResults;

    private Byte TRUE = 1;
    private Byte FALSE = 0;

    public UserBeanBean() {}

    @Override
    public void initializeSessionBean(int userID) {
        this.USER_ID = userID;
    }

    @Override
    public int getUSER_ID() {
        return USER_ID;
    }

    @Override
    public void setUSER_ID(int userID) {
        this.USER_ID = userID;
    }

    @Override
    public boolean handleSignUp(String firstName, String lastName, String email, String school, String authToken) {
        boolean success = false;
        if(sessionFactory == null){
            sessionFactory = FrendzHibernateUtil.getSessionFactory();
        }
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        UserEntity user = new UserEntity();
        try {
            tx = session.beginTransaction();
            user.setFirstName(firstName);
            user.setSecondName(lastName);
            user.setEmail(email);
            user.setAuthorisationToken(authToken);
            user.setSchool(school);
            user.setConfirmed(FALSE);
            session.save(user);
            setUSER_ID(user.getId());
            tx.commit();
            success = true;
        } catch (Exception ee) {
            if (tx != null) tx.rollback();
            System.out.println("Error in SIGN UP : " + ee.getMessage());
        } finally {
            session.close();
        }
        return success;
    }

    @Override
    public boolean handleLogin(String email, String password) {
        if (sessionFactory == null) {
            sessionFactory = FrendzHibernateUtil.getSessionFactory();
        }
        Session session = sessionFactory.openSession();
        boolean authorise = false;
        String hashedPass = HashHelper.createHash(password);

        try {
            Query q2 = session.createQuery("from UserEntity as u where u.email = :email and u.password = :password");
            q2.setString("email", email);
            q2.setString("password", hashedPass);
            List list = q2.list();
            if (list.size() == 1) {
                UserEntity user = (UserEntity) list.get(0);
                setUSER_ID(user.getId());

                if (user.getConfirmed() == 1) {
                    authorise = true;
                } else if (user.getConfirmed() == 0) {
                    authorise = false;
                }
            } else if (list.size() == 0) {
                System.out.println("BEAN - Wrong email or password");
                authorise = false;
            }

        } catch (RuntimeException ee) {
            System.out.println("Error in log in");
            ee.printStackTrace();
        } finally {
            session.close();
        }

        return authorise;
    }

    @Override
    public boolean handleConfirmation(String authToken, String email) {
        boolean confirmed = false;

        if (sessionFactory == null) {
            sessionFactory = FrendzHibernateUtil.getSessionFactory();
        }
        Session session = sessionFactory.openSession();

        UserEntity user = null;
        String retToken = "";

        try {
            Query query = session.createQuery("from UserEntity as u where u.email = :email and u.authorisationToken = :authToken");
            query.setString("email", email);
            query.setString("authToken", authToken);
            List<UserEntity> list = query.list();
            if (list.size() > 0) {
                user = list.get(0);
                retToken = user.getAuthorisationToken();
                setUSER_ID(user.getId());
            }
            if (authToken != null) {
                if (authToken.equals(retToken) && user != null) {
                    if(user.getConfirmed()==TRUE){
                        //user has already confirmed account
                        confirmed = false;
                    }
                    else{
                        session.beginTransaction();
                        user.setConfirmed(TRUE);
                        session.save(user);
                        session.getTransaction().commit();
                        confirmed = true;
                    }
                } else if (!authToken.equals(retToken)) {
                    confirmed = false;
                }
            } else {
                confirmed = false;
            }


        } catch (Exception ee) {
            System.out.println("Error handling confirmation");
            ee.printStackTrace();
        } finally {
            session.close();
        }
        return confirmed;
    }

    /**
     * Set the users password
     *
     * @param password The password to be set
     * @return boolean if setting password was successful
     */
    @Override
    public boolean setPassword(String password) {
        if (sessionFactory == null) {
            sessionFactory = FrendzHibernateUtil.getSessionFactory();
        }
        boolean added = false;
        String hashPass = HashHelper.createHash(password);
        Session session = sessionFactory.openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            UserEntity user = (UserEntity) session.get(UserEntity.class, getUSER_ID());
            user.setPassword(hashPass);
            session.save(user);
            tx.commit();
            added = true;
        } catch (HibernateException ee) {
            if (tx != null) tx.rollback();
            System.out.println("error setting password");
        } finally {
            session.close();
        }

        return added;
    }

    /**
     * Creates a new profile entity
     * @param age the age of the new user
     * @param gender the gender of the new user
     * @param soughtGender what gender is the user looking for
     * @param programme the programme that the user attends
     * @param bio a short few lines about the new user
     * @return whether the creation was successful
     */
    @Override
    public boolean createProfile(int age, String gender, String soughtGender, String programme, String bio) {
        Session session = sessionFactory.openSession();
        boolean success = false;
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            UserProfileEntity profile = new UserProfileEntity();
            profile.setUserId(getUSER_ID());
            profile.setAge(age);
            profile.setBio(bio);
            profile.setGender(gender);
            profile.setSoughtGender(soughtGender);
            profile.setProgramme(programme);
            session.save(profile);
            tx.commit();
            success = true;
        } catch (HibernateException ee) {
            if (tx != null) tx.rollback();
            System.out.println(ee.getMessage());
        } finally {
            session.close();
        }

        return success;
    }

    @Override
    public boolean handleEditProfile(String secondName, String password, String programme, String bio){
        boolean success = false;
        if(sessionFactory == null){
            sessionFactory = FrendzHibernateUtil.getSessionFactory();
        }
        Session session = sessionFactory.openSession();
        Transaction tx = null;


        try{
            tx = session.beginTransaction();
            UserEntity user = (UserEntity)session.get(UserEntity.class, getUSER_ID());
            UserProfileEntity userProfile = (UserProfileEntity)session.get(UserProfileEntity.class, getUSER_ID());
            if(secondName != null && !secondName.isEmpty()){
                user.setSecondName(secondName);
            }
            if(password != null && !password.isEmpty()){
                String hashPass = HashHelper.createHash(password);
                user.setPassword(hashPass);
            }
            if(programme != null && !programme.isEmpty()){
                userProfile.setProgramme(programme);
            }
            if(bio != null && !bio.isEmpty()){
                userProfile.setBio(bio);
            }
            session.update(user);
            session.update(userProfile);
            tx.commit();
            success = true;

        } catch (HibernateException ee){
            if (tx != null) tx.rollback();
            System.out.println("Error in edit profile : " +ee.getMessage());
        }finally {
            session.close();
        }

        return success;
    }

    /**
     * Get the profile of the user that is currectly logged on
     * @return the entity class of the user
     */
    @Override
    public UserProfileEntity getProfile() {
        if(sessionFactory==null){
            sessionFactory = FrendzHibernateUtil.getSessionFactory();
        }
        Session session = sessionFactory.openSession();
        UserProfileEntity profile = null;

        try {
            Query query = session.createQuery("from UserProfileEntity as p where p.userId = :id");
            query.setInteger("id", getUSER_ID());
            List<UserProfileEntity> list = query.list();
            if (list.size() > 0) {
                profile = list.get(0);
            } else {
                System.out.println("error in retrieving profile");
            }
        } catch (Exception ee) {
            System.out.println(ee.getMessage());
        } finally {
            session.close();
        }

        return profile;
    }

    @Override
    public UserEntity getUser(){
        if(sessionFactory==null){
            sessionFactory = FrendzHibernateUtil.getSessionFactory();
        }
        UserEntity user = null;
        try{
            Session session = sessionFactory.openSession();
            user = (UserEntity)session.get(UserEntity.class, getUSER_ID());
        }catch (HibernateException ee){
            System.out.println("Error getting user : " +ee.getMessage());
        }

        return user;
    }

    /**
     * Returns a UserProfileEntity with the specified id
     * @param userID the ID used to specify user
     * @return the userprofile object
     */
    @Override
    public UserProfileEntity getUserProfile(int userID) {
        if(sessionFactory==null){
            sessionFactory = FrendzHibernateUtil.getSessionFactory();
        }
        UserProfileEntity user = null;
        try{
            Session session = sessionFactory.openSession();
            user = (UserProfileEntity) session.get(UserProfileEntity.class, userID);
        }catch (HibernateException ee){
            System.out.println("error retrieving user " +ee.getMessage());
        }

        return user;
    }

    /**
     * Add the image key string to the database
     *
     * @param blobKeyString The key string of the image uploaded by user
     * @param imageNumber   The image number to set it to.
     * @return
     */
    @Override
    public boolean addImage(String blobKeyString, int imageNumber) {
        boolean success = false;
        if(sessionFactory==null){
            sessionFactory = FrendzHibernateUtil.getSessionFactory();
        }

        Session session = sessionFactory.openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            UserProfileEntity user = (UserProfileEntity) session.get(UserProfileEntity.class, getUSER_ID());
            switch (imageNumber) {
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
            success = true;
        } catch (HibernateException ee) {
            if (tx != null) tx.rollback();
            ee.printStackTrace();
        } finally {
            session.close();
        }

        return success;
    }

    /**
     * Retrieve the image key string from the database for the user
     * that is currently logged in.
     *
     * @return
     */
    @Override
    public String getImage() {
        return getProfile().getImage1();
    }

    /**
     * Retrieve the first image of the specified user, for use in
     * browse user function.
     *
     * @param userID
     * @return
     */
    @Override
    public String getImage(int userID) {
        Session session = sessionFactory.openSession();
        UserProfileEntity user = (UserProfileEntity) session.get(UserEntity.class, userID);
        return user.getImage1();
    }

    /**
     * To be used for the browse function, will return a list of all users that
     * match the required criteria.
     *
     * @return a list of 10 users that match criteria.
     */
    @Override
    public List<NextUser> browseAllUsers()  {
        if (sessionFactory == null) {
            sessionFactory = FrendzHibernateUtil.getSessionFactory();
        }
        Session session = sessionFactory.openSession();
        UserEntity user = (UserEntity) session.get(UserEntity.class, getUSER_ID());
        UserProfileEntity usersProfile = (UserProfileEntity) session.get(UserProfileEntity.class, getUSER_ID());
        List<Integer> ids = new ArrayList<>();

        Criteria criteria = session.createCriteria(UserEntity.class);
        //TODO: add below line when testing completed.
        //criteria.add(Restrictions.eq("confirmed", (byte)1));
        criteria.add(Restrictions.eq("school", user.getSchool()));
        criteria.add(Restrictions.ne("id", user.getId()));
        List<UserEntity> list = criteria.list();

        for (int i = 0; i < list.size(); i++) {
            ids.add(list.get(i).getId());
        }

        Criteria c = session.createCriteria(UserProfileEntity.class);
        c.add(Restrictions.in("id", ids));
        c.add(Restrictions.eq("gender", usersProfile.getSoughtGender()));

        if(!usersProfile.getSoughtGender().equals("BOTH")){
            c.add(Restrictions.eq("soughtGender", usersProfile.getGender()));
        }

        setMatchedResults(c.list().size());

        c.setMaxResults(10);
        c.setFirstResult(getBrowseIndex());
        List<UserProfileEntity> matchedUsers = c.list();

        //Comment out max results for above criteria
//        ids.clear();
//        for(int i = 0; i < matchedUsers.size(); i++){
//            ids.add(matchedUsers.get(i).getUserId());
//        }
//        Criteria criteria1 = session.createCriteria(RelationshipsEntity.class);
//        criteria1.add(Restrictions.in("id", ids));
//        criteria1.add(Restrictions.ne("visited", TRUE));
//        List<RelationshipsEntity> relationship = criteria1.list();
//        ids.clear();
//        for(int i = 0; i < matchedUsers.size(); i++) {
//            ids.add(relationship.get(i).getUser2());
//        }

        setBrowseIndex(browseIndex + matchedUsers.size());
        List<NextUser> nextUsers = new ArrayList<>();

        for (int i = 0; i < matchedUsers.size(); i++) {
            NextUser nextUser = new NextUser();
            UserEntity userr = (UserEntity) session.get(UserEntity.class, matchedUsers.get(i).getUserId());
            nextUser.setFirstName(userr.getFirstName());
            nextUser.setPictureString(matchedUsers.get(i).getImage1());
            nextUser.setProgramme(matchedUsers.get(i).getProgramme());
            nextUser.setAge(matchedUsers.get(i).getAge());
            nextUser.setUserID(matchedUsers.get(i).getUserId());
            nextUser.setBio(matchedUsers.get(i).getBio());
            nextUsers.add(nextUser);
        }
        return nextUsers;
    }

    /**
     * Returns a list of 4 random users to be used in user homepage
     * @return List of the random users
     */
    @Override
    public List<NextUser> getRandomUsers(){
        if(sessionFactory==null){
            sessionFactory = FrendzHibernateUtil.getSessionFactory();
        }
        Session session = sessionFactory.openSession();

        UserEntity user = (UserEntity) session.get(UserEntity.class, getUSER_ID());
        List<Integer> ids = new ArrayList<>();

        Criteria criteria = session.createCriteria(UserEntity.class);
        //TODO: add below line when testing completed.
        //criteria.add(Restrictions.eq("confirmed", (byte)1));
        criteria.add(Restrictions.eq("school", user.getSchool()));
        criteria.add(Restrictions.ne("id", user.getId()));
        List<UserEntity> list = criteria.list();

        for (int i = 0; i < list.size(); i++) {
            ids.add(list.get(i).getId());
        }

        Criteria criteria1 = session.createCriteria(UserProfileEntity.class);
        criteria1.add(Restrictions.in("id", ids));
        criteria1.add(Restrictions.sqlRestriction("1=1 order by rand()"));
        criteria1.setMaxResults(4);
        List<UserProfileEntity> matchedUsers = criteria1.list();

        List<NextUser> nextUsers = new ArrayList<>();

        for (int i = 0; i < matchedUsers.size(); i++) {
            NextUser nextUser = new NextUser();
            UserEntity userr = (UserEntity) session.get(UserEntity.class, matchedUsers.get(i).getUserId());
            nextUser.setFirstName(userr.getFirstName());
            nextUser.setPictureString(matchedUsers.get(i).getImage1());
            nextUsers.add(nextUser);
        }
        return nextUsers;

    }

    /**
     * Handle like button clicked
     *
     * @param likedUserID the ID of the profile that the user likes
     */
    @Override
    public void handleLike(int likedUserID) {
        if (sessionFactory == null) {
            sessionFactory = FrendzHibernateUtil.getSessionFactory();
        }
        Session session = sessionFactory.openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            RelationshipsEntity relationshipsEntity = new RelationshipsEntity();
            relationshipsEntity.setUser1(getUSER_ID());
            relationshipsEntity.setUser2(likedUserID);
            relationshipsEntity.setVisit(TRUE);
            relationshipsEntity.setLike(TRUE);
            relationshipsEntity.setBlock(FALSE);
            session.save(relationshipsEntity);
            tx.commit();

            Criteria criteria = session.createCriteria(RelationshipsEntity.class);
            criteria.add(Restrictions.eq("user1", likedUserID));
            criteria.add(Restrictions.eq("like", TRUE));
            if (criteria.list().size() > 0) {
                //TODO: There was a match, initialise messaging.
            }

        } catch (HibernateException ee) {
            try {
                tx.rollback();
            } catch (RuntimeException rbe) {
                System.out.println(rbe.getMessage());
            }
        } finally {
            session.close();
        }
    }

    /**
     * Handle dislike button clicked
     *
     * @param dislikedUserID the id of the profile that the user dislikes
     */
    @Override
    public void handleDislike(int dislikedUserID) {
        if (sessionFactory == null) {
            sessionFactory = FrendzHibernateUtil.getSessionFactory();
        }
        Session session = sessionFactory.openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            RelationshipsEntity relationshipsEntity = new RelationshipsEntity();
            relationshipsEntity.setUser1(getUSER_ID());
            relationshipsEntity.setUser2(dislikedUserID);
            relationshipsEntity.setVisit(TRUE);
            relationshipsEntity.setLike(FALSE);
            relationshipsEntity.setBlock(FALSE);
            session.save(relationshipsEntity);
            tx.commit();

        } catch (HibernateException ee) {
            try {
                tx.rollback();
            } catch (RuntimeException rbe) {
                System.out.println(rbe.getMessage());
            }
        } finally {
            session.close();
        }
    }

    public Image getImageObject(BlobKey blobKey) {
        ImagesService imagesService = ImagesServiceFactory.getImagesService();

        Image oldImage = ImagesServiceFactory.makeImageFromBlob(blobKey);
        Transform resize = ImagesServiceFactory.makeResize(200, 300);

        Image newImage = imagesService.applyTransform(resize, oldImage);

        byte[] newImageData = newImage.getImageData();
        return newImage;
    }

    public void setBrowseIndex(int browseIndex) {
        this.browseIndex = browseIndex;
    }

    public int getBrowseIndex() {
        return browseIndex;
    }

    /**
     * Sets the total number of matched results from the browse query.
     *
     * @param matchedResults the initial number of results from the query
     */
    public void setMatchedResults(int matchedResults) {
        this.matchedResults = matchedResults;
    }

    /**
     * Returns the total number of matched results in the browse query.
     *
     * @return
     */
    public int getMatchedResults() {
        return matchedResults;
    }

    /**
     * Generates the URL to be used to display the specific Blob
     * @param blobKeyString The BlobKey string that is stored in database
     * @return The url that has been created
     */
    public String getServingURL(String blobKeyString){
        BlobKey blobKey = new BlobKey(blobKeyString);
        ImagesService imagesService = ImagesServiceFactory.getImagesService();
        ServingUrlOptions servingUrlOptions = ServingUrlOptions.Builder.withBlobKey(blobKey);
        String url = imagesService.getServingUrl(servingUrlOptions);

        return url;
    }

}
