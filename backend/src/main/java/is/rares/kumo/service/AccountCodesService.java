package is.rares.kumo.service;

import is.rares.kumo.core.exceptions.KumoException;
import is.rares.kumo.core.exceptions.codes.AccountCodeErrorCodes;
import is.rares.kumo.domain.AccountCode;
import is.rares.kumo.domain.User;
import is.rares.kumo.domain.enums.AccountCodeStatus;
import is.rares.kumo.domain.enums.AccountCodeType;
import is.rares.kumo.controller.requests.AccountCodeRequest;
import is.rares.kumo.repository.AccountCodeRepository;
import is.rares.kumo.security.domain.CurrentUser;
import is.rares.kumo.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class AccountCodesService {

    private static final int TWO_FACTOR_CODE_LENGTH = 6;
    private static final int TWO_FACTOR_CODE_EXPIRATION_TIME_MIN = 5;

    private final AccountCodeRepository accountCodeRepository;

    private final PasswordEncoder passwordEncoder;

    public AccountCodesService(AccountCodeRepository accountCodeRepository, PasswordEncoder passwordEncoder) {
        this.accountCodeRepository = accountCodeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void generateTwoFactorCode(User user) {
        String code = StringUtils.generateRandomString(TWO_FACTOR_CODE_LENGTH);
        LocalDateTime expirationDate = LocalDateTime.now().plusMinutes(TWO_FACTOR_CODE_EXPIRATION_TIME_MIN);

        log.error("2FA Code: {}", code);

        AccountCode accountCode = new AccountCode();
        accountCode.setCode(passwordEncoder.encode(code));
        accountCode.setCodeType(AccountCodeType.TWO_FACTOR_CODE);
        accountCode.setCodeStatus(AccountCodeStatus.UNUSED);
        accountCode.setExpirationDate(expirationDate);
        accountCode.setUser(user);

        accountCodeRepository.save(accountCode);
    }

    @Transactional
    public void validateAccountCode(AccountCodeRequest request, CurrentUser currentUser) {
        List<AccountCode> accountCodeList = accountCodeRepository.findAllByUserId(currentUser.getId());

        AccountCode accountCode = findAccountCode(accountCodeList, request.getCode());
        accountCodeRepository.updateAccountCodeStatus(accountCode.getUuid(), AccountCodeStatus.USED);
    }

    private AccountCode findAccountCode(List<AccountCode> accountCodeList, String validationCode) {
        AccountCode accountCode = accountCodeList.stream()
                .filter(code -> passwordEncoder.matches(validationCode, code.getCode()))
                .findFirst()
                .orElse(null);

        if (accountCode == null)
            throw new KumoException(AccountCodeErrorCodes.ACCOUNT_CODE_NOT_FOUND);

        if (accountCode.getExpirationDate().isBefore(LocalDateTime.now()))
            throw new KumoException(AccountCodeErrorCodes.ACCOUNT_CODE_EXPIRED);

        if (accountCode.getCodeStatus() == AccountCodeStatus.USED)
            throw new KumoException(AccountCodeErrorCodes.ACCOUNT_CODE_USED);

        return accountCode;

    }
}
