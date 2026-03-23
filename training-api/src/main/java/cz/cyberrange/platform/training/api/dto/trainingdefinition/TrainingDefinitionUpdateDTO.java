package cz.cyberrange.platform.training.api.dto.trainingdefinition;

import cz.cyberrange.platform.training.api.dto.betatestinggroup.BetaTestingGroupUpdateDTO;
import cz.cyberrange.platform.training.api.enums.TDState;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.Valid;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/** Encapsulates information about Training Definition, intended for edit of the definition. */
@Getter
@Setter
@ToString
@ApiModel(value = "TrainingDefinitionUpdateDTO", description = "Training definition to update.")
public class TrainingDefinitionUpdateDTO {

  @ApiModelProperty(
      value = "Main identifier of training definition.",
      required = true,
      example = "2")
  @NotNull(message = "{trainingDefinition.id.NotNull.message}")
  private Long id;

  @ApiModelProperty(
      value = "A name of the training/game (e.g., Photo Hunter) .",
      required = true,
      example = "TrainingDefinition2")
  @NotEmpty(message = "{trainingDefinition.title.NotEmpty.message}")
  private String title;

  @ApiModelProperty(
      value = "Description of training definition that is visible to the participant.",
      example = "Unreleased training definition")
  private String description;

  @ApiModelProperty(
      value = "List of knowledge and skills necessary to complete the training.",
      example = "[phishing]")
  private String[] prerequisites;

  @ApiModelProperty(
      value =
          "A list of knowledge and skills that the participant should learn by attending the training (if it is used for educational purposes) ",
      example = "")
  private String[] outcomes;

  @ApiModelProperty(
      value = "Current state of training definition.",
      required = true,
      example = "UNRELEASED")
  @NotNull(message = "{trainingDefinition.state.NotNull.message}")
  private TDState state;

  @ApiModelProperty(
      value = "Group of organizers who is allowed to see the training definition.",
      required = true)
  @Valid
  private BetaTestingGroupUpdateDTO betaTestingGroup;

  @ApiModelProperty(
      value = "Sign if stepper bar should be displayed.",
      required = true,
      example = "false")
  @NotNull(message = "{trainingDefinition.showStepperBar.NotNull.message}")
  private boolean showStepperBar;

  @ApiModelProperty(value = "Enable dynamic flag for training definition.", example = "false")
  private boolean enableDynamicFlag = false;

  @ApiModelProperty(value = "Interval in minutes for flag changes.", example = "5")
  private Integer flagChangeInterval;

  @ApiModelProperty(value = "Initial secret for generating dynamic flags.", example = "secret123")
  private String initialSecret;

  @AssertTrue(message = "When dynamic flag is enabled, interval and secret must be set")
  public boolean isDynamicFlagConfigurationValid() {
    return !enableDynamicFlag
        || (flagChangeInterval != null
            && flagChangeInterval >= 1
            && initialSecret != null
            && !initialSecret.isBlank());
  }
}
