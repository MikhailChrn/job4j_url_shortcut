package ru.job4j.shortcut.mapper;

import org.springframework.stereotype.Component;
import ru.job4j.shortcut.dto.response.inner.SiteDto;
import ru.job4j.shortcut.entity.SiteEntity;

/**
 * DTO for {@link SiteEntity}
 */
@Component
public class SiteMapper {

    /**
     * SiteDto используется для сбора статистики
     */
    public SiteDto getSiteDtoFromEntity(SiteEntity entity) {

        return new SiteDto(entity.getDomainName(), entity.getTotal());
    }

}
