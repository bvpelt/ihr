package nl.bsoft.ihr.library.mapper;

import lombok.Setter;
import nl.bsoft.ihr.generated.model.RelatieMetExternPlanReferentie;
import nl.bsoft.ihr.generated.model.RelatieMetExternPlanReferentieDossier;
import nl.bsoft.ihr.generated.model.RelatieMetExternPlanReferentiePlanstatusInfo;
import nl.bsoft.ihr.library.model.dto.ExternPlanDto;
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
public abstract class ExternPlanMapper implements JsonNullableMapper {

    @Mapping(target = "id", source = "id", ignore = true)
    @Mapping(target = "identificatie", source = "id", qualifiedByName = "toJsonNullableString")
    @Mapping(target = "naam", source = "naam", qualifiedByName = "toJsonNullableString")
    @Mapping(target = "planstatus", source = "planstatusInfo", qualifiedByName = "toPlanStatus")
    @Mapping(target = "planstatusdate", source = "planstatusInfo", qualifiedByName = "toPlanStatusDate")
    @Mapping(target = "dossier", source = "dossier", qualifiedByName = "toDossier")
    @Mapping(target = "href", source = "href", qualifiedByName = "toJsonNullableString")
    public abstract ExternPlanDto toExternPlan(RelatieMetExternPlanReferentie relaties) throws ParseException;

    @Named("toPlanStatus")
    protected String totoPlanStatus(JsonNullable<RelatieMetExternPlanReferentiePlanstatusInfo> planstatusinfo) {
        if (planstatusinfo.isPresent()) {
            if (planstatusinfo.get() != null) {
                return planstatusinfo.get().getPlanstatus();
            }
        }
        return null;
    }

    @Named("toPlanStatusDate")
    protected LocalDate toPlanStatusDate(JsonNullable<RelatieMetExternPlanReferentiePlanstatusInfo> planstatusinfo) {
        if (planstatusinfo.isPresent()) {
            if (planstatusinfo.get() != null) {
                String date = planstatusinfo.get().getDatum();
                return LocalDate.parse(date);
            }
        }
        return null;
    }

    @Named("toDossier")
    protected String toDossier(JsonNullable<RelatieMetExternPlanReferentieDossier> dossier) {
        if (dossier.isPresent()) {
            if (dossier.get() != null) {
                return dossier.get().getStatus();
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

