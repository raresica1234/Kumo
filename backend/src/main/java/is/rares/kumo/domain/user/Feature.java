package is.rares.kumo.domain.user;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Feature {
    OWNER("owner"),
    CREATE_REGISTER_INVITE("create_register_invite");

    private final String name;
}
