package servlet;

import Helper.HashHelper;
import beans.UserBeanBean;
import com.google.appengine.api.blobstore.*;
import com.google.appengine.api.images.Image;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.ServingUrlOptions;
import com.google.appengine.api.users.User;
import hibernate.NextUser;
import hibernate.UserEntity;
import hibernate.UserProfileEntity;
import utils.MailSender;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;
import java.util.Map;

/**
 * Created by davidmunro on 08/12/2015.
 */

@WebServlet(name = "FrendzServlet")
public class FrendzServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private UserBeanBean bean; //= new UserBeanBean();

    private Byte TRUE = 1;
    private Byte FALSE = 0;

    private BlobstoreService blobStoreService = BlobstoreServiceFactory.getBlobstoreService();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        bean = (UserBeanBean)request.getSession().getAttribute("bean");

        if(request.getParameter("button").equalsIgnoreCase("login")) {
            System.out.println("login");

            response.sendRedirect("browse.jsp");
            //response.sendRedirect("homepage.jsp");
            //response.sendRedirect("profileCreation.jsp");
            //handleLogin(request,response);
        } else if(request.getParameter("button").equalsIgnoreCase("Sign up")){
            System.out.println("signup");
            handleSignUp(request, response);
        } else if(request.getParameter("button").equalsIgnoreCase("Confirm")){
            System.out.println("confirm account");
            handleConfirmation(request, response);
        } else if(request.getParameter("button").equalsIgnoreCase("Create profile")){
            System.out.println("create profile");
            handleCreateProfile(request, response);
        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        if(request.getParameter("action").equalsIgnoreCase("like")) {
            bean.handleLike(Integer.parseInt(request.getParameter("userId")));
            System.out.println("like handled");
        } else if(request.getParameter("action").equalsIgnoreCase("dislike")) {
            bean.handleDislike(Integer.parseInt(request.getParameter("userId")));
            System.out.println("dislike handled");
        }
    }

    private void handleLogin(HttpServletRequest request, HttpServletResponse response){
        if(bean==null){
            bean = new UserBeanBean();
            request.getSession().setAttribute("bean", bean);
        }
        boolean correctUser = bean.handleLogin(request.getParameter("email"), request.getParameter("password"));
        System.out.println("user exists : " +correctUser);
        if(correctUser){
            //go to homepage
            try {
                response.sendRedirect("homepage.jsp");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(!correctUser){
            System.out.println("user does not exist or account not confirmed!");
            //TODO: Need to send error message back to user, user doesnt exist, not confirmed or wrong combination.

        }
    }

    private void handleSignUp(HttpServletRequest request, HttpServletResponse response) {
        if(bean == null){
            bean = new UserBeanBean();
            request.getSession().setAttribute("bean", bean);
        }
        byte confirmed =0;
        String toHash = request.getParameter("email").concat(request.getParameter("university"));
        String authToken = HashHelper.createHash(toHash);
        String email = request.getParameter("email");

        boolean signUp = bean.handleSignUp(request.getParameter("firstName"), request.getParameter("lastName"),
               email, request.getParameter("university"), confirmed, authToken);

        if(signUp){
            System.out.println("send mail with activation code, user signed up");
            MailSender mailSender = new MailSender();
            mailSender.sendMessage(email, authToken);
            try {
                response.sendRedirect("mailSent.jsp?email="+request.getParameter("email"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(!signUp){
            System.out.println("sign up failed");

        }
    }

    private void handleConfirmation(HttpServletRequest request, HttpServletResponse response){

        try{
            boolean pass = bean.setPassword(request.getParameter("password"));

            if(pass){
                response.sendRedirect("profileCreation.jsp");
            }
            else{
                System.out.println("confirmation failed");
            }
        }catch (Exception ee){
            System.out.println(ee.getMessage());
        }
    }

    private void handleCreateProfile(HttpServletRequest request, HttpServletResponse response){
        boolean uploadPic = false;
        boolean created;
        if(bean == null){
            bean = (UserBeanBean)request.getSession().getAttribute("bean");
        }

        created = bean.createProfile(Integer.valueOf(request.getParameter("age")),
                request.getParameter("gender"),
                request.getParameter("soughtGender"),
                request.getParameter("programme"),
                request.getParameter("bio"));

        if(created){
            uploadPic = uploadImage(request,response);
        }
        else{
            System.out.println("failed in creating profile");
        }


        if(created && uploadPic){
            ImagesService imagesService = ImagesServiceFactory.getImagesService();
            BlobKey blobKey = new BlobKey(bean.getImage());
            ServingUrlOptions servingUrlOptions = ServingUrlOptions.Builder.withBlobKey(blobKey);

            System.out.println("profile created");
        }
        else if(created==false || uploadPic==false){
            System.out.println("failed in creation of profile");
        }
    }

    private boolean uploadImage(HttpServletRequest request, HttpServletResponse response){

        boolean pictureUploadSuccess = false;
        try{
            Map<String, List<BlobKey>> blobs = blobStoreService.getUploads(request);
            List<BlobKey> blobKeys = blobs.get("image1");
            bean.addImage(blobKeys.get(0).getKeyString(), 1);

            List<BlobKey> blobK = blobs.get("image2");
            bean.addImage(blobK.get(0).getKeyString(), 2);

            pictureUploadSuccess = true;


        }catch (Exception ee){
            System.out.println("Error" +ee.getMessage());
            ee.printStackTrace();
        }

        return pictureUploadSuccess;
    }



}
