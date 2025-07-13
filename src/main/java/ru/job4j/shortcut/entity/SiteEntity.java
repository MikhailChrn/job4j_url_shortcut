package ru.job4j.shortcut.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = {"domainName"})
@Table (name = "sites")
public class SiteEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank
    @Column(name = "domain_name", unique = true)
    private String domainName;

    @Column(name = "total")
    private Integer total;

    @ManyToOne
    @JoinColumn(name = "person_id")
    private PersonEntity person;

}
