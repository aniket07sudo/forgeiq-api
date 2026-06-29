package org.forgeiq.planning.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@RequiredArgsConstructor
public class BreakdownRequestDto {
    @NotBlank(message = "Description is required")
    public String description;
    @NotBlank(message = "title is required")
    public String title;
    public String additionalContext;
    public Long projectId;
}
