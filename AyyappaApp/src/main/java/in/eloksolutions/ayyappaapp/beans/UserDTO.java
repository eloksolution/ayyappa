package in.eloksolutions.ayyappaapp.beans;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by welcome on 7/17/2017.
 */

public class UserDTO {
    String userId;
    String firstName;
    String lastName;
    String mobile;
    String email;
    String area;
    String city;
    String state;
    String password;
    private LatLong loc;
    private String lat;
    private String lon;
    List<GroupDTO> groups=new ArrayList<GroupDTO>();

    public List<GroupDTO> getGroups() {
        return groups;
    }

    public void setGroups(List<GroupDTO> groups) {
        this.groups = groups;
    }

    public class LatLong{
        String lat;
        String lon;
        public LatLong(){

        }

        public String getLat() {
            return lat;
        }

        public String getLon() {
            return lon;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }

        public void setLon(String lon) {
            this.lon = lon;
        }

        public LatLong(String lat, String lon) {
            super();
            this.lat = lat;
            this.lon = lon;
        }

    }
    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lati) {
        this.lat = lati;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;

    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LatLong getLoc() {
        return loc;
    }

    public void setLoc(LatLong loc) {
        this.loc = loc;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "userId='" + userId + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", mobile='" + mobile + '\'' +
                ", email='" + email + '\'' +
                ", area='" + area + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", password='" + password + '\'' +
                ", loc=" + loc +
                ", lat='" + lat + '\'' +
                ", lon='" + lon + '\'' +
                '}';
    }
}
