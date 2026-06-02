package lab4.backend.data.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "table_meta")
@Data
@NoArgsConstructor
public class TableMetaEntity {
    @Id
    @Column(name="table_name")
    private String tableName;
    @Column(name = "last_modified")
    private LocalDateTime lastModified;
}
