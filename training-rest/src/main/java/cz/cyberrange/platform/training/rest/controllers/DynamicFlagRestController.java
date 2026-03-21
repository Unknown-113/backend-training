package cz.cyberrange.platform.training.rest.controllers;

import cz.cyberrange.platform.training.api.dto.DynamicFlagConfigDTO;
import cz.cyberrange.platform.training.persistence.model.TrainingDefinition;
import cz.cyberrange.platform.training.service.services.DynamicFlagService;
import cz.cyberrange.platform.training.service.services.TrainingDefinitionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing dynamic flags in training definitions.
 */
@Api(value = "/dynamic-flags",
     tags = "Dynamic Flags",
     consumes = MediaType.APPLICATION_JSON_VALUE,
     authorizations = @Authorization(value = "bearerAuth"))
@ApiResponses(value = {
        @ApiResponse(code = 401, message = "Full authentication is required to access this resource.", response = ApiError.class),
        @ApiResponse(code = 403, message = "The necessary permissions are required for a resource.", response = ApiError.class)
})
@RestController
@RequestMapping(path = "/dynamic-flags", produces = MediaType.APPLICATION_JSON_VALUE)
public class DynamicFlagRestController {

    private final TrainingDefinitionService trainingDefinitionService;
    private final DynamicFlagService dynamicFlagService;

    @Autowired
    public DynamicFlagRestController(TrainingDefinitionService trainingDefinitionService,
                                     DynamicFlagService dynamicFlagService) {
        this.trainingDefinitionService = trainingDefinitionService;
        this.dynamicFlagService = dynamicFlagService;
    }

    /**
     * Get dynamic flag configuration for a training definition.
     *
     * @param id the training definition ID
     * @return the dynamic flag configuration
     */
    @ApiOperation(httpMethod = "GET",
            value = "Get Dynamic Flag Configuration",
            response = DynamicFlagConfigDTO.class,
            nickname = "getDynamicFlagConfig",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The configuration has been retrieved.", response = DynamicFlagConfigDTO.class),
            @ApiResponse(code = 404, message = "The training definition has not been found.", response = ApiError.class),
            @ApiResponse(code = 500, message = "Unexpected condition was encountered.", response = ApiError.class)
    })
    @GetMapping(path = "/{definitionId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DynamicFlagConfigDTO> getConfig(@ApiParam(value = "ID of training definition", required = true)
                                                           @PathVariable("definitionId") Long id) {
        TrainingDefinition td = trainingDefinitionService.findById(id);
        DynamicFlagConfigDTO config = new DynamicFlagConfigDTO(
                td.isEnableDynamicFlag(),
                td.getFlagChangeInterval(),
                td.getInitialSecret()
        );
        return ResponseEntity.ok(config);
    }

    /**
     * Update dynamic flag configuration for a training definition.
     *
     * @param id the training definition ID
     * @param config the new configuration
     * @return response entity
     */
    @ApiOperation(httpMethod = "PUT",
            value = "Update Dynamic Flag Configuration",
            nickname = "updateDynamicFlagConfig",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The configuration has been updated."),
            @ApiResponse(code = 400, message = "The provided configuration is not valid.", response = ApiError.class),
            @ApiResponse(code = 404, message = "The training definition has not been found.", response = ApiError.class),
            @ApiResponse(code = 500, message = "Unexpected condition was encountered.", response = ApiError.class)
    })
    @PutMapping(path = "/{definitionId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateConfig(@ApiParam(value = "ID of training definition", required = true)
                                             @PathVariable("definitionId") Long id,
                                             @ApiParam(value = "Dynamic flag configuration")
                                             @RequestBody DynamicFlagConfigDTO config) {
        TrainingDefinition td = trainingDefinitionService.findById(id);
        td.setEnableDynamicFlag(config.isEnableDynamicFlag());
        td.setFlagChangeInterval(config.getFlagChangeInterval());
        td.setInitialSecret(config.getInitialSecret());
        trainingDefinitionService.update(td);
        return ResponseEntity.ok().build();
    }

    /**
     * Get the current dynamic flag for a training definition.
     *
     * @param id the training definition ID
     * @return the current flag
     */
    @ApiOperation(httpMethod = "GET",
            value = "Get Current Dynamic Flag",
            response = String.class,
            nickname = "getCurrentDynamicFlag",
            produces = MediaType.TEXT_PLAIN_VALUE
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The current flag has been retrieved.", response = String.class),
            @ApiResponse(code = 400, message = "Dynamic flag is not enabled.", response = ApiError.class),
            @ApiResponse(code = 404, message = "The training definition has not been found.", response = ApiError.class),
            @ApiResponse(code = 500, message = "Unexpected condition was encountered.", response = ApiError.class)
    })
    @GetMapping(path = "/{definitionId}/current-flag", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> getCurrentFlag(@ApiParam(value = "ID of training definition", required = true)
                                                 @PathVariable("definitionId") Long id) {
        TrainingDefinition td = trainingDefinitionService.findById(id);
        if (!td.isEnableDynamicFlag()) {
            return ResponseEntity.badRequest().body("Dynamic flag not enabled");
        }
        String flag = dynamicFlagService.generateFlag(td.getInitialSecret(), td.getFlagChangeInterval());
        return ResponseEntity.ok(flag);
    }
}