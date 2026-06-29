package org.forgeiq.planning.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class BreakdownsDto {

    private List<BreakdownListItemDto> items;

    private int page;

    private int pageSize;

    private int totalElements;

    private int totalPages;
}
