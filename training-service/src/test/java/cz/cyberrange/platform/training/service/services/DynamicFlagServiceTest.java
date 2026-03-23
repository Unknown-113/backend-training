package cz.cyberrange.platform.training.service.services;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class DynamicFlagServiceTest {

  private final DynamicFlagService dynamicFlagService = new DynamicFlagService();

  @Test
  public void generateFlag_shouldReturnConsistentFlagForSameTimeSlot() {
    String secret = "testSecret";
    int interval = 5;

    // Generate flags multiple times quickly (same time slot)
    String flag1 = dynamicFlagService.generateFlag(secret, interval);
    String flag2 = dynamicFlagService.generateFlag(secret, interval);

    assertEquals(flag1, flag2, "Flags should be consistent within the same time slot");
    assertNotNull(flag1);
    assertTrue(flag1.startsWith("FLAG{"), "Flag should start with FLAG{");
    assertTrue(flag1.endsWith("}"), "Flag should end with }");
    assertEquals(22, flag1.length(), "Flag should be 22 characters long");
  }

  @Test
  public void generateFlag_shouldReturnDifferentFlagsForDifferentSecrets() {
    String secret1 = "secret1";
    String secret2 = "secret2";
    int interval = 5;

    String flag1 = dynamicFlagService.generateFlag(secret1, interval);
    String flag2 = dynamicFlagService.generateFlag(secret2, interval);

    assertNotEquals(flag1, flag2, "Flags should be different for different secrets");
  }

  @Test
  public void generateFlag_shouldHandleEdgeCases() {
    assertThrows(IllegalArgumentException.class, () -> dynamicFlagService.generateFlag("", 5));
    assertThrows(IllegalArgumentException.class, () -> dynamicFlagService.generateFlag(" ", 5));
    assertThrows(IllegalArgumentException.class, () -> dynamicFlagService.generateFlag(null, 5));
    assertThrows(
        IllegalArgumentException.class, () -> dynamicFlagService.generateFlag("secret", 0));
  }
}
