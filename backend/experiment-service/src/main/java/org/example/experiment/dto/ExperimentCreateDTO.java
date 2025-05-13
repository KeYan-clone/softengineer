package org.example.experiment.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Data Transfer Object for creating a new experiment
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExperimentCreateDTO {
    @NotBlank(message = "Experiment name is required")
    @Size(min = 3, max = 255, message = "Experiment name must be between 3 and 255 characters")
    private String name;
    
    @Size(max = 2000, message = "Description cannot exceed 2000 characters")
    private String description;
    
    @Valid
    @NotEmpty(message = "At least one step is required")
    private List<ExperimentStepCreateDTO> steps = new ArrayList<>();
}
