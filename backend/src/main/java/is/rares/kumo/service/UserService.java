package is.rares.kumo.service;

import is.rares.kumo.core.config.KumoConfig;
import is.rares.kumo.core.exceptions.KumoException;
import is.rares.kumo.core.exceptions.codes.AccountCodeErrorCodes;
import is.rares.kumo.domain.User;
import is.rares.kumo.model.requests.RegisterRequest;
import is.rares.kumo.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {

    private final KumoConfig kumoConfig;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public UserService(KumoConfig kumoConfig,
                       PasswordEncoder passwordEncoder,
                       UserRepository userRepository) {
        this.kumoConfig = kumoConfig;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }


    public ResponseEntity<String> register(RegisterRequest request, @Nullable String registerInvite) {
        if (kumoConfig.isInviteBasedRegistration())
            throw new KumoException(AccountCodeErrorCodes.INVALID_INVITE, "Invite is invalid");

        if (userRepository.findByUsername(request.getUsername()).isPresent())
            throw new KumoException(AccountCodeErrorCodes.DUPLICATE_USERNAME);

        if (userRepository.findByEmail(request.getEmail()).isPresent())
            throw new KumoException(AccountCodeErrorCodes.DUPLICATE_EMAIL);


        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        this.userRepository.save(user);

        return new ResponseEntity<>("{}", HttpStatus.OK);
    }

    public User findByUserId(UUID userId) {
        return this.userRepository.findByUuid(userId)
                .orElseThrow(() -> new KumoException(AccountCodeErrorCodes.UNEXPECTED_ERROR));
    }
}
