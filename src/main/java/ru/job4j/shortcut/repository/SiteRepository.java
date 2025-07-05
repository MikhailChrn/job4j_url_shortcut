package ru.job4j.shortcut.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.job4j.shortcut.entity.SiteEntity;

@Repository
public interface SiteRepository extends JpaRepository<SiteEntity, Integer> {

}
