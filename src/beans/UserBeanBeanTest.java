package beans;

import junit.framework.TestCase;

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
        assertEquals(false, bean.handleSignUp("d","d","mexi@mpoi.com","",(byte)0,"njnkjndjcw"));
    }

    /**
     * Test that login method works correctly
     * @throws Exception
     */
    public void testHandleLogin() throws Exception {
        UserBeanBean bean = new UserBeanBean();
        assertTrue(bean.handleLogin("david.munro@hkr.stud.se", "password"));
    }
}