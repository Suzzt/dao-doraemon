package org.dao.doraemon.sensitive.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.dao.doraemon.sensitive.model.SensitiveEntry;
import org.dao.doraemon.sensitive.model.SensitiveModel;

import java.io.IOException;
import java.util.Set;

/**
 * @author sucf
 * @since 1.0
 */
public class SensitiveSerializer extends StdSerializer<String> {

    private final static ThreadLocal<Set<SensitiveModel>> sensitive = new ThreadLocal<>();

    public SensitiveSerializer() {
        super(String.class);
    }

    public static void setSensitiveConfig(Set<SensitiveModel> sensitiveModels) {
        sensitive.set(sensitiveModels);
    }

    public static void removeSensitiveConfig() {
        sensitive.remove();
    }

    @Override
    public void serialize(String value, JsonGenerator gen, SerializerProvider serializerProvider) throws IOException {
        Class<?> aClass = gen.getOutputContext().getCurrentValue().getClass();
        if (aClass.isPrimitive()) {
            gen.writeString(value);
            return;
        }
        String name = gen.getOutputContext().getCurrentName();
        Set<SensitiveModel> sensitiveModels = sensitive.get();
        for (SensitiveModel sensitiveModel : sensitiveModels) {
            if (sensitiveModel.getFieldName().equals(name)) {
                SensitiveEntry sensitiveEntry = sensitiveModel.getHandler().generateSensitiveEntry(value);
                gen.writeStartObject();
                gen.writeNumberField("key", sensitiveEntry.getKey());
                gen.writeStringField("value", sensitiveEntry.getValue());
                gen.writeEndObject();
            } else {
                gen.writeString(value);
            }
        }
    }
}