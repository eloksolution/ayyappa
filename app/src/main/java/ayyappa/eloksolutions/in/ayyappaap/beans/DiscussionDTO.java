package ayyappa.eloksolutions.in.ayyappaap.beans;

/**
 * Created by welcome on 7/19/2017.
 */

public class DiscussionDTO {
    String topicId;
    String comment;
    String ownerId;

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    @Override
    public String toString() {
        return "DiscussionDTO{" +
                "topicId='" + topicId + '\'' +
                ", comment='" + comment + '\'' +
                ", owner='" + ownerId + '\'' +
                '}';
    }
}
