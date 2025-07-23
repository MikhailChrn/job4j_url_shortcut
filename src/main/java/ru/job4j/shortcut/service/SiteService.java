package ru.job4j.shortcut.service;

import ru.job4j.shortcut.dto.request.UrlRequestDTO;
import ru.job4j.shortcut.dto.response.ConvertLinkResponseDTO;
import ru.job4j.shortcut.dto.response.RegisterSiteResponseDTO;
import ru.job4j.shortcut.dto.response.StatisticResponseDto;

public interface SiteService {

    RegisterSiteResponseDTO registerSite(UrlRequestDTO urlRequestDTO, String username);

    ConvertLinkResponseDTO convertLink(UrlRequestDTO urlRequestDTO, String username);

    String getOriginalLink(String code);

    StatisticResponseDto getStatisticByUsername(String username);

    StatisticResponseDto getAllStatistic();

}
