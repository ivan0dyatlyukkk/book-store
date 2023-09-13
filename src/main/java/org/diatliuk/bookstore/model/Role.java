package org.diatliuk.bookstore.model;

import jakarta.persistence.*;
import lombok.Data;
import org.diatliuk.bookstore.enums.RoleName;

@Data
@Entity
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private RoleName name;
}
