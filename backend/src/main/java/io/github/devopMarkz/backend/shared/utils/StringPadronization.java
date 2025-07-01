package io.github.devopMarkz.backend.shared.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Locale;

@Component
public class StringPadronization extends StdDeserializer<String> {


    private static final long serialVersionUID = 7527542687158493910L;

    public StringPadronization() {
        super(String.class);
    }

    public String converter(String string) {
        if (string != null) {
            String stringSemAcento = string.toUpperCase(Locale.ROOT);

            stringSemAcento = stringSemAcento.replaceAll("á", "a");
            stringSemAcento = stringSemAcento.replaceAll("à", "a");
            stringSemAcento = stringSemAcento.replaceAll("ã", "a");
            stringSemAcento = stringSemAcento.replaceAll("â", "a");
            stringSemAcento = stringSemAcento.replaceAll("ä", "a");
            stringSemAcento = stringSemAcento.replaceAll("Á", "A");
            stringSemAcento = stringSemAcento.replaceAll("À", "A");
            stringSemAcento = stringSemAcento.replaceAll("Ã", "A");
            stringSemAcento = stringSemAcento.replaceAll("Â", "A");
            stringSemAcento = stringSemAcento.replaceAll("Ä", "A");

            stringSemAcento = stringSemAcento.replaceAll("é", "e");
            stringSemAcento = stringSemAcento.replaceAll("è", "e");
            stringSemAcento = stringSemAcento.replaceAll("ê", "e");
            stringSemAcento = stringSemAcento.replaceAll("ë", "e");
            stringSemAcento = stringSemAcento.replaceAll("É", "E");
            stringSemAcento = stringSemAcento.replaceAll("È", "E");
            stringSemAcento = stringSemAcento.replaceAll("Ê", "E");
            stringSemAcento = stringSemAcento.replaceAll("Ë", "E");

            stringSemAcento = stringSemAcento.replaceAll("í", "i");
            stringSemAcento = stringSemAcento.replaceAll("ì", "i");
            stringSemAcento = stringSemAcento.replaceAll("î", "i");
            stringSemAcento = stringSemAcento.replaceAll("ï", "i");
            stringSemAcento = stringSemAcento.replaceAll("Í", "I");
            stringSemAcento = stringSemAcento.replaceAll("Ì", "I");
            stringSemAcento = stringSemAcento.replaceAll("Î", "I");
            stringSemAcento = stringSemAcento.replaceAll("Ï", "I");

            stringSemAcento = stringSemAcento.replaceAll("ó", "o");
            stringSemAcento = stringSemAcento.replaceAll("ò", "o");
            stringSemAcento = stringSemAcento.replaceAll("õ", "o");
            stringSemAcento = stringSemAcento.replaceAll("ô", "o");
            stringSemAcento = stringSemAcento.replaceAll("ö", "o");
            stringSemAcento = stringSemAcento.replaceAll("Ó", "O");
            stringSemAcento = stringSemAcento.replaceAll("Ò", "O");
            stringSemAcento = stringSemAcento.replaceAll("Õ", "O");
            stringSemAcento = stringSemAcento.replaceAll("Ô", "O");
            stringSemAcento = stringSemAcento.replaceAll("Ö", "O");

            stringSemAcento = stringSemAcento.replaceAll("ú", "u");
            stringSemAcento = stringSemAcento.replaceAll("ù", "u");
            stringSemAcento = stringSemAcento.replaceAll("û", "u");
            stringSemAcento = stringSemAcento.replaceAll("ü", "u");
            stringSemAcento = stringSemAcento.replaceAll("Ú", "U");
            stringSemAcento = stringSemAcento.replaceAll("Ù", "U");
            stringSemAcento = stringSemAcento.replaceAll("Û", "U");
            stringSemAcento = stringSemAcento.replaceAll("Ü", "U");

            stringSemAcento = stringSemAcento.replaceAll("ç", "c");
            stringSemAcento = stringSemAcento.replaceAll("Ç", "C");

            return stringSemAcento;
        }

        return null;
    }


    @Override
    public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        try {
            return converter(_parseString(p,ctxt));
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return null;
    }
}
