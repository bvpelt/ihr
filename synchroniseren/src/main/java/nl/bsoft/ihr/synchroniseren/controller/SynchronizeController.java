package nl.bsoft.ihr.synchroniseren.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.bsoft.ihr.library.service.PlannenService;
import nl.bsoft.ihr.library.util.UpdateCounter;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Slf4j
@RequiredArgsConstructor
@Controller
public class SynchronizeController {

    private final PlannenService plannenService;

    @Operation(
            operationId = "ihrSynchronisation",
            summary = "Synchronize ihr",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK.", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = UpdateCounter.class))
                    })
            }
    )
    @RequestMapping(
            method = RequestMethod.GET,
            value = "/plannen",
            produces = {"application/json"}
    )
    public ResponseEntity<UpdateCounter> startIhrPlannen() {
        UpdateCounter counter = new UpdateCounter();

        counter = plannenService.getAllPlannen();

        return ResponseEntity.ok(counter);
    }

    @Operation(
            operationId = "ihrSynchronisation",
            summary = "Synchronize ihr",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK.", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = UpdateCounter.class))
                    })
            }
    )
    @RequestMapping(
            method = RequestMethod.GET,
            value = "/load",
            produces = {"application/json"}
    )
    public ResponseEntity<UpdateCounter> loadIhrPlannen() {
        UpdateCounter counter = new UpdateCounter();

        counter = plannenService.loadPlannen();

        return ResponseEntity.ok(counter);
    }
}
