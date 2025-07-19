package ru.job4j.shortcut.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.job4j.shortcut.dto.response.inner.SiteDto;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatisticResponseDto {

    private List<SiteDto> sites;

}
