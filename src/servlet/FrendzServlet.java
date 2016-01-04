package servlet;

import Helper.HashHelper;
import beans.UserBeanBean;
import com.google.appengine.api.blobstore.*;
import com.google.appengine.repackaged.com.google.common.base.Flag;
import utils.MailSender;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * Created by davidmunro on 08/12/2015.
 */

@WebServlet(name = "FrendzServlet")
public class FrendzServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private int USER_ID = 10;
    private UserBeanBean bean = new UserBeanBean();

    private String ERROR_SIGN_UP = "Sign up failed";

    private BlobstoreService blobStoreService = BlobstoreServiceFactory.getBlobstoreService();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        if(request.getParameter("button").equalsIgnoreCase("login")) {
            System.out.println("login");
            handleLogin(request,response);
        } else if(request.getParameter("button").equalsIgnoreCase("Sign up")){
            System.out.println("signup");
            handleSignUp(request, response);
        } else if(request.getParameter("button").equalsIgnoreCase("Verify")){
            System.out.println("Verify account");
            //handleConfirmation(request, response);
        }else if(request.getParameter("button").equalsIgnoreCase("Create profile")){
            System.out.println("create profile");
            handleCreateProfile(request, response);
        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        bean.setUSER_ID(18);

        String type = request.getParameter("button");
        System.out.println("Value== "+type);
        byte confirmed =0;

        if(type.equals("Login")){

        }
        else if(type.equals("User")){
            //bla bla
            response.sendRedirect("file.jsp");
        }
    }

    private void handleLogin(HttpServletRequest request, HttpServletResponse response){
        boolean correctUser = bean.handleLogin(request.getParameter("email"), request.getParameter("password"));
        System.out.println("user exists : " +correctUser);
        if(correctUser==true){
            //go to homepage
            try {
                response.sendRedirect("homepage.jsp");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(correctUser==false){
            System.out.println("user does not exist or account not confirmed!");
            //TODO: Need to send error message back to user, user doesnt exist, not confirmed or wrong combination.

        }
    }

    private void handleSignUp(HttpServletRequest request, HttpServletResponse response) {
        byte confirmed =0;
        String toHash = request.getParameter("email").concat(request.getParameter("university"));
        String authToken = HashHelper.createHash(toHash);
        String email = request.getParameter("email");

        boolean signUp = bean.handleSignUp(request.getParameter("firstName"), request.getParameter("lastName"),
               email, request.getParameter("university"), confirmed, authToken);

        if(signUp==true){
            System.out.println("send mail with activation code, user signed up");
            MailSender mailSender = new MailSender();
            mailSender.sendMessage(email, authToken);
            try {
                response.sendRedirect("index.html");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(signUp==false){
            System.out.println("sign up failed");
        }
    }

    private void handleConfirmation(HttpServletRequest request, HttpServletResponse response){
        String authToken = request.getParameter("token");
        String email = request.getParameter("email");
        boolean confirm = bean.handleConfirmation(authToken, email);
        if(confirm==true){
            System.out.println("confirmed, go to create profile page");
        }
        else{
            System.out.println("confirmation failed, where to go here?");
        }
    }

    private void handleCreateProfile(HttpServletRequest request, HttpServletResponse response){
        int age = Integer.parseInt(request.getParameter("age"));
        String gender = request.getParameter("gender");
        String soughtGender = request.getParameter("soughtGender");
        String programme = request.getParameter("programme");
        String bio = request.getParameter("bio");
        boolean profileCreated = bean.createProfile(age, bio, gender, soughtGender, programme);

        if(profileCreated) {
            try {
                response.sendRedirect("homepage.jsp");
            } catch(IOException ex) {
                ex.printStackTrace();
            }
        }
    }

}
