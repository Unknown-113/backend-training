package cz.cyberrange.platform.training.api.dto.run;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDateTime;

@ApiModel(description = "Deep Link URL for terminal-based exam access")
public class DeepLinkDTO {

    @ApiModelProperty(value = "The deep link URL containing a one-time session token", required = true)
    @JsonProperty("deep_link_url")
    private String deepLinkUrl;

    @ApiModelProperty(value = "ID of the training run this link belongs to", required = true)
    @JsonProperty("training_run_id")
    private Long trainingRunId;

    @ApiModelProperty(value = "Expiration timestamp of the session token", required = true)
    @JsonProperty("expires_at")
    private LocalDateTime expiresAt;

    public DeepLinkDTO() {}

    public DeepLinkDTO(String deepLinkUrl, Long trainingRunId, LocalDateTime expiresAt) {
        this.deepLinkUrl = deepLinkUrl;
        this.trainingRunId = trainingRunId;
        this.expiresAt = expiresAt;
    }

    public String getDeepLinkUrl() { return deepLinkUrl; }
    public void setDeepLinkUrl(String deepLinkUrl) { this.deepLinkUrl = deepLinkUrl; }

    public Long getTrainingRunId() { return trainingRunId; }
    public void setTrainingRunId(Long trainingRunId) { this.trainingRunId = trainingRunId; }

    public LocalDateTime getExpiresAt() { return expiresAt; }
    public void setExpiresAt(LocalDateTime expiresAt) { this.expiresAt = expiresAt; }
}
