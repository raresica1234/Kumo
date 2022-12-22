package is.rares.kumo.security.enums;

import lombok.Getter;

public enum UserClaims {
    IS_USING_TWO_FA("isUsing2FA"),

    TWO_FA_NEEDED("twoFANeeded");

    @Getter
    private final String claim;

    UserClaims(String claim) {
        this.claim = claim;
    }
}