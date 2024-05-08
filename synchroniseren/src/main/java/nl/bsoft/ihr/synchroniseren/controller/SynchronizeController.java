package nl.bsoft.ihr.synchroniseren.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.models.media.MediaType;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.bsoft.ihr.library.service.*;
import nl.bsoft.ihr.library.util.UpdateCounter;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Slf4j
@RequiredArgsConstructor
@Controller
public class SynchronizeController {

    private final PlannenService plannenService;
    private final TekstenService tekstenService;
    private final BestemmingsvlakkenService bestemmingsvlakkenService;
    private final BouwvlakkenService bouwvlakkenService;
    private final BouwaanduidingenService bouwaanduidingenService;
    private final LettertekenaanduidingService lettertekenaanduidingService;
    private final MaatvoeringenService maatvoeringenService;
    private final FigurenService figurenService;
    private final StructuurVisieGebiedenService structuurVisieGebiedenService;

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
            operationId = "ihrPlannenSynchronisation",
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

    @Operation(
            operationId = "ihrPlanSynchronisation",
            summary = "Synchronize ihr",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK.", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = UpdateCounter.class))
                    })
            }
    )
    @RequestMapping(
            method = RequestMethod.GET,
            value = "/load/{identificatie}",
            produces = {"application/json"}
    )
    public ResponseEntity<UpdateCounter> loadIhrPlan(@PathVariable String identificatie) {
        UpdateCounter counter = new UpdateCounter();

        counter = plannenService.loadPlan(identificatie);

        return ResponseEntity.ok(counter);
    }

    @Operation(
            operationId = "ihrTekstenSynchronisation",
            summary = "Synchronize ihr",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK.", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = UpdateCounter.class))
                    })
            }
    )
    @RequestMapping(
            method = RequestMethod.GET,
            value = "/teksten",
            produces = {"application/json"}
    )
    public ResponseEntity<UpdateCounter> loadIhrTeksten() {
        UpdateCounter counter = new UpdateCounter();

        counter = tekstenService.loadTekstenFromList();

        return ResponseEntity.ok(counter);
    }

    @Operation(
            operationId = "ihrBestemmingsvlakkenSynchronisation",
            summary = "Synchronize ihr",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK.", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = UpdateCounter.class))
                    })
            }
    )
    @RequestMapping(
            method = RequestMethod.GET,
            value = "/bestemmingsvlakken",
            produces = {"application/json"}
    )
    public ResponseEntity<UpdateCounter> loadBestemmingsvlakken() {
        UpdateCounter counter = new UpdateCounter();

        counter = bestemmingsvlakkenService.loadBestemmingsvlakkenFromList();

        return ResponseEntity.ok(counter);
    }


    @Operation(
            operationId = "ihrBouwvlakkenSynchronisation",
            summary = "Synchronize ihr",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK.", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = UpdateCounter.class))
                    })
            }
    )
    @RequestMapping(
            method = RequestMethod.GET,
            value = "/bouwvlakken",
            produces = {"application/json"}
    )
    public ResponseEntity<UpdateCounter> loadBouwvlakken() {
        UpdateCounter counter = new UpdateCounter();

        counter = bouwvlakkenService.loadBouwvlakkenFromList();

        return ResponseEntity.ok(counter);
    }

    @Operation(
            operationId = "ihrBouwaanduidingenSynchronisation",
            summary = "Synchronize ihr",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK.", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = UpdateCounter.class))
                    })
            }
    )
    @RequestMapping(
            method = RequestMethod.GET,
            value = "/bouwaanduidingen",
            produces = {"application/json"}
    )
    public ResponseEntity<UpdateCounter> loadBouwaanduidingen() {
        UpdateCounter counter = new UpdateCounter();

        counter = bouwaanduidingenService.loadBouwaanduidingenFromList();

        return ResponseEntity.ok(counter);
    }

    @Operation(
            operationId = "ihrLettertekenaanduidingenSynchronisation",
            summary = "Synchronize ihr",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK.", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = UpdateCounter.class))
                    })
            }
    )
    @RequestMapping(
            method = RequestMethod.GET,
            value = "/lettertekenduidingen",
            produces = {"application/json"}
    )
    public ResponseEntity<UpdateCounter> loadLettertekenaanduidingen() {
        UpdateCounter counter = new UpdateCounter();

        counter = lettertekenaanduidingService.loadLettertekenaanduidingenFromList();

        return ResponseEntity.ok(counter);
    }

    @Operation(
            operationId = "ihrMaatvoeringenSynchronisation",
            summary = "Synchronize ihr",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK.", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = UpdateCounter.class))
                    })
            }
    )
    @RequestMapping(
            method = RequestMethod.GET,
            value = "/maatvoeringen",
            produces = {"application/json"}
    )
    public ResponseEntity<UpdateCounter> loadMaatvoeringen() {
        UpdateCounter counter = new UpdateCounter();

        counter = maatvoeringenService.loadMaatvoeringenFromList();

        return ResponseEntity.ok(counter);
    }

    @Operation(
            operationId = "ihrFigurenSynchronisation",
            summary = "Synchronize ihr",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK.", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = UpdateCounter.class))
                    })
            }
    )
    @RequestMapping(
            method = RequestMethod.GET,
            value = "/figuren",
            produces = {"application/json"}
    )
    public ResponseEntity<UpdateCounter> loadFiguren() {
        UpdateCounter counter = new UpdateCounter();

        counter = figurenService.loadFigurenFromList();

        return ResponseEntity.ok(counter);
    }

    @Operation(
            operationId = "ihrStructuurvisieGebiedenSynchronisation",
            summary = "Synchronize ihr",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK.", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = UpdateCounter.class))
                    })
            }
    )
    @RequestMapping(
            method = RequestMethod.GET,
            value = "/structuurvisiegebieden",
            produces = {"application/json"}
    )
    public ResponseEntity<UpdateCounter> loadStructuurvisieGebieden() {
        UpdateCounter counter = new UpdateCounter();

        counter = structuurVisieGebiedenService.loadStructuurvisieGebiedenFromList();

        return ResponseEntity.ok(counter);
    }
}
