package beans;

import hibernate.NextUser;
import junit.framework.TestCase;

import java.util.List;

/**
 * Created by davidmunro on 07/01/2016.
 */
public class UserBeanBeanTest extends TestCase {

    /**
     * Test that user must have unique email address
     * @throws Exception
     */
    public void testHandleSignUp() throws Exception {
        UserBeanBean bean = new UserBeanBean();
        //assertEquals(false, bean.handleSignUp("d","d","mexi@mpoi.com","",(byte)0,"njnkjndjcw"));
    }

    /**
     * Test that login method works correctly
     * @throws Exception
     */
    public void testHandleLogin() throws Exception {
        //UserBeanBean bean = new UserBeanBean();
        //assertTrue(bean.handleLogin("david.munro@hkr.stud.se", "password"));
    }

    public void testEditProfile() throws Exception {
        UserBeanBean bean = new UserBeanBean();
        //bean.setUSER_ID(36);
        //assertEquals(36, bean.getUSER_ID());
        //assertEquals(true, bean.handleEditProfile("Munro", "password", "Computer", "I like changing my profile"));
    }

    public void testGetRandomUsers() throws Exception {
        UserBeanBean bean = new UserBeanBean();
        bean.setUSER_ID(26);
        //assertEquals(26, bean.getUSER_ID());
        //List<NextUser> nextUsers = bean.getRandomUsers();
        //assertEquals(4, nextUsers.size());
        //for(int i = 0; i < nextUsers.size(); i++){
//            System.out.println(nextUsers.get(i).getFirstName());
//        }
    }

    public void testGetUser() throws Exception {
        UserBeanBean bean =  new UserBeanBean();
        bean.setUSER_ID(29);
        assertEquals("Paul", bean.getUser().getFirstName());

    }
}