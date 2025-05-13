package org.example.experiment.service;

import org.example.experiment.dto.ExperimentCreateDTO;
import org.example.experiment.dto.ExperimentDTO;
import org.example.experiment.dto.ExperimentStepCreateDTO;
import org.example.experiment.model.ExperimentStatus;
import org.example.experiment.model.StepType;
import org.example.experiment.service.impl.ExperimentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for ExperimentService
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ExperimentServiceIntegrationTest {

    @Autowired
    private ExperimentService experimentService;
    
    private ExperimentCreateDTO createDTO;
    private final String TEST_USER_ID = "test-user-1";
    
    @BeforeEach
    void setUp() {
        // Create test step DTOs
        ExperimentStepCreateDTO step1 = ExperimentStepCreateDTO.builder()
                .name("Input Data")
                .description("Step for data input")
                .sequence(1)
                .stepType(StepType.INPUT)
                .configuration("{\"source\": \"sample_data.csv\"}")
                .build();
        
        ExperimentStepCreateDTO step2 = ExperimentStepCreateDTO.builder()
                .name("Process Data")
                .description("Step for data processing")
                .sequence(2)
                .stepType(StepType.PROCESSING)
                .configuration("{\"algorithm\": \"kmeans\", \"clusters\": 3}")
                .build();
        
        ExperimentStepCreateDTO step3 = ExperimentStepCreateDTO.builder()
                .name("Visualize Results")
                .description("Step for result visualization")
                .sequence(3)
                .stepType(StepType.VISUALIZATION)
                .configuration("{\"chartType\": \"scatter\"}")
                .build();
        
        // Create test experiment DTO
        createDTO = ExperimentCreateDTO.builder()
                .name("Test Experiment")
                .description("Test experiment description")
                .steps(Arrays.asList(step1, step2, step3))
                .build();
    }
    
    @Test
    void testCreateAndGetExperiment() {
        // Create experiment
        ExperimentDTO createdExperiment = experimentService.createExperiment(createDTO, TEST_USER_ID);
        
        // Verify creation
        assertNotNull(createdExperiment);
        assertNotNull(createdExperiment.getId());
        assertEquals("Test Experiment", createdExperiment.getName());
        assertEquals("Test experiment description", createdExperiment.getDescription());
        assertEquals(TEST_USER_ID, createdExperiment.getUserId());
        assertEquals(ExperimentStatus.DRAFT, createdExperiment.getStatus());
        assertEquals(3, createdExperiment.getSteps().size());
        
        // Get experiment by ID
        Optional<ExperimentDTO> retrievedExperiment = experimentService.getExperimentById(createdExperiment.getId());
        
        // Verify retrieval
        assertTrue(retrievedExperiment.isPresent());
        assertEquals(createdExperiment.getId(), retrievedExperiment.get().getId());
        assertEquals(3, retrievedExperiment.get().getSteps().size());
    }
    
    @Test
    void testUpdateExperiment() {
        // Create experiment
        ExperimentDTO createdExperiment = experimentService.createExperiment(createDTO, TEST_USER_ID);
        
        // Update experiment
        ExperimentCreateDTO updateDTO = ExperimentCreateDTO.builder()
                .name("Updated Experiment")
                .description("Updated description")
                .steps(List.of(
                        ExperimentStepCreateDTO.builder()
                                .name("New Step")
                                .sequence(1)
                                .stepType(StepType.PROCESSING)
                                .build()
                ))
                .build();
        
        ExperimentDTO updatedExperiment = experimentService.updateExperiment(createdExperiment.getId(), updateDTO);
        
        // Verify update
        assertEquals("Updated Experiment", updatedExperiment.getName());
        assertEquals("Updated description", updatedExperiment.getDescription());
        assertEquals(1, updatedExperiment.getSteps().size());
        assertEquals("New Step", updatedExperiment.getSteps().get(0).getName());
    }
    
    @Test
    void testShareExperiment() {
        // Create experiment
        ExperimentDTO createdExperiment = experimentService.createExperiment(createDTO, TEST_USER_ID);
        
        // Share experiment
        ExperimentDTO sharedExperiment = experimentService.shareExperiment(createdExperiment.getId());
        
        // Verify status change
        assertEquals(ExperimentStatus.PUBLISHED, sharedExperiment.getStatus());
    }
    
    @Test
    void testDeleteExperiment() {
        // Create experiment
        ExperimentDTO createdExperiment = experimentService.createExperiment(createDTO, TEST_USER_ID);
        
        // Delete experiment
        boolean deleted = experimentService.deleteExperiment(createdExperiment.getId());
        
        // Verify deletion
        assertTrue(deleted);
        
        // Verify experiment no longer exists
        Optional<ExperimentDTO> retrievedExperiment = experimentService.getExperimentById(createdExperiment.getId());
        assertFalse(retrievedExperiment.isPresent());
    }
}
