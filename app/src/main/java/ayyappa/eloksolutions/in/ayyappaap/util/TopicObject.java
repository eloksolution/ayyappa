package ayyappa.eloksolutions.in.ayyappaap.util;

/**
 * Created by welcome on 7/18/2017.
 */

public class TopicObject {
   private String groupId;
    private String topicId;
    private String topic;
    private String description;
    private int imgResource;

    public TopicObject(String groupId, String topicId, int imgResource, String topic, String description) {
        this.groupId = groupId;
        this.topicId = topicId;
        this.topic = topic;
        this.description=description;
        this.imgResource=imgResource;

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

    public int getImgResource() {
        return imgResource;
    }

    public void setImgResource(int imgResource) {
        this.imgResource = imgResource;
    }

    @Override
    public String toString() {
        return "TopicObject{" +
                "groupId='" + groupId + '\'' +
                ", topicId='" + topicId + '\'' +
                ", topic='" + topic + '\'' +
                ", description='" + description + '\'' +
                ", imgResource=" + imgResource +
                '}';
    }
}

