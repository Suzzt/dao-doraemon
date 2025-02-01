package org.dao.doraemon.sensitive.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonStreamContext;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.dao.doraemon.sensitive.drive.data.Derivator;
import org.dao.doraemon.sensitive.model.SensitiveEntry;
import org.dao.doraemon.sensitive.model.SensitiveFieldModel;
import org.dao.doraemon.sensitive.model.SensitiveMethodModel;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Set;

/**
 * @author sucf
 * @since 1.0
 */
public class SensitiveSerializer extends StdSerializer<String> {

    private final Derivator derivator;
    private final static ThreadLocal<Set<SensitiveMethodModel>> METHOD = new ThreadLocal<>();
    private final static ThreadLocal<Set<SensitiveFieldModel>> FIELD = new ThreadLocal<>();
    // todo class
    public SensitiveSerializer(Derivator derivator) {
        super(String.class);
        this.derivator = derivator;
    }

    public static void setSensitiveConfig(Set<SensitiveMethodModel> sensitiveMethodModels, Set<SensitiveFieldModel> sensitiveFieldModels) {
        METHOD.set(sensitiveMethodModels);
        FIELD.set(sensitiveFieldModels);
    }

    public static void removeSensitiveConfig() {
        METHOD.remove();
        FIELD.remove();
    }

    @Override
    public void serialize(String value, JsonGenerator gen, SerializerProvider serializerProvider) throws IOException {
        Class<?> aClass = gen.getOutputContext().getCurrentValue().getClass();
        if (aClass.isPrimitive()) {
            gen.writeString(value);
            return;
        }
        String name = gen.getOutputContext().getCurrentName();
        Set<SensitiveMethodModel> sensitiveMethodModels = METHOD.get();
        Set<SensitiveFieldModel> sensitiveFieldModels = FIELD.get();
        if (sensitiveMethodModels != null) {
            for (SensitiveMethodModel sensitiveMethodModel : sensitiveMethodModels) {
                if (sensitiveMethodModel.getFieldName().equals(name)) {
                    SensitiveEntry sensitiveEntry = sensitiveMethodModel.getHandler().generate(value, derivator);
                    gen.writeStartObject();
                    gen.writeStringField("key", sensitiveEntry.getKey());
                    gen.writeStringField("value", sensitiveEntry.getValue());
                    gen.writeEndObject();
                    return;
                }
            }
        }

        if (sensitiveFieldModels != null) {
            for (SensitiveFieldModel sensitiveFieldModel : sensitiveFieldModels) {
                JsonStreamContext context = gen.getOutputContext();
                LinkedList<String> path = new LinkedList<>();
                while (context != null) {
                    String currentName = context.getCurrentName();
                    if (currentName != null) {
                        path.addFirst(currentName);
                    }
                    context = context.getParent();
                }
                String fullPath = String.join(".", path);
                if (sensitiveFieldModel.getPath().equals(fullPath)) {
                    SensitiveEntry sensitiveEntry = sensitiveFieldModel.getHandler().generate(value, derivator);
                    gen.writeStartObject();
                    gen.writeStringField("key", sensitiveEntry.getKey());
                    gen.writeStringField("value", sensitiveEntry.getValue());
                    gen.writeEndObject();
                    return;
                }
            }
        }
        gen.writeString(value);
    }
}