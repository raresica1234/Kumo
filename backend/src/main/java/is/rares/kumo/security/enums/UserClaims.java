package is.rares.kumo.security.enums;

import lombok.Getter;

@Getter
public enum UserClaims {
    IS_USING_TWO_FA("isUsing2FA"),

    TWO_FA_NEEDED("twoFANeeded"),

    REFRESH_TOKEN("refreshToken"),

    RANDOM_ID("randomId"),

    TOKEN_TYPE("tokenType")

    ;

    private final String claim;

    UserClaims(String claim) {
        this.claim = claim;
    }
}
