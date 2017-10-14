package in.eloksolutions.ayyappaapp.beans;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by welcome on 6/28/2017.
 */

public class GroupDTO {
    String groupId;
    String name;
    String description;
    String owner;
    String createDate;
    int numberOfMembers;
    String type;
    String imagePath;
    String groupCatagory;
    String isMember;
    Bitmap bitmap;
    String ownerName;

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    List<RegisterDTO> groupMembers=new ArrayList<RegisterDTO>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGroupid() {
        return groupId;
    }

    public void setGroupid(String groupid) {
        this.groupId = groupid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public int getNumberOfMembers() {
        return numberOfMembers;
    }

    public void setNumberOfMembers(int numberOfMembers) {
        this.numberOfMembers = numberOfMembers;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getGroupCatagory() {
        return groupCatagory;
    }

    public void setGroupCatagory(String groupCatagory) {
        this.groupCatagory = groupCatagory;
    }

    public List<RegisterDTO> getGroupMembers() {
        return groupMembers;
    }

    public void setGroupMembers(List<RegisterDTO> groupMembers) {
        this.groupMembers = groupMembers;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getIsMember() {
        return isMember;
    }

    public void setIsMember(String isMember) {
        this.isMember = isMember;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    @Override
    public String toString() {
        return "GroupDto{" +
                "groupid=" + groupId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", owner='" + owner + '\'' +
                ", createDate='" + createDate + '\'' +
                ", numberOfMembers=" + numberOfMembers +
                ", type='" + type + '\'' +
                ", imagePath='" + imagePath + '\'' +
                ", groupCatagory='" + groupCatagory + '\'' +
                ", isMember='" + isMember + '\'' +
                ", ownerName='" + ownerName + '\'' +
                '}';
    }


}
