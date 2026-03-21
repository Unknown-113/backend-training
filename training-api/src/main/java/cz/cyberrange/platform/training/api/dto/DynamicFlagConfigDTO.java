package cz.cyberrange.platform.training.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO for dynamic flag configuration.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "DynamicFlagConfigDTO", description = "Dynamic flag configuration.")
public class DynamicFlagConfigDTO {

    @ApiModelProperty(value = "Enable dynamic flag", example = "false")
    private boolean enableDynamicFlag;

    @ApiModelProperty(value = "Interval in minutes for flag changes", example = "5")
    private Integer flagChangeInterval;

    @ApiModelProperty(value = "Initial secret for generating flags", example = "secret123")
    private String initialSecret;
}