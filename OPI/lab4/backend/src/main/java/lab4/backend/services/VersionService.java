package lab4.backend.services;

import jakarta.ejb.Singleton;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Stateless
public class VersionService {
    @PersistenceContext
    private EntityManager em;

    @Transactional
    public String getCurrentVersion(String tableName) {
        try {
            String sql = "SELECT last_modified FROM table_meta WHERE table_name = ?";
            Timestamp time = (Timestamp) em.createNativeQuery(sql)
                    .setParameter(1, tableName)
                    .getSingleResult();

            return time != null ? time.toLocalDateTime().toString() : "Not Modified yet";

        } catch (Exception e) {
            return "Not Modified yet";
        }
    }
}