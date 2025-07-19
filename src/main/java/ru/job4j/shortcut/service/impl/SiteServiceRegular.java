package ru.job4j.shortcut.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.shortcut.dto.request.UrlRequestDTO;
import ru.job4j.shortcut.dto.response.ConvertLinkResponseDTO;
import ru.job4j.shortcut.dto.response.RegisterSiteResponseDTO;
import ru.job4j.shortcut.dto.response.StatisticResponseDto;
import ru.job4j.shortcut.service.SiteService;

@Service
@AllArgsConstructor
public class SiteServiceRegular implements SiteService {

    @Override
    public RegisterSiteResponseDTO registerSite(UrlRequestDTO urlRequestDTO) {
        return null;
    }

    @Override
    public ConvertLinkResponseDTO convertLink(UrlRequestDTO urlRequestDTO) {
        return null;
    }

    @Override
    public String getOriginalLink(String code) {
        return "";
    }

    @Override
    public StatisticResponseDto getStatisticByUsername(String username) {
        return null;
    }

    @Override
    public StatisticResponseDto getAllStatistic() {
        return null;
    }
}
