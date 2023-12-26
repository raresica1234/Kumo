package is.rares.kumo.domain.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Feature {
    OWNER("owner");

    private final String name;

}
