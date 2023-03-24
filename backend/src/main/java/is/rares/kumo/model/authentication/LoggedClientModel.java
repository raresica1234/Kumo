package is.rares.kumo.model.authentication;

import io.swagger.annotations.ApiModel;
import is.rares.kumo.security.token.Token;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@ApiModel(description = "Specifies a logged user session")
@AllArgsConstructor
@NoArgsConstructor
public class LoggedClientModel {

    UUID uuid;

    ClientLocationModel clientLocationModel;

    Date lastActivityData;

    public LoggedClientModel(Token token) {
        this.uuid = token.getUuid();
        this.clientLocationModel = new ClientLocationModel(token.getClientLocation());
        this.lastActivityData = token.getLastActivityDate();
    }

}

