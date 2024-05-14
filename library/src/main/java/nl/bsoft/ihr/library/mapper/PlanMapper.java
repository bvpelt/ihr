package nl.bsoft.ihr.library.mapper;

import lombok.Setter;
import nl.bsoft.ihr.generated.model.*;
import nl.bsoft.ihr.library.model.dto.OverheidDto;
import nl.bsoft.ihr.library.model.dto.PlanDto;
import nl.bsoft.ihr.library.model.dto.PlanStatusDto;
import org.locationtech.jts.io.ParseException;
import org.mapstruct.*;
import org.openapitools.jackson.nullable.JsonNullable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Setter
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = {
                JsonNullableMapper.class
        },
        nullValueIterableMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
public abstract class PlanMapper implements JsonNullableMapper {

    @Mapping(target = "id", source = "id", ignore = true)
    @Mapping(target = "identificatie", source = "id", qualifiedByName = "toId")
    @Mapping(target = "plantype", source = "type", qualifiedByName = "toPlanType")
    @Mapping(target = "beleidsmatigeoverheid", source = "beleidsmatigVerantwoordelijkeOverheid", qualifiedByName = "toBeleidsmatigeOverheid")
    @Mapping(target = "publicerendeoverheid", source = "publicerendBevoegdGezag", qualifiedByName = "toPublicerendeOverheid")
    @Mapping(target = "illustraties", source = "illustraties", ignore = true)
    @Mapping(target = "ondergronden", source = "ondergronden", ignore = true)
    @Mapping(target = "naam", source = "naam")
    @Mapping(target = "besluitnummer", source = "besluitnummer", qualifiedByName = "toJsonNullableString")
    @Mapping(target = "regelstatus", source = "regelStatus", qualifiedByName = "toJsonNullableString")
    @Mapping(target = "planstatus", source = "planstatusInfo", qualifiedByName = "toPlanStatusDto")
    @Mapping(target = "dossierid", source = "dossier", qualifiedByName = "toDossierId")
    @Mapping(target = "dossierstatus", source = "dossier", qualifiedByName = "toDossierStatus")
    @Mapping(target = "ishistorisch", source = "isHistorisch")
    @Mapping(target = "verwijderdop", source = "verwijderdOp", qualifiedByName = "toIsVerwijderdOp")
    @Mapping(target = "isparapluplan", source = "isParapluplan")
    @Mapping(target = "istamplan", source = "isTamPlan")
    @Mapping(target = "einderechtsgeldigheid", source = "eindeRechtsgeldigheid", qualifiedByName = "toEindeRechtsgeldigheid")
    @Mapping(target = "verwijzingnaarvaststelling", source = "verwijzingNaarVaststellingsbesluit", qualifiedByName = "toJsonNullableString")
    @Mapping(target = "verwijzingnaargml", source = "verwijzingNaarGml")
    @Mapping(target = "beroepenbezwaar", source = "beroepEnBezwaar", qualifiedByName = "toBeroepEnBezwaar")
    public abstract PlanDto toPlan(Plan plan) throws ParseException;

    @Named("toPlanStatusDto")
    protected PlanStatusDto toPlanStatusDto(PlanstatusInfo planstatusInfo) {
        PlanStatusDto planStatusDto = null;
        if (planstatusInfo != null) {
            planStatusDto = new PlanStatusDto();
            planStatusDto.setStatus(planstatusInfo.getPlanstatus().getValue());
            LocalDate localDate = planstatusInfo.getDatum();
            planStatusDto.setDatum(localDate);
        }
        return planStatusDto;
    }

    @Named("toBeleidsmatigeOverheid")
    protected OverheidDto toBeleidsmatigeOverheid(PlanBeleidsmatigVerantwoordelijkeOverheid overheid) {
        OverheidDto beleidsmatigeOverheid = null;
        if (overheid != null) {
            beleidsmatigeOverheid = new OverheidDto();
            if (overheid.getNaam().isPresent()) {
                beleidsmatigeOverheid.setNaam(overheid.getNaam().get());
            }
            if (overheid.getCode().isPresent()) {
                beleidsmatigeOverheid.setCode(overheid.getCode().get());
            }
            beleidsmatigeOverheid.setType(overheid.getType().getValue());
        }
        return beleidsmatigeOverheid;
    }

    @Named("toPublicerendeOverheid")
    protected OverheidDto toPublicerendeOverheid(JsonNullable<PlanPublicerendBevoegdGezag> overheid) {
        OverheidDto publicerendeOverheid = null;

        if (overheid != null) {
            if (overheid.isPresent()) {
                publicerendeOverheid = new OverheidDto();
                if (overheid.get().getNaam().isPresent()) {
                    publicerendeOverheid.setNaam(overheid.get().getNaam().get());
                }
                if (overheid.get().getCode().isPresent()) {
                    publicerendeOverheid.setCode(overheid.get().getCode().get());
                }
                publicerendeOverheid.setType(overheid.get().getType().getValue());
            }
        }
        return publicerendeOverheid;
    }

    @Named("toId")
    protected String toId(String id) {
        return id;
    }

    @Named("toPlanType")
    protected String toPlanType(PlanType planType) {
        String type = null;

        type = planType.getValue();
        return type;
    }

    @Named("toIsVerwijderdOp")
    protected LocalDateTime toIsVerwijderdOp(JsonNullable<OffsetDateTime> date) {
        LocalDateTime ldate = null;
        if (date != null) {
            if (date.isPresent()) {
                OffsetDateTime odt = date.get();
                if (odt != null) {
                    ldate = odt.toLocalDateTime();
                }
            }
        }
        return ldate;
    }

    @Named("toEindeRechtsgeldigheid")
    protected LocalDate toEindeRechtsgeldigheid(JsonNullable<LocalDate> date) {
        LocalDate ldate = null;
        if (date.isPresent()) {
            ldate = date.get();
        }
        return ldate;
    }

    @Named("toPlanStatusInfo")
    protected PlanStatusDto toPlanStatusInfo(PlanstatusInfo planstatusInfo) {
        PlanStatusDto planStatusDto = new PlanStatusDto();

        LocalDate date = planstatusInfo.getDatum();
        planStatusDto.setDatum(date);
        planStatusDto.setStatus(planstatusInfo.getPlanstatus().getValue());

        return planStatusDto;
    }

    @Named("toDossierId")
    protected String toDossierId(JsonNullable<PlanDossier> planDossier) {
        String dossierid = null;

        if (planDossier != null) {
            if (planDossier.isPresent()) {
                if (planDossier.get() != null) {
                    if (planDossier.get().getId() != null) {
                        dossierid = planDossier.get().getId();
                    }
                }
            }
        }
        return dossierid;
    }

    @Named("toDossierStatus")
    protected String toDossierStatus(JsonNullable<PlanDossier> planDossier) {
        String dossierstatus = null;

        if (planDossier != null) {
            if (planDossier.isPresent()) {
                if (planDossier.get() != null) {
                    if (planDossier.get().getStatus().isPresent()) {
                        dossierstatus = planDossier.get().getStatus().get();
                    }
                }
            }
        }
        return dossierstatus;
    }

    @Named("toBeroepEnBezwaar")
    protected String toBeroepEnBezwaar(JsonNullable<String> value) {
/* Enum implementation
        if (value != null) {
            if (value.isPresent()) {
                if (value.get() != null) {
                    return value.get().getValue();
                }
            }
        }

 */
        return toJsonNullableString(value);
    }

    @Named("toJsonNullableString")
    protected String toJsonNullableString(JsonNullable<String> jsonNullable) {
        if (jsonNullable.isPresent()) {
            return jsonNullable.get();
        } else {
            return null;
        }
    }
}

