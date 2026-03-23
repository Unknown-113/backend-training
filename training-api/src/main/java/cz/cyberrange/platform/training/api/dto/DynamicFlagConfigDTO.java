package cz.cyberrange.platform.training.api.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** DTO for dynamic flag configuration. */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "DynamicFlagConfigDTO", description = "Dynamic flag configuration.")
public class DynamicFlagConfigDTO {

  @ApiModelProperty(value = "Enable dynamic flag", example = "false")
  @JsonAlias({"enableDynamicFlag", "enable_dynamic_flag"})
  private boolean enableDynamicFlag;

  @ApiModelProperty(value = "Interval in minutes for flag changes", example = "5")
  @Min(value = 1, message = "Flag change interval must be at least 1 minute")
  @JsonAlias({"flagChangeInterval", "flag_change_interval"})
  private Integer flagChangeInterval;

  @ApiModelProperty(value = "Initial secret for generating flags", example = "secret123")
  @JsonAlias({"initialSecret", "initial_secret"})
  private String initialSecret;

  @AssertTrue(message = "When dynamic flag is enabled, interval and secret must be set")
  @JsonIgnore
  public boolean isDynamicFlagConfigurationValid() {
    return !enableDynamicFlag
        || (flagChangeInterval != null
            && flagChangeInterval >= 1
            && initialSecret != null
            && !initialSecret.isBlank());
  }
}
