package cz.cyberrange.platform.training.persistence.model;

import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/** Stores access attempts and ban state for a participant within a training definition. */
@Entity
@Table(
    name = "training_access_restriction",
    uniqueConstraints =
        @UniqueConstraint(columnNames = {"participant_ref_id", "training_definition_id"}))
public class TrainingAccessRestriction extends AbstractEntity<Long> {

  @Column(name = "participant_ref_id", nullable = false)
  private Long participantRefId;

  @Column(name = "training_definition_id", nullable = false)
  private Long trainingDefinitionId;

  @Column(name = "access_count", nullable = false)
  private int accessCount;

  @Column(name = "banned", nullable = false)
  private boolean banned;

  @Column(name = "last_accessed_at", nullable = false)
  private LocalDateTime lastAccessedAt;

  @Column(name = "banned_at")
  private LocalDateTime bannedAt;

  public TrainingAccessRestriction() {}

  public TrainingAccessRestriction(
      Long participantRefId,
      Long trainingDefinitionId,
      int accessCount,
      boolean banned,
      LocalDateTime lastAccessedAt,
      LocalDateTime bannedAt) {
    this.participantRefId = participantRefId;
    this.trainingDefinitionId = trainingDefinitionId;
    this.accessCount = accessCount;
    this.banned = banned;
    this.lastAccessedAt = lastAccessedAt;
    this.bannedAt = bannedAt;
  }

  public Long getParticipantRefId() {
    return participantRefId;
  }

  public void setParticipantRefId(Long participantRefId) {
    this.participantRefId = participantRefId;
  }

  public Long getTrainingDefinitionId() {
    return trainingDefinitionId;
  }

  public void setTrainingDefinitionId(Long trainingDefinitionId) {
    this.trainingDefinitionId = trainingDefinitionId;
  }

  public int getAccessCount() {
    return accessCount;
  }

  public void setAccessCount(int accessCount) {
    this.accessCount = accessCount;
  }

  public boolean isBanned() {
    return banned;
  }

  public void setBanned(boolean banned) {
    this.banned = banned;
  }

  public LocalDateTime getLastAccessedAt() {
    return lastAccessedAt;
  }

  public void setLastAccessedAt(LocalDateTime lastAccessedAt) {
    this.lastAccessedAt = lastAccessedAt;
  }

  public LocalDateTime getBannedAt() {
    return bannedAt;
  }

  public void setBannedAt(LocalDateTime bannedAt) {
    this.bannedAt = bannedAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof TrainingAccessRestriction)) return false;
    TrainingAccessRestriction that = (TrainingAccessRestriction) o;
    return Objects.equals(getParticipantRefId(), that.getParticipantRefId())
        && Objects.equals(getTrainingDefinitionId(), that.getTrainingDefinitionId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getParticipantRefId(), getTrainingDefinitionId());
  }
}
