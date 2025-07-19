package ru.job4j.shortcut.dto.response.inner;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SiteDto {

    private String domainName;

    private Integer total;

}
