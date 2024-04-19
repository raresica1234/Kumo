package is.rares.kumo.security.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Feature {
    OWNER("owner"),

    CREATE_REGISTER_INVITE("create_register_invite"),

    GET_PATH_POINT("get_path_point"),
    CREATE_PATH_POINT("create_path_point"),
    UPDATE_PATH_POINT("update_path_point"),
    DELETE_PATH_POINT("delete_path_point")

    ;

    private final String name;
}
