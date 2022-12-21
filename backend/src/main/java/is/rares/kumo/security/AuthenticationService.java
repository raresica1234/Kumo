package is.rares.kumo.security;

import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.slf4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import org.bouncycastle.openssl.PEMParser;

import javax.annotation.PostConstruct;
import java.io.*;
import java.security.PrivateKey;
import java.security.PublicKey;

@Service
@Slf4j
public class AuthenticationService {
    private PublicKey publicKey;
    private PrivateKey privateKey;

    @PostConstruct
    public void loadPrivateKey() {
        try {
            InputStream resource = new ClassPathResource("keystore/private-auth-key.pem").getInputStream();
            final Reader reader = new BufferedReader(new InputStreamReader(resource));

            PEMParser pemParser = new PEMParser(reader);
            JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
            final PEMKeyPair keyPair = (PEMKeyPair) pemParser.readObject();

            PrivateKeyInfo privateKeyInfo = PrivateKeyInfo.getInstance(keyPair.getPrivateKeyInfo());
            this.privateKey = converter.getPrivateKey(privateKeyInfo);
        } catch (IOException e) {
            log.warn("Exception: {}", e.getMessage());
            log.error("Failed: ", e);
        }
    }

    @PostConstruct
    public void loadPublicKey() {
        try {
            InputStream resource = new ClassPathResource("keystore/public-auth-key.pem").getInputStream();
            final Reader reader = new BufferedReader(new InputStreamReader(resource));

            PEMParser pemParser = new PEMParser(reader);
            JcaPEMKeyConverter converter = new JcaPEMKeyConverter();

            final SubjectPublicKeyInfo publicKeyInfo = SubjectPublicKeyInfo.getInstance(pemParser.readObject());
            this.publicKey = converter.getPublicKey(publicKeyInfo);
        } catch (IOException e) {
            log.warn("Exception: {}", e.getMessage());
            log.error("Failed: ", e);
        }
    }
}
