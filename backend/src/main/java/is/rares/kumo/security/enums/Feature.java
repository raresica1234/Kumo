package is.rares.kumo.security.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Feature {
    OWNER("owner"),

    ADMIN("admin"),

    CREATE_REGISTER_INVITE("create_register_invite"),

    GET_PATH_POINT("get_path_point"),
    CREATE_PATH_POINT("create_path_point"),
    UPDATE_PATH_POINT("update_path_point"),
    DELETE_PATH_POINT("delete_path_point"),

    GET_EXPLORATION_ROLE("get_exploration_role"),
    CREATE_EXPLORATION_ROLE("create_exploration_role"),
    UPDATE_EXPLORATION_ROLE("update_exploration_role"),
    DELETE_EXPLORATION_ROLE("delete_exploration_role"),

    GET_EXPLORATION_PERMISSION("get_exploration_permission"),
    CREATE_EXPLORATION_PERMISSION("create_exploration_permission"),
    UPDATE_EXPLORATION_PERMISSION("update_exploration_permission"),
    DELETE_EXPLORATION_PERMISSION("delete_exploration_permission")

    ;

    private final String name;
}
