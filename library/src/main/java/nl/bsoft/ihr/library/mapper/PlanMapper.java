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
import java.util.HashSet;
import java.util.Set;

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
/*
    @Named("toBeleidType")
    protected Set<OverheidDto> toBeleidType(PlanBeleidsmatigVerantwoordelijkeOverheid publicerendBevoegdGezag) {
        Set<OverheidDto> overheidDtoSet = new HashSet<>();
        OverheidDto overheidDto = new OverheidDto();
        String type;
        String code = null;
        String naam = null;

        type = publicerendBevoegdGezag.getType().getValue();
        if (publicerendBevoegdGezag.getCode().isPresent()) {
            code = publicerendBevoegdGezag.getCode().get();
        }
        if (publicerendBevoegdGezag.getNaam().isPresent()) {
            naam = publicerendBevoegdGezag.getNaam().get();
        }

        overheidDto.setType(type);
        overheidDto.setCode(code);
        overheidDto.setNaam(naam);
        overheidDtoSet.add(overheidDto);

        return overheidDtoSet;
    }
*/

    /*
    @Named("toPublicerendType")
    protected Set<OverheidDto> toPublicerendType(JsonNullable<PlanPublicerendBevoegdGezag> publicerendBevoegdGezag) {
        Set<OverheidDto> overheidDtoSet = null;

        if (publicerendBevoegdGezag.isPresent()) {
            String type;
            String code = null;
            String naam = null;
            overheidDtoSet = new HashSet<>();
            type = publicerendBevoegdGezag.get().getType().getValue();

            if (publicerendBevoegdGezag.get().getCode().isPresent()) {
                code = publicerendBevoegdGezag.get().getCode().get();
            }
            if (publicerendBevoegdGezag.get().getNaam().isPresent()) {
                naam = publicerendBevoegdGezag.get().getNaam().get();
            }
            OverheidDto overheidDto = new OverheidDto();
            overheidDto.setType(type);
            overheidDto.setCode(code);
            overheidDto.setNaam(naam);

            overheidDtoSet.add(overheidDto);
        }

        return overheidDtoSet;
    }
*/
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
    protected String toBeroepEnBezwaar(JsonNullable<Plan.BeroepEnBezwaarEnum> value) {

        if (value != null) {
            if (value.isPresent()) {
                if (value.get() != null) {
                    return value.get().getValue();
                }
            }
        }
        return null;
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
