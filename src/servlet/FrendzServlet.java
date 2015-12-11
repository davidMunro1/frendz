package servlet;

import beans.UserBeanBean;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by davidmunro on 08/12/2015.
 */
@WebServlet(name = "FrendzServlet")
public class FrendzServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String type = request.getParameter("button");
        System.out.println("Value = "+type);
        UserBeanBean bean = new UserBeanBean();

    }
}
