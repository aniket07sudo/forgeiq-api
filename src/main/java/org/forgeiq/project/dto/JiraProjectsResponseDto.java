package org.forgeiq.project.dto;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class JiraProjectsResponseDto {

    private String self;

    private String nextPage;

    private Integer maxResults;

    private Integer startAt;

    private Integer total;

    private Boolean isLast;

    private List<JiraProjectResponseDto> values;
}