package ar.edu.itba.paw.webapp.dto.user;

import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.enums.UserRole;
import ar.edu.itba.paw.webapp.forms.validators.ValidEnum;
import org.hibernate.validator.constraints.NotBlank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.ws.rs.core.UriInfo;

public class FullUserDto extends UpdatableUserDto {
    private static final Logger LOGGER = LoggerFactory.getLogger(FullUserDto.class);

    @ValidEnum(enumClazz = UserRole.class)
    private String role;

    @Size(min = 1, max = 255)
    @NotBlank
    @Pattern(regexp="^([^@]+@[^@]+\\.[a-zA-Z]{2,}$)?")
    private String email;

    public static FullUserDto fromUser(User user, UriInfo uriInfo) {
        final FullUserDto userDto = new FullUserDto();
        userDto.setId(user.getId());
        userDto.setRole(UserRole.valueOf(user.getRole()).getRole());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setRealId(user.getRealId());
        userDto.setBirthDate(user.getBirthDate());
        userDto.setEmail(user.getEmail());
        userDto.setPhone(user.getPhone());
        userDto.setLinkedin(user.getLinkedin());
        userDto.setJoinDate(user.getJoinDate());
        userDto.setVerified(user.isVerified());
        userDto.setLocale(user.getLocale());

        userDto.setLocation(uriInfo.getAbsolutePathBuilder().path("location").build());
        userDto.setProjectList(uriInfo.getAbsolutePathBuilder().path("projects").build());
        userDto.setReceivedMessages(uriInfo.getAbsolutePathBuilder().path("received_messages").build());
        userDto.setSentMessages(uriInfo.getAbsolutePathBuilder().path("sent_messages").build());
        userDto.setFavorites(uriInfo.getAbsolutePathBuilder().path("favorites").build());
        userDto.setLocation(uriInfo.getAbsolutePathBuilder().path("location").build());

        if (user.getImage_id() != null) {
            userDto.setImageExists(true);
            userDto.setImage(uriInfo.getBaseUriBuilder().path("/images/users").path(String.valueOf(user.getImage_id())).build());
        }

        return userDto;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
