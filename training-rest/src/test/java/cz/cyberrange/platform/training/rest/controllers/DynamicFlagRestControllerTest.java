package cz.cyberrange.platform.training.rest.controllers;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.cyberrange.platform.training.api.dto.DynamicFlagConfigDTO;
import cz.cyberrange.platform.training.persistence.model.TrainingDefinition;
import cz.cyberrange.platform.training.service.services.DynamicFlagService;
import cz.cyberrange.platform.training.service.services.TrainingDefinitionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

public class DynamicFlagRestControllerTest {

  private MockMvc mockMvc;

  @Mock private TrainingDefinitionService trainingDefinitionService;

  @Mock private DynamicFlagService dynamicFlagService;

  private ObjectMapper objectMapper = new ObjectMapper();

  @BeforeEach
  public void setup() {
    MockitoAnnotations.openMocks(this);
    DynamicFlagRestController controller =
        new DynamicFlagRestController(trainingDefinitionService, dynamicFlagService);
    mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
  }

  @Test
  public void getConfig_shouldReturnConfig() throws Exception {
    TrainingDefinition td = new TrainingDefinition();
    td.setId(1L);
    td.setEnableDynamicFlag(true);
    td.setFlagChangeInterval(5);
    td.setInitialSecret("secret");

    when(trainingDefinitionService.findById(1L)).thenReturn(td);

    mockMvc
        .perform(get("/dynamic-flags/1"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON));
  }

  @Test
  public void updateConfig_shouldUpdateConfig() throws Exception {
    TrainingDefinition td = new TrainingDefinition();
    td.setId(1L);

    when(trainingDefinitionService.findById(1L)).thenReturn(td);

    DynamicFlagConfigDTO config = new DynamicFlagConfigDTO(true, 10, "newSecret");

    mockMvc
        .perform(
            put("/dynamic-flags/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(config)))
        .andExpect(status().isOk());
  }

  @Test
  public void updateConfig_shouldAcceptCamelCasePayload() throws Exception {
    TrainingDefinition td = new TrainingDefinition();
    td.setId(1L);

    when(trainingDefinitionService.findById(1L)).thenReturn(td);

    mockMvc
        .perform(
            put("/dynamic-flags/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    "{\"enableDynamicFlag\":true,\"flagChangeInterval\":10,\"initialSecret\":\"newSecret\"}"))
        .andExpect(status().isOk());
  }

  @Test
  public void getCurrentFlag_shouldReturnFlag() throws Exception {
    TrainingDefinition td = new TrainingDefinition();
    td.setId(1L);
    td.setEnableDynamicFlag(true);
    td.setFlagChangeInterval(5);
    td.setInitialSecret("secret");

    when(trainingDefinitionService.findById(1L)).thenReturn(td);
    when(dynamicFlagService.generateFlag("secret", 5)).thenReturn("generatedFlag");

    mockMvc
        .perform(get("/dynamic-flags/1/current-flag"))
        .andExpect(status().isOk())
        .andExpect(content().string("generatedFlag"));
  }

  @Test
  public void getCurrentFlag_whenNotEnabled_shouldReturnBadRequest() throws Exception {
    TrainingDefinition td = new TrainingDefinition();
    td.setId(1L);
    td.setEnableDynamicFlag(false);

    when(trainingDefinitionService.findById(1L)).thenReturn(td);

    mockMvc
        .perform(get("/dynamic-flags/1/current-flag"))
        .andExpect(status().isBadRequest())
        .andExpect(content().string("Dynamic flag not enabled"));
  }
}
