package hibernate;

import javax.persistence.*;

/**
 * Created by davidmunro on 05/01/2016.
 */
@Entity
@Table(name = "relationships", schema = "", catalog = "FrendzDB")
public class RelationshipsEntity {
    private int relationshipId;
    private Integer user1;
    private Integer user2;
    private Byte visit;
    private Byte like;
    private Byte block;

    @Id
    @GeneratedValue
    @Column(name = "relationship_id", nullable = false, insertable = true, updatable = true)
    public int getRelationshipId() {
        return relationshipId;
    }

    public void setRelationshipId(int relationshipId) {
        this.relationshipId = relationshipId;
    }

    @Basic
    @Column(name = "user_1", nullable = true, insertable = true, updatable = true)
    public Integer getUser1() {
        return user1;
    }

    public void setUser1(Integer user1) {
        this.user1 = user1;
    }

    @Basic
    @Column(name = "user_2", nullable = true, insertable = true, updatable = true)
    public Integer getUser2() {
        return user2;
    }

    public void setUser2(Integer user2) {
        this.user2 = user2;
    }

    @Basic
    @Column(name = "visited", nullable = true, insertable = true, updatable = true)
    public Byte getVisit() {
        return visit;
    }

    public void setVisit(Byte visit) {
        this.visit = visit;
    }

    @Basic
    @Column(name = "like_user", nullable = true, insertable = true, updatable = true)
    public Byte getLike() {
        return like;
    }

    public void setLike(Byte like) {
        this.like = like;
    }

    @Basic
    @Column(name = "block_user", nullable = true, insertable = true, updatable = true)
    public Byte getBlock() {
        return block;
    }

    public void setBlock(Byte block) {
        this.block = block;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RelationshipsEntity that = (RelationshipsEntity) o;

        if (relationshipId != that.relationshipId) return false;
        if (user1 != null ? !user1.equals(that.user1) : that.user1 != null) return false;
        if (user2 != null ? !user2.equals(that.user2) : that.user2 != null) return false;
        if (visit != null ? !visit.equals(that.visit) : that.visit != null) return false;
        if (like != null ? !like.equals(that.like) : that.like != null) return false;
        if (block != null ? !block.equals(that.block) : that.block != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = relationshipId;
        result = 31 * result + (user1 != null ? user1.hashCode() : 0);
        result = 31 * result + (user2 != null ? user2.hashCode() : 0);
        result = 31 * result + (visit != null ? visit.hashCode() : 0);
        result = 31 * result + (like != null ? like.hashCode() : 0);
        result = 31 * result + (block != null ? block.hashCode() : 0);
        return result;
    }
}
