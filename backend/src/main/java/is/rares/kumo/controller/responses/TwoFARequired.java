package is.rares.kumo.controller.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class TwoFARequired implements Serializable {
    private boolean required;
}