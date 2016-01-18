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
    private UserBeanBean bean;

    private Byte TRUE = 1;
    private Byte FALSE = 0;

    private String ERROR_LOGIN = "Invalid email or password, please try again";
    private String ERROR_SIGN_UP = "Sign up failed";
    private String ERROR_EDIT_PROFILE = "Profile could not be updated, please try again";

    private BlobstoreService blobStoreService = BlobstoreServiceFactory.getBlobstoreService();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        bean = (UserBeanBean)request.getSession().getAttribute("bean");

        if(request.getParameter("button").equalsIgnoreCase("login")) {
            handleLogin(request,response);
        } else if(request.getParameter("button").equalsIgnoreCase("Sign up")){
            System.out.println("signup");
            handleSignUp(request, response);
        } else if(request.getParameter("button").equalsIgnoreCase("Confirm")){
            System.out.println("confirm account");
            handleConfirmation(request, response);
        } else if(request.getParameter("button").equalsIgnoreCase("Create profile")){
            System.out.println("create profile");
            handleCreateProfile(request, response);
        } else if(request.getParameter("button").equalsIgnoreCase("save")){
            System.out.println("edit profile");
            handleEditProfile(request, response);
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
        if(correctUser){
            try {
                response.sendRedirect("homepage.jsp");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(!correctUser){
            try {
                response.sendRedirect("index.jsp?error="+ERROR_LOGIN);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleSignUp(HttpServletRequest request, HttpServletResponse response) {
        if(bean == null){
            bean = new UserBeanBean();
            request.getSession().setAttribute("bean", bean);
        }
        String toHash = request.getParameter("email").concat(request.getParameter("university"));
        String authToken = HashHelper.createHash(toHash);
        String email = request.getParameter("email");

        boolean signUp = bean.handleSignUp(request.getParameter("firstName"), request.getParameter("lastName"),
                email, request.getParameter("university"), authToken);

        if(signUp){
            MailSender mailSender = new MailSender();
            mailSender.sendMessage(email, authToken);
            try {
                response.sendRedirect("mailSent.jsp?email="+request.getParameter("email"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(!signUp){
            try {
                response.sendRedirect("signup.html?error="+ERROR_SIGN_UP);
            }catch (IOException ee){
                System.out.println("error directing to signup page");
            }
        }
    }

    private void handleConfirmation(HttpServletRequest request, HttpServletResponse response){
        bean = (UserBeanBean)request.getSession().getAttribute("bean");
        if(bean==null){
            bean = new UserBeanBean();
            request.getSession().setAttribute("bean", bean);
        }

        try{
            boolean pass = bean.setPassword(request.getParameter("password"));

            if(pass){
                response.sendRedirect("profileCreation.jsp");
            }
            else{
                //TODO: If confirmation fails...
                System.out.println("confirmation failed");
            }
        }catch (Exception ee){
            System.out.println(ee.getMessage());
        }
    }

    private void handleCreateProfile(HttpServletRequest request, HttpServletResponse response){
        boolean uploadPic = false;
        boolean created;
        bean = (UserBeanBean)request.getSession().getAttribute("bean");
        if(bean == null){
            bean = new UserBeanBean();
            request.getSession().setAttribute("bean", bean);
        }

        created = bean.createProfile(Integer.valueOf(request.getParameter("age")),
                request.getParameter("gender"),
                request.getParameter("soughtGender"),
                request.getParameter("programme"),
                request.getParameter("bio"));

        if(created){
            uploadPic = uploadInitialImage(request);
        }
        else{
            System.out.println("failed in creating profile");
        }

        if(created && uploadPic){
            try {
                response.sendRedirect("homepage.jsp");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(!created || !uploadPic){
            System.out.println("failed in creation of profile");
        }
    }

    private void handleEditProfile(HttpServletRequest request, HttpServletResponse response){
        bean = (UserBeanBean)request.getSession().getAttribute("bean");

        if(bean==null){
            UserBeanBean bean = new UserBeanBean();
            request.getSession().setAttribute("bean", bean);
        }

        try{
            boolean editSuccess = bean.handleEditProfile(request.getParameter("secondName"),
                    request.getParameter("password"),
                    request.getParameter("programme"),
                    request.getParameter("bio"));

            boolean newImageUpload = uploadNewImages(request);

            if(editSuccess && newImageUpload){
                response.sendRedirect("editProfile.jsp");
            }
            else{
                response.sendRedirect("editProfile.jsp?error="+ERROR_EDIT_PROFILE);
            }
        }catch (Exception ee){
            ee.printStackTrace();
        }
    }

    private boolean uploadInitialImage(HttpServletRequest request){
        boolean pictureUploadSuccess = false;
        try{
            Map<String, List<BlobKey>> blobs = blobStoreService.getUploads(request);
            List<BlobKey> blobKeys = blobs.get("image1");
            boolean picOne = bean.addImage(blobKeys.get(0).getKeyString(), 1);

            List<BlobKey> blobK = blobs.get("image2");
            boolean picTwo = bean.addImage(blobK.get(0).getKeyString(), 2);

            if(picOne && picTwo){
                pictureUploadSuccess = true;
            }
            else{
                System.out.println("Error adding images!");
            }

        }catch (Exception ee){
            System.out.println("Error in adding image : " +ee.getMessage());
            ee.printStackTrace();
        }

        return pictureUploadSuccess;
    }

    private boolean uploadNewImages(HttpServletRequest request){
        boolean pictureUploadSuccess = false;
        try{
            Map<String, List<BlobKey>> blobs = blobStoreService.getUploads(request);
            if(blobs.size() > 0){
                List<BlobKey> blobKey1 = blobs.get("image1");
                System.out.println(request.getParameter("image1controller") + " size: " + blobKey1.size());
                if(!blobKey1.isEmpty() && request.getParameter("image1controller") != null && !request.getParameter("image1controller").isEmpty()){
                    try{
                        bean.addImage(blobKey1.get(0).getKeyString(), 1);
                        pictureUploadSuccess = true;
                    }catch (Exception ee){
                        System.out.println("Error uploading new image 1");
                    }
                }
                List<BlobKey> blobKey2 = blobs.get("image2");
                if(!blobKey2.isEmpty() && request.getParameter("image2controller") != null && !request.getParameter("image2controller").isEmpty()){
                    try{
                        bean.addImage(blobKey2.get(0).getKeyString(), 2);
                        pictureUploadSuccess = true;
                    }catch (Exception ee){
                        System.out.println("Error uploading new image 2");
                    }
                }
                List<BlobKey> blobKey3 = blobs.get("image3");
                if(!blobKey3.isEmpty() && request.getParameter("image3controller") != null && !request.getParameter("image3controller").isEmpty()){
                    try{
                        bean.addImage(blobKey3.get(0).getKeyString(), 3);
                        pictureUploadSuccess = true;
                    }catch (Exception ee){
                        System.out.println("Error uploading new image 3");
                    }
                }
                List<BlobKey> blobKey4 = blobs.get("image4");
                if(!blobKey4.isEmpty() && request.getParameter("image4controller") != null && !request.getParameter("image4controller").isEmpty()){
                    try{
                        bean.addImage(blobKey4.get(0).getKeyString(), 4);
                        pictureUploadSuccess = true;
                    }catch (Exception ee){
                        System.out.println("Error uploading new image 4");
                    }
                }
                List<BlobKey> blobKey5 = blobs.get("image5");
                if(!blobKey5.isEmpty() && request.getParameter("image5controller") != null && !request.getParameter("image5controller").isEmpty()){
                    try{
                        bean.addImage(blobKey5.get(0).getKeyString(), 5);
                        pictureUploadSuccess = true;
                    }catch (Exception ee){
                        System.out.println("Error uploading new image 5");
                    }
                }
            }
            if(blobs.size() == 0)
                pictureUploadSuccess = true;
        }catch (Exception ee){
            System.out.println("Error in adding image : " +ee.getMessage());
        }

        return pictureUploadSuccess;
    }

}
