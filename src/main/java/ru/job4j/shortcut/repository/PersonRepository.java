package ru.job4j.shortcut.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.job4j.shortcut.entity.PersonEntity;

import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<PersonEntity, Integer> {

    Optional<PersonEntity> findByUsername(String username);

    Boolean existsByUsername(String username);

}
