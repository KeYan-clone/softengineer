package org.example.experiment.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.experiment.model.StepType;

/**
 * Data Transfer Object for creating a new experiment step
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExperimentStepCreateDTO {
    @NotBlank(message = "Step name is required")
    @Size(min = 1, max = 255, message = "Step name must be between 1 and 255 characters")
    private String name;
    
    private String description;
    
    @NotNull(message = "Step sequence is required")
    @Min(value = 1, message = "Step sequence must be at least 1")
    private Integer sequence;
    
    @NotNull(message = "Step type is required")
    private StepType stepType;
    
    private String configuration;
}
