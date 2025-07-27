package ru.job4j.shortcut.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.job4j.shortcut.entity.SiteEntity;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface SiteRepository extends JpaRepository<SiteEntity, Integer> {

    Optional<SiteEntity> findByDomainName(String domainName);

    boolean existsByDomainNameAndPersonId(String domainName, Integer id);

    Collection<SiteEntity> findAllByPersonId(Integer id);

}
