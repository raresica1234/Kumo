package is.rares.kumo.service;

import is.rares.kumo.controller.requests.user.RegisterRequest;
import is.rares.kumo.controller.responses.BooleanResponse;
import is.rares.kumo.convertor.UserConvertor;
import is.rares.kumo.core.config.KumoConfig;
import is.rares.kumo.core.exceptions.KumoException;
import is.rares.kumo.core.exceptions.codes.AccountCodeErrorCodes;
import is.rares.kumo.core.exceptions.codes.AuthorizationErrorCodes;
import is.rares.kumo.domain.user.AccountDetails;
import is.rares.kumo.security.enums.Feature;
import is.rares.kumo.domain.user.Role;
import is.rares.kumo.domain.user.User;
import is.rares.kumo.model.UserModel;
import is.rares.kumo.repository.user.RoleRepository;
import is.rares.kumo.repository.user.UserRepository;
import is.rares.kumo.security.domain.CurrentUser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    private final KumoConfig kumoConfig;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserConvertor userConvertor;

    private final RoleService roleService;
    private final RoleRepository roleRepository;

    private final RegisterInviteService registerInviteService;

    public UserService(KumoConfig kumoConfig,
                       PasswordEncoder passwordEncoder,
                       UserRepository userRepository,
                       UserConvertor userConvertor,
                       RoleService roleService,
                       RoleRepository roleRepository,
                       RegisterInviteService registerInviteService) {
        this.kumoConfig = kumoConfig;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.userConvertor = userConvertor;
        this.roleService = roleService;
        this.roleRepository = roleRepository;
        this.registerInviteService = registerInviteService;
    }


    public ResponseEntity<String> register(RegisterRequest request, @Nullable String registerInvite) {
        if (kumoConfig.isInviteBasedRegistration() && !registerInviteService.validateInvite(registerInvite))
            throw new KumoException(AuthorizationErrorCodes.INVALID_REGISTER_INVITE);

        if (userRepository.findByUsername(request.getUsername()).isPresent())
            throw new KumoException(AccountCodeErrorCodes.DUPLICATE_USERNAME);

        if (userRepository.findByEmail(request.getEmail()).isPresent())
            throw new KumoException(AccountCodeErrorCodes.DUPLICATE_EMAIL);


        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setAccountDetails(new AccountDetails());

        user = this.userRepository.save(user);

        if (kumoConfig.getOwnerUsername().equals(request.getUsername())) {
            Role ownerRole = roleService.getOwnerRoleOrCreateIfNotExists();
            ownerRole.getUsers().add(user);
            roleRepository.save(ownerRole);
        }

        if (kumoConfig.isInviteBasedRegistration()) {
            registerInviteService.incrementInviteUsage(registerInvite);
        }

        return new ResponseEntity<>("{}", HttpStatus.OK);
    }

    public User findByUserId(UUID userId) {
        return this.userRepository.findByUuid(userId)
                .orElseThrow(() -> new KumoException(AccountCodeErrorCodes.UNEXPECTED_ERROR));
    }

    public UserModel getUser(CurrentUser currentUser) {
        User user = findByUserId(currentUser.getId());

        return userConvertor.mapEntityToModel(user);
    }

    public BooleanResponse registerInviteRequired() {
        return new BooleanResponse(kumoConfig.isInviteBasedRegistration());
    }

    public List<Feature> getFeatures(CurrentUser currentUser) {
        User user = findByUserId(currentUser.getId());

        return user.getRoles().stream()
                .flatMap(role -> role.getFeatures().stream())
                .toList();
    }
}
