package cz.cyberrange.platform.training.service.services;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.stereotype.Service;

/** Service for generating dynamic flags based on time and secret. */
@Service
public class DynamicFlagService {

  /**
   * Generates a dynamic flag using HMAC-SHA256 with the initial secret and current time slot.
   *
   * @param initialSecret the initial secret string
   * @param intervalMinutes the interval in minutes for flag changes
   * @return the generated flag in FLAG{...} format
   */
  public String generateFlag(String initialSecret, int intervalMinutes) {
    if (initialSecret == null || initialSecret.isBlank()) {
      throw new IllegalArgumentException("Initial secret must not be blank");
    }
    if (intervalMinutes < 1) {
      throw new IllegalArgumentException("Flag change interval must be at least 1 minute");
    }
    long currentTime = System.currentTimeMillis();
    long intervalMillis = intervalMinutes * 60 * 1000L;
    long timeSlot = currentTime / intervalMillis;

    try {
      Mac mac = Mac.getInstance("HmacSHA256");
      SecretKeySpec secretKey = new SecretKeySpec(initialSecret.getBytes(), "HmacSHA256");
      mac.init(secretKey);
      byte[] hash = mac.doFinal(String.valueOf(timeSlot).getBytes());
      // Use the first 16 hex chars for a stable, flag-friendly token.
      return "FLAG{" + toHex(hash).substring(0, 16) + "}";
    } catch (NoSuchAlgorithmException | InvalidKeyException e) {
      throw new RuntimeException("Error generating dynamic flag", e);
    }
  }

  private String toHex(byte[] bytes) {
    StringBuilder builder = new StringBuilder(bytes.length * 2);
    for (byte b : bytes) {
      builder.append(String.format("%02x", b));
    }
    return builder.toString();
  }
}
