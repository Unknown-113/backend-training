package cz.cyberrange.platform.training.persistence.repository;

import cz.cyberrange.platform.training.persistence.model.TrainingAccessRestriction;
import java.util.Optional;
import javax.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/** Repository for access attempt counters and bans on training definitions. */
@Repository
public interface TrainingAccessRestrictionRepository
    extends JpaRepository<TrainingAccessRestriction, Long> {

  Optional<TrainingAccessRestriction> findByParticipantRefIdAndTrainingDefinitionId(
      Long participantRefId, Long trainingDefinitionId);

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query(
      "SELECT tar FROM TrainingAccessRestriction tar "
          + "WHERE tar.participantRefId = :participantRefId "
          + "AND tar.trainingDefinitionId = :trainingDefinitionId")
  Optional<TrainingAccessRestriction> findByParticipantRefIdAndTrainingDefinitionIdForUpdate(
      @Param("participantRefId") Long participantRefId,
      @Param("trainingDefinitionId") Long trainingDefinitionId);
}
