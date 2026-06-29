package org.forgeiq.planning.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BreakdownDashboardResponseDto {
    private BreakdownOverviewDto summary;
    private BreakdownsDto table;
}
