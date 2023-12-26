package is.rares.kumo.service;

import is.rares.kumo.domain.user.AuthorityType;
import is.rares.kumo.domain.user.Feature;
import is.rares.kumo.domain.user.Authority;
import is.rares.kumo.repository.AuthorityRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthorityService {
    private final AuthorityRepository authorityRepository;

    public AuthorityService(AuthorityRepository authorityRepository) {
        this.authorityRepository = authorityRepository;
    }

    public Authority getOwnerAuthorityOrCreateIfNotExists() {
        var authorityOptional = authorityRepository.findByFeature(Feature.OWNER);
        if (authorityOptional.isPresent())
            return authorityOptional.get();

        Authority authority = new Authority();
        authority.setFeature(Feature.OWNER);
        authority.setType(AuthorityType.FEATURE);

        authorityRepository.save(authority);
        return authority;
    }

}
