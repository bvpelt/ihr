package nl.bsoft.ihr.library.mapper;

import lombok.Setter;
import nl.bsoft.ihr.generated.model.BaseBestemmingsvlakBestemmingsfunctiesInner;
import nl.bsoft.ihr.generated.model.Bestemmingsvlak;
import nl.bsoft.ihr.library.model.dto.BestemmingFunctieDto;
import nl.bsoft.ihr.library.model.dto.BestemmingsvlakDto;
import nl.bsoft.ihr.library.model.dto.TekstRefDto;
import org.locationtech.jts.io.ParseException;
import org.mapstruct.*;
import org.openapitools.jackson.nullable.JsonNullable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Setter
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = {
                JsonNullableMapper.class
        },
        nullValueIterableMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
public abstract class BestemmingsvlakMapper {

    @Mapping(target = "id", source = "id", ignore = true)
    @Mapping(target = "identificatie", source = "id", qualifiedByName = "toId")
    @Mapping(target = "type", source = "type", qualifiedByName = "toType")
    @Mapping(target = "naam", source = "naam")
    @Mapping(target = "bestemmingshoofdgroep", source = "bestemmingshoofdgroep", qualifiedByName = "toJsonNullableString")
    @Mapping(target = "bestemmingsfuncties", source = "bestemmingsfuncties", qualifiedByName = "toBestemmingsfuncties")
    @Mapping(target = "artikelnummer", source = "artikelnummer", qualifiedByName = "toJsonNullableString")
    @Mapping(target = "verwijzingNaarTekst", source = "verwijzingNaarTekst", qualifiedByName = "toTekstRef")
    @Mapping(target = "labelInfo", source = "labelInfo", qualifiedByName = "toJsonNullableString")
    public abstract BestemmingsvlakDto toBestemmingsvlak(Bestemmingsvlak bestemmingsvlak) throws ParseException;

    @Named("toId")
    protected String toPlanStatusDate(String id) {
        return id;
    }

    @Named("toType")
    protected String toType(Bestemmingsvlak.TypeEnum typeEnum) {
        return typeEnum.getValue();
    }

    @Named("toBestemmingsfuncties")
    protected Set<BestemmingFunctieDto> toBestemmingsfuncties(List<BaseBestemmingsvlakBestemmingsfunctiesInner> bestemmingsfuncties) {
        Set<BestemmingFunctieDto> bestemmingFunctieDtos = new HashSet<>();

        bestemmingsfuncties.forEach(bestemmingsfunctie -> {
            BestemmingFunctieDto bestemmingFunctieDto = new BestemmingFunctieDto();
            bestemmingFunctieDto.setBestemmingsfunctie(bestemmingsfunctie.getBestemmingsfunctie());
            bestemmingFunctieDto.setFunctieniveau(bestemmingsfunctie.getFunctieniveau());
            bestemmingFunctieDtos.add(bestemmingFunctieDto);
        });
        return bestemmingFunctieDtos;
    }
    @Named("toTekstRef")
    protected Set<TekstRefDto> toVerwijzingNaarTekst(List<String> verwijzing) {
        Set<TekstRefDto>  tekstRefDtoSet = new HashSet<>();

        verwijzing.forEach(tekstref -> {
            TekstRefDto tekstRefDto = new TekstRefDto();
            tekstRefDto.setReferentie(tekstref);
            tekstRefDtoSet.add(tekstRefDto);
        });

        return tekstRefDtoSet;
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

