package in.eloksolutions.ayyappaapp.beans;

import java.util.ArrayList;
import java.util.List;

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
    String imgPath;
    String name;
    String ownerName;
    int noOfMembers;
List<TopicDissDTO> discussions = new ArrayList<TopicDissDTO>();

    public List<TopicDissDTO> getDiscussions() {
        return discussions;
    }

    public void setDiscussions(List<TopicDissDTO> discussions) {
        this.discussions = discussions;
    }

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

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
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
                ", imgPath=" + imgPath +
                ", name=" + name +
                ", ownerName=" + ownerName +
                '}';
    }
}
