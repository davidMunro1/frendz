package beans;

import Helper.HashHelper;
//import com.google.api.server.spi.auth.common.User;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.images.Image;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.Transform;
import hibernate.*;
import org.hibernate.*;
import org.hibernate.criterion.*;

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

    private int browseIndex = 0;
    private int matchedResults;

    private Byte TRUE = 1;
    private Byte FALSE = 0;

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
        String hashedPass = HashHelper.createHash(password);

        Session session = sessionFactory.openSession();

        try{
            Query q2 = session.createQuery("from UserEntity as u where u.email = :email and u.password = :password");
            q2.setString("email", email);
            q2.setString("password", hashedPass);
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
    public boolean handleConfirmation(String authToken, String email, String password) {
        boolean confirmed = false;

        String hashPass = HashHelper.createHash(password);
        if(sessionFactory==null){
            sessionFactory = FrendzHibernateUtil.getSessionFactory();
        }
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

            }
            if(authToken.equals(retToken)){
                session.beginTransaction();
                user.setConfirmed((byte)1);
                user.setPassword(hashPass);
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

    @Override
    public boolean createProfile(int age, String gender, String soughtGender, String programme, String bio){
        Session session = sessionFactory.openSession();
        boolean success = false;
        Transaction tx;

        try{
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
        }catch (HibernateException ee){
            System.out.println(ee.getMessage());
        }
        //finally {
        //    session.close();
        //}

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

    @Override
    public UserProfileEntity getUserProfile(int userID){
        Session session = sessionFactory.openSession();
        UserProfileEntity user = (UserProfileEntity)session.get(UserProfileEntity.class, userID);
        return user;
    }

    public void ejbCreate(int userID) throws CreateException {
        this.USER_ID = userID;
    }

    /**
     * Add the image key string to the database
     * @param blobKeyString The key string of the image uploaded by user
     * @param imageNumber The image number to set it to.
     * @return
     */
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

    /**
     * Retrieve the image key string from the database for the user
     * that is currently logged in.
     * @return
     */
    public String getImage(){
        return getProfile().getImage1();
    }

    /**
     * Retrieve the first image of the specified user, for use in
     * browse user function.
     * @param userID
     * @return
     */
    public String getImage(int userID){
        Session session = sessionFactory.openSession();
        UserProfileEntity user = (UserProfileEntity)session.get(UserEntity.class, userID);
        return user.getImage1();
    }

    /**
     * To be used for the browse function, will return a list of all users that
     * match the required criteria.
     * @return a list of all users that match criteria.
     */
    public List<NextUser> browseAllUsers(){
        if(sessionFactory==null){
            sessionFactory = FrendzHibernateUtil.getSessionFactory();
        }
        Session session = sessionFactory.openSession();
        UserEntity user = (UserEntity)session.get(UserEntity.class, getUSER_ID());
        UserProfileEntity usersProfile = (UserProfileEntity)session.get(UserProfileEntity.class, getUSER_ID());
        List<Integer> ids = new ArrayList<>();

        System.out.println("The user is looking for..." +usersProfile.getSoughtGender());

        Criteria criteria = session.createCriteria(UserEntity.class);
        //TODO: add below line when testing completed.
        //criteria.add(Restrictions.eq("confirmed", (byte)1));
        criteria.add(Restrictions.eq("school", user.getSchool()));
        criteria.add(Restrictions.ne("id", user.getId()));
        List<UserEntity> list = criteria.list();

        for(int i = 0; i < list.size(); i++){
            ids.add(list.get(i).getId());
        }

        Criteria c = session.createCriteria(UserProfileEntity.class);
        c.add(Restrictions.in("id", ids));
        c.add(Restrictions.eq("gender", usersProfile.getSoughtGender()));
        c.add(Restrictions.eq("soughtGender", usersProfile.getGender()));
        setMatchedResults(c.list().size());

        c.setMaxResults(10);
        c.setFirstResult(getBrowseIndex());
        List<UserProfileEntity> matchedUsers = c.list();

        setBrowseIndex(browseIndex+matchedUsers.size());
        List<NextUser> nextUsers = new ArrayList<>();

        for(int i = 0; i < matchedUsers.size(); i++){
            NextUser nextUser = new NextUser();
            UserEntity userr = (UserEntity)session.get(UserEntity.class, matchedUsers.get(i).getUserId());
            nextUser.setFirstName(userr.getFirstName());
            nextUser.setPictureString(matchedUsers.get(i).getImage1());
            nextUser.setProgramme(matchedUsers.get(i).getProgramme());
            nextUser.setAge(matchedUsers.get(i).getAge());
            nextUser.setUserID(matchedUsers.get(i).getUserId());
            nextUsers.add(nextUser);
        }

        return nextUsers;
    }

    /**
     * Handle like button clicked
     * @param likedUserID the ID of the profile that the user likes
     */
    public void handleLike(int likedUserID, byte like){
        if(sessionFactory==null){
            sessionFactory = FrendzHibernateUtil.getSessionFactory();
        }
        Session session = sessionFactory.openSession();
        Transaction tx = null;

        try{
            tx = session.beginTransaction();
            RelationshipsEntity relationshipsEntity = new RelationshipsEntity();
            relationshipsEntity.setUser1(getUSER_ID());
            relationshipsEntity.setUser2(likedUserID);
            relationshipsEntity.setVisit(TRUE);
            relationshipsEntity.setLike(like);
            relationshipsEntity.setBlock(FALSE);
            session.save(relationshipsEntity);
            tx.commit();

            Criteria criteria = session.createCriteria(RelationshipsEntity.class);


        }catch(HibernateException ee){
            try{
                tx.rollback();
            }catch(RuntimeException rbe){
                System.out.println(rbe.getMessage());
            }
        }finally {
            session.close();
        }
    }

    /**
     * Handle dislike button clicked
     * @param dislikedUserID the id of the profile that the user dislikes
     */
    public void handleDislike(int dislikedUserID){

    }

    public Image getImageObject(BlobKey blobKey){
        ImagesService imagesService = ImagesServiceFactory.getImagesService();

        Image oldImage = ImagesServiceFactory.makeImageFromBlob(blobKey);
        Transform resize = ImagesServiceFactory.makeResize(200, 300);

        Image newImage = imagesService.applyTransform(resize, oldImage);

        byte[] newImageData = newImage.getImageData();
        return newImage;
    }

    public void setBrowseIndex(int browseIndex){
        this.browseIndex = browseIndex;
    }

    public int getBrowseIndex(){
        return browseIndex;
    }

    /**
     * Sets the total number of matched results from the browse query.
     * @param matchedResults the initial number of results from the query
     */
    public void setMatchedResults(int matchedResults){
        this.matchedResults = matchedResults;
    }

    /**
     * Returns the total number of matched results in the browse query.
     * @return
     */
    public int getMatchedResults(){
        return matchedResults;
    }
}
