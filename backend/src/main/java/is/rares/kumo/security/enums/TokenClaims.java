package is.rares.kumo.security.enums;

import lombok.Getter;

@Getter
public enum TokenClaims {
    IS_USING_TWO_FA("isUsing2FA"),

    TWO_FA_NEEDED("twoFANeeded"),

    REFRESH_TOKEN("refreshToken"),

    RANDOM_ID("randomId"),

    TOKEN_TYPE("tokenType"),

    MAX_USAGE("maxUsage"),
    VALIDITY("validity")

    ;

    private final String claim;

    TokenClaims(String claim) {
        this.claim = claim;
    }
}
