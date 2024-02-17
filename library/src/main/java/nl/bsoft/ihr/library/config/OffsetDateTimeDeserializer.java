package nl.bsoft.ihr.library.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonTokenId;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.datatype.jsr310.deser.InstantDeserializer;

import java.io.IOException;
import java.time.OffsetDateTime;

public class OffsetDateTimeDeserializer extends JsonDeserializer<OffsetDateTime> {
    @Override
    public OffsetDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        OffsetDateTime offsetDateTime = null;
        try {
            offsetDateTime = InstantDeserializer.OFFSET_DATE_TIME.deserialize(p, ctxt);
        } catch (InvalidFormatException invalidFormatException) {
            if (p.currentTokenId() == JsonTokenId.ID_STRING) {
                offsetDateTime = OffsetDateTime.parse(p.getText());
            }
        }
        return offsetDateTime;
    }
}