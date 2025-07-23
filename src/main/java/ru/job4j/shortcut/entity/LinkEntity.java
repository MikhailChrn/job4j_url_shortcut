package ru.job4j.shortcut.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = {"originalKey"})
@Table (name = "links")
public class LinkEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank
    @Column(name = "orig_url", unique = true)
    private String originalUrl;

    @Column(name = "code", unique = true)
    private String code;

    @Column(name = "total")
    private Integer total;

    @ManyToOne
    @JoinColumn(name = "site_id")
    private SiteEntity site;
}
