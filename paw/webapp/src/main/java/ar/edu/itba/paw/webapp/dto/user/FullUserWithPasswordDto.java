package ar.edu.itba.paw.webapp.dto.user;

import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.components.UserRole;
import ar.edu.itba.paw.model.location.City;
import ar.edu.itba.paw.model.location.Country;
import ar.edu.itba.paw.model.location.Location;
import ar.edu.itba.paw.model.location.State;
import ar.edu.itba.paw.webapp.forms.validators.EqualFields;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Size;

@EqualFields({"password", "repeatPassword"})
public class FullUserWithPasswordDto extends FullUserDto {
    @Size(min = 1, max = 50)
    @NotBlank
    private String password;

    private String repeatPassword;

    public static User toUser(FullUserWithPasswordDto userDto) {
        return new User(UserRole.getEnum(userDto.getRole()).getId(), userDto.getPassword(),
                userDto.getFirstName(), userDto.getLastName(), userDto.getRealId(), userDto.getBirthDate(),
                new Location(new Country(userDto.getCountryId()), new State(userDto.getStateId()), new City(userDto.getCityId())),
                userDto.getEmail(), userDto.getPhone(), userDto.getLinkedin(), null);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRepeatPassword() {
        return repeatPassword;
    }

    public void setRepeatPassword(String repeatPassword) {
        this.repeatPassword = repeatPassword;
    }
}
