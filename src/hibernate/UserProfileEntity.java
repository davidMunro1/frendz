package hibernate;

import javax.persistence.*;

/**
 * Created by davidmunro on 22/12/2015.
 */
@Entity
@Table(name = "user_profile", schema = "", catalog = "FrendzDB")
public class UserProfileEntity {
    private String gender;
    private String soughtGender;
    private Integer age;
    private String programme;
    private String bio;
    private String image1;
    private String image2;
    private String image3;
    private String image4;
    private String image5;
    private int userId;

    @Basic
    @Column(name = "gender", nullable = true, insertable = true, updatable = true, length = 6)
    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Basic
    @Column(name = "sought_gender", nullable = true, insertable = true, updatable = true, length = 6)
    public String getSoughtGender() {
        return soughtGender;
    }

    public void setSoughtGender(String soughtGender) {
        this.soughtGender = soughtGender;
    }

    @Basic
    @Column(name = "age", nullable = true, insertable = true, updatable = true)
    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Basic
    @Column(name = "programme", nullable = true, insertable = true, updatable = true, length = 65)
    public String getProgramme() {
        return programme;
    }

    public void setProgramme(String programme) {
        this.programme = programme;
    }

    @Basic
    @Column(name = "bio", nullable = true, insertable = true, updatable = true, length = 150)
    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    @Basic
    @Column(name = "image_1", nullable = true, insertable = true, updatable = true, length = 45)
    public String getImage1() {
        return image1;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }

    @Basic
    @Column(name = "image_2", nullable = true, insertable = true, updatable = true, length = 45)
    public String getImage2() {
        return image2;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }

    @Basic
    @Column(name = "image_3", nullable = true, insertable = true, updatable = true, length = 45)
    public String getImage3() {
        return image3;
    }

    public void setImage3(String image3) {
        this.image3 = image3;
    }

    @Basic
    @Column(name = "image_4", nullable = true, insertable = true, updatable = true, length = 45)
    public String getImage4() {
        return image4;
    }

    public void setImage4(String image4) {
        this.image4 = image4;
    }

    @Basic
    @Column(name = "image_5", nullable = true, insertable = true, updatable = true, length = 45)
    public String getImage5() {
        return image5;
    }

    public void setImage5(String image5) {
        this.image5 = image5;
    }

    @Id
    @Column(name = "user_id", nullable = false, insertable = true, updatable = true)
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserProfileEntity that = (UserProfileEntity) o;

        if (userId != that.userId) return false;
        if (gender != null ? !gender.equals(that.gender) : that.gender != null) return false;
        if (soughtGender != null ? !soughtGender.equals(that.soughtGender) : that.soughtGender != null) return false;
        if (age != null ? !age.equals(that.age) : that.age != null) return false;
        if (programme != null ? !programme.equals(that.programme) : that.programme != null) return false;
        if (bio != null ? !bio.equals(that.bio) : that.bio != null) return false;
        if (image1 != null ? !image1.equals(that.image1) : that.image1 != null) return false;
        if (image2 != null ? !image2.equals(that.image2) : that.image2 != null) return false;
        if (image3 != null ? !image3.equals(that.image3) : that.image3 != null) return false;
        if (image4 != null ? !image4.equals(that.image4) : that.image4 != null) return false;
        if (image5 != null ? !image5.equals(that.image5) : that.image5 != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = gender != null ? gender.hashCode() : 0;
        result = 31 * result + (soughtGender != null ? soughtGender.hashCode() : 0);
        result = 31 * result + (age != null ? age.hashCode() : 0);
        result = 31 * result + (programme != null ? programme.hashCode() : 0);
        result = 31 * result + (bio != null ? bio.hashCode() : 0);
        result = 31 * result + (image1 != null ? image1.hashCode() : 0);
        result = 31 * result + (image2 != null ? image2.hashCode() : 0);
        result = 31 * result + (image3 != null ? image3.hashCode() : 0);
        result = 31 * result + (image4 != null ? image4.hashCode() : 0);
        result = 31 * result + (image5 != null ? image5.hashCode() : 0);
        result = 31 * result + userId;
        return result;
    }
}
