package ayyappa.eloksolutions.in.ayyappaap.beans;

/**
 * Created by welcome on 7/11/2017.
 */

public class TopicDTO {
    String groupId;
    String topicId;
    String description;
    String topic;
    String discussion;
    String createDate;
    String owner;
    int noOfMembers;

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getDiscussion() {
        return discussion;
    }

    public void setDiscussion(String discussion) {
        this.discussion = discussion;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public int getNoOfMembers() {
        return noOfMembers;
    }

    public void setNoOfMembers(int noOfMembers) {
        this.noOfMembers = noOfMembers;
    }

    @Override
    public String toString() {
        return "TopicDTO{" +
                "groupId='" + groupId + '\'' +
                ", topicId='" + topicId + '\'' +
                ", description='" + description + '\'' +
                ", topic='" + topic + '\'' +
                ", discussion='" + discussion + '\'' +
                ", createDate='" + createDate + '\'' +
                ", owner='" + owner + '\'' +
                ", noOfMembers=" + noOfMembers +
                '}';
    }
}
