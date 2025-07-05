package ru.job4j.shortcut.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.job4j.shortcut.entity.LinkEntity;

import java.util.Optional;

@Repository
public interface LinkRepository extends JpaRepository<LinkEntity, Integer> {

    Optional<LinkEntity> findAllByOriginalUrl(String originalUrl);
}
