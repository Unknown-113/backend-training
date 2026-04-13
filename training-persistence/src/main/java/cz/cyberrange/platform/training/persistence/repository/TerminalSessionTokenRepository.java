package cz.cyberrange.platform.training.persistence.repository;

import cz.cyberrange.platform.training.persistence.model.TerminalSessionToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface TerminalSessionTokenRepository extends JpaRepository<TerminalSessionToken, Long> {

    @Query("SELECT t FROM TerminalSessionToken t WHERE t.token = :token AND t.used = false AND t.expiresAt > :now")
    Optional<TerminalSessionToken> findValidByToken(@Param("token") String token, @Param("now") LocalDateTime now);
}
