package org.forgeiq.planning.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BreakdownOverviewDto {
    private String breakdownUrl;

    private long totalBreakdown;

    private long drafts;

    private long generated;

    private long jiraSynced;

    private long outOfSync;
}
