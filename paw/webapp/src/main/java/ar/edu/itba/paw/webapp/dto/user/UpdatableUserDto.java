package ar.edu.itba.paw.webapp.dto.user;

import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.location.City;
import ar.edu.itba.paw.model.location.Country;
import ar.edu.itba.paw.model.location.Location;
import ar.edu.itba.paw.model.location.State;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.net.URI;
import java.util.Date;

public class UpdatableUserDto {

    private long id;

    @Size(min = 1, max = 25)
    @NotBlank
    private String firstName;

    @Size(min = 1, max = 25)
    @NotBlank
    private String lastName;

    @Size(min = 1, max = 15)
    @NotBlank
    private String realId;

    @NotNull
    private Date birthDate;

    @Size(max = 25)
    @Pattern(regexp = "[0-9]*")
    private String phone;

    @Size(max = 100)
    @Pattern(regexp = "^((http(s)?://)?(www\\.)?(linkedin\\.com/in/)([-a-zA-Z0-9@:%_+.~#?&=/]*)$)?")
    private String linkedin;

    private Date joinDate;
    private boolean verified;
    private String locale;

    private int countryId, stateId, cityId;

    private boolean imageExists;

    private URI location, image, projectList, receivedMessages, sentMessages, favorites;

    public static User toUser(UpdatableUserDto userDto) {
        return new User(null, null, userDto.firstName, userDto.lastName, userDto.realId, userDto.birthDate,
                new Location(new Country(userDto.countryId), new State(userDto.stateId), new City(userDto.cityId)),
                        null, userDto.phone, userDto.linkedin, null);
    }

    /* Getters and setters */

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    public int getStateId() {
        return stateId;
    }

    public void setStateId(int stateId) {
        this.stateId = stateId;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
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

    public boolean isImageExists() {
        return imageExists;
    }

    public void setImageExists(boolean imageExists) {
        this.imageExists = imageExists;
    }
}
