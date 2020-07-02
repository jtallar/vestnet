package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.model.User;

import javax.ws.rs.core.UriInfo;
import java.util.Date;
import java.net.URI;

public class UserDto {
    private long id;
    private int role;
    private String password;
    private String firstName;
    private String lastName;
    private String realId;
    private Date birthDate;
    private String email;
    private String phone;
    private String linkedin;
    private Date joinDate;
    private boolean verified;
    private String locale;

//    private long imageId;

    private URI location, image, projectList, receivedMessages, sentMessages, favorites;

    public static UserDto fromUser(User user, UriInfo uriInfo) {
        final UserDto userDto = new UserDto();
        userDto.id = user.getId();
        userDto.role = user.getRole();
        userDto.firstName = user.getFirstName();
        userDto.lastName = user.getLastName();
        userDto.realId = user.getRealId();
        userDto.birthDate = user.getBirthDate();
        userDto.email = user.getEmail();
        userDto.phone = user.getPhone();
        userDto.linkedin = user.getLinkedin();
        userDto.joinDate = user.getJoinDate();
        userDto.verified = user.isVerified();
        userDto.locale = user.getLocale();

        userDto.location = uriInfo.getAbsolutePathBuilder().path("location").build();
        userDto.image = uriInfo.getAbsolutePathBuilder().path("image").build();
        userDto.projectList = uriInfo.getAbsolutePathBuilder().path("projects").build();
        userDto.receivedMessages = uriInfo.getAbsolutePathBuilder().path("received_messages").build();
        userDto.sentMessages = uriInfo.getAbsolutePathBuilder().path("sent_messages").build();
        userDto.favorites = uriInfo.getAbsolutePathBuilder().path("favorites").build();

        return userDto;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getRealId() {
        return realId;
    }

    public void setRealId(String realId) {
        this.realId = realId;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLinkedin() {
        return linkedin;
    }

    public void setLinkedin(String linkedin) {
        this.linkedin = linkedin;
    }

    public Date getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(Date joinDate) {
        this.joinDate = joinDate;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public URI getLocation() {
        return location;
    }

    public void setLocation(URI location) {
        this.location = location;
    }

    public URI getImage() {
        return image;
    }

    public void setImage(URI image) {
        this.image = image;
    }

    public URI getProjectList() {
        return projectList;
    }

    public void setProjectList(URI projectList) {
        this.projectList = projectList;
    }

    public URI getReceivedMessages() {
        return receivedMessages;
    }

    public void setReceivedMessages(URI receivedMessages) {
        this.receivedMessages = receivedMessages;
    }

    public URI getSentMessages() {
        return sentMessages;
    }

    public void setSentMessages(URI sentMessages) {
        this.sentMessages = sentMessages;
    }

    public URI getFavorites() {
        return favorites;
    }

    public void setFavorites(URI favorites) {
        this.favorites = favorites;
    }
}
