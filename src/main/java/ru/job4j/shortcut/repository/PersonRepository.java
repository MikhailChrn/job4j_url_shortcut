package ru.job4j.shortcut.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.job4j.shortcut.entity.ERole;
import ru.job4j.shortcut.entity.PersonEntity;

import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<PersonEntity, Integer> {

    Optional<PersonEntity> findByUsername(String username);

    boolean existsByUsername(String username);

    /**
     * Важно не путать 'SELECT 1' с 'COUNT(1)'.
     * 'COUNT(1)' возвращает количество строк в таблице,
     * а 'SELECT 1' возвращает значение '1' для каждой строки
     */
    @Query(value = """
                SELECT EXISTS (
                    SELECT 1
                    FROM persons p
                    JOIN persons_roles pr ON p.id = pr.person_id
                    JOIN roles r ON r.id = pr.role_id
                    WHERE p.username = :username
                      AND r.title = :roleTitle
                )
            """, nativeQuery = true)
    boolean hasRole(@Param("username") String username,
                    @Param("roleTitle") String roleTitle);
}
