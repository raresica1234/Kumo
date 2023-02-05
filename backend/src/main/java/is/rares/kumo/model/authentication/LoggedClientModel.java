package is.rares.kumo.model.authentication;

import is.rares.kumo.security.token.Token;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
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

