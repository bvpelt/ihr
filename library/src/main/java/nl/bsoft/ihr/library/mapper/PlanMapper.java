package nl.bsoft.ihr.library.mapper;

import lombok.Setter;
import nl.bsoft.ihr.generated.model.Plan;
import nl.bsoft.ihr.generated.model.PlanDossier;
import nl.bsoft.ihr.generated.model.PlanType;
import nl.bsoft.ihr.generated.model.PlanstatusInfo;
import nl.bsoft.ihr.library.model.dto.PlanDto;
import nl.bsoft.ihr.library.model.dto.PlanStatusDto;
import org.locationtech.jts.io.ParseException;
import org.mapstruct.*;
import org.openapitools.jackson.nullable.JsonNullable;

import java.time.LocalDate;

@Setter
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = {
                JsonNullableMapper.class
        },
        nullValueIterableMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
public abstract class PlanMapper {

    @Mapping(target = "id", source = "id", ignore = true)
    @Mapping(target = "identificatie", source = "id", qualifiedByName = "toId")
    @Mapping(target = "plantype", source = "type") //, qualifiedByName = "toPlanType")
    @Mapping(target = "naam", source = "naam")
    @Mapping(target = "besluitnummer", source = "besluitnummer", qualifiedByName = "toJsonNullableString")
    @Mapping(target = "regelstatus", source = "regelStatus", qualifiedByName = "toJsonNullableString")
    @Mapping(target = "planstatus", source = "planstatusInfo", qualifiedByName = "toPlanStatusInfo")
    @Mapping(target = "dossierid", source = "dossier", qualifiedByName = "toDossierId")
    @Mapping(target = "dossierstatus", source = "dossier", qualifiedByName = "toDossierStatus")
    @Mapping(target = "isparapluplan", source = "isParapluplan")
    @Mapping(target = "verwijzingnaarvaststelling", source = "verwijzingNaarVaststellingsbesluit", qualifiedByName = "toJsonNullableString")
    @Mapping(target = "verwijzingnaargml", source = "verwijzingNaarGml")
    @Mapping(target = "beroepenbezwaar", source = "beroepEnBezwaar", qualifiedByName = "toBeroepEnBezwaar")
    public abstract PlanDto toPlan(Plan plan) throws ParseException;

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
