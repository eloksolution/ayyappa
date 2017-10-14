package in.eloksolutions.ayyappaapp.util;

/**
 * Created by welcome on 7/18/2017.
 */

public class TopicObject {
   private String groupId;
    private String topicId;
    private String topic;
    private String description;
    private String imgResource;
    private String imgPath;
    private String owner;
    private String name;
    private String createDate;

    public TopicObject(String groupId, String topicId,  String topic,String description,  String owner,String name,String imgResource,String createDate) {
        this.groupId = groupId;
        this.topicId = topicId;
        this.topic = topic;
        this.description=description;
        this.imgResource=imgResource;
        this.owner=owner;
        this.name=name;
        this.createDate=createDate;

    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImgResource() {
        return imgResource;
    }

    public void setImgResource(String imgResource) {
        this.imgResource = imgResource;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    @Override
    public String toString() {
        return "TopicObject{" +
                "groupId='" + groupId + '\'' +
                ", topicId='" + topicId + '\'' +
                ", topic='" + topic + '\'' +
                ", description='" + description + '\'' +
                ", imgResource='" + imgResource + '\'' +
                ", imgPath='" + imgPath + '\'' +
                ", owner='" + owner + '\'' +
                ", name='" + name + '\'' +
                ", createDate='" + createDate + '\'' +
                '}';
    }
}

