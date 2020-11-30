package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.components.UserRole;
import ar.edu.itba.paw.model.location.City;
import ar.edu.itba.paw.model.location.Country;
import ar.edu.itba.paw.model.location.Location;
import ar.edu.itba.paw.model.location.State;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.ws.rs.core.UriInfo;
import java.util.Date;
import java.net.URI;

public class UserDto {
    private long id;

    // TODO: Validar rol
    private String role;

    @Size(max = 50)
    @NotEmpty
    private String password;

    @Size(max = 25)
    @NotEmpty
    private String firstName;

    @Size(max = 25)
    @NotEmpty
    private String lastName;

    @Size(max = 15)
    @NotEmpty
    private String realId;

    // TODO: Validar date?
    private Date birthDate;

    @Size(max = 255)
    @NotEmpty
    @Pattern(regexp="^([^@]+@[^@]+\\.[a-zA-Z]{2,}$)?")
    private String email;

    @Size(max = 25)
    @Pattern(regexp = "[0-9]*")
    private String phone;

    @Size(max = 100)
    @Pattern(regexp = "^((http(s)?://)?(www\\.)?(linkedin\\.com/in/)([-a-zA-Z0-9@:%_+.~#?&=/]*)$)?")
    private String linkedin;

    // TODO: Validar date?
    private Date joinDate;
    private boolean verified;
    private String locale;

    private int countryId, stateId, cityId;

    private URI location, image, projectList, receivedMessages, sentMessages, favorites;

    public static UserDto fromUser(User user, UriInfo uriInfo) {
        final UserDto userDto = new UserDto();
        userDto.id = user.getId();
        userDto.role = UserRole.valueOf(user.getRole()).getRole();
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

        userDto.countryId = user.getLocation().getCountry().getId();
        userDto.stateId = user.getLocation().getState().getId();
        userDto.cityId = user.getLocation().getCity().getId();

        return userDto;
    }

    public static User toUser(UserDto userDto) {
        return new User(UserRole.getEnum(userDto.getRole()).getId(), userDto.getPassword(),
                userDto.getFirstName(), userDto.getLastName(), userDto.getRealId(), userDto.getBirthDate(),
                new Location(new Country(userDto.getCountryId()), new State(userDto.getStateId()), new City(userDto.getCityId())),
                userDto.getEmail(), userDto.getPhone(), userDto.getLinkedin(), null);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
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
}
