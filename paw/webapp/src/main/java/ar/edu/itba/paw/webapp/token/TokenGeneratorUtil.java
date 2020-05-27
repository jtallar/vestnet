package ar.edu.itba.paw.webapp.token;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.time.Instant;

public class TokenGeneratorUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(TokenGeneratorUtil.class);

    // Set TimeStep to 5 minutes
    private static final Duration TIME_STEP = Duration.ofMinutes(5);
    // Set Key encription algorithm to AES
    private static final String KEY_ALGORITHM = "AES";
    // Set number of steps to check from current time
    private static final int STEP_DISTANCE = 1;

    public static int getToken(String charKey) throws NoSuchAlgorithmException, InvalidKeyException {
        final TimeBasedOneTimePasswordGenerator totp = new TimeBasedOneTimePasswordGenerator(TIME_STEP);
        SecretKey key = new SecretKeySpec(charKey.getBytes(), 0, charKey.length(), KEY_ALGORITHM);
        return totp.generateOneTimePassword(key, Instant.now());
    }

    public static boolean checkToken(String charKey, int token) throws NoSuchAlgorithmException, InvalidKeyException {
        final TimeBasedOneTimePasswordGenerator totp = new TimeBasedOneTimePasswordGenerator(TIME_STEP);
        SecretKey key = new SecretKeySpec(charKey.getBytes(), 0, charKey.length(), KEY_ALGORITHM);
        Instant from = Instant.now(), to = from;
        for (int i = 0; i < STEP_DISTANCE; i++) {
            from = from.minus(TIME_STEP);
            to = to.plus(TIME_STEP);
        }
        while (!from.isAfter(to)) {
            if (token == totp.generateOneTimePassword(key, from))
                return true;
            from = from.plus(TIME_STEP);
        }
        return false;
    }
}
