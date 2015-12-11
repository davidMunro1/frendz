package beans;

import javax.ejb.Stateful;

/**
 * Created by davidmunro on 11/12/2015.
 */
@Stateful(name = "UserSession")
public class UserBeanBean implements UserSessionLocal{
    private int USER_ID;

    public UserBeanBean() {
    }

    @Override
    public void addUser() {

    }
}
