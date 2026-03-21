package cz.cyberrange.platform.training.service.services;

import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * Service for generating dynamic flags based on time and secret.
 */
@Service
public class DynamicFlagService {

    /**
     * Generates a dynamic flag using HMAC-SHA256 with the initial secret and current time slot.
     *
     * @param initialSecret the initial secret string
     * @param intervalMinutes the interval in minutes for flag changes
     * @return the generated flag as a base64 encoded string
     */
    public String generateFlag(String initialSecret, int intervalMinutes) {
        long currentTime = System.currentTimeMillis();
        long intervalMillis = intervalMinutes * 60 * 1000L;
        long timeSlot = currentTime / intervalMillis;

        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKey = new SecretKeySpec(initialSecret.getBytes(), "HmacSHA256");
            mac.init(secretKey);
            byte[] hash = mac.doFinal(String.valueOf(timeSlot).getBytes());
            // Take first 16 characters of base64 for shorter flag
            return Base64.getEncoder().encodeToString(hash).substring(0, 16);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Error generating dynamic flag", e);
        }
    }
}