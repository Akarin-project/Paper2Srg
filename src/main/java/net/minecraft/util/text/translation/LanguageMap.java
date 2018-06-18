package net.minecraft.util.text.translation;

import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.IllegalFormatException;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Pattern;
import org.apache.commons.io.IOUtils;

public class LanguageMap {

    private static final Pattern NUMERIC_VARIABLE_PATTERN = Pattern.compile("%(\\d+\\$)?[\\d\\.]*[df]");
    private static final Splitter EQUAL_SIGN_SPLITTER = Splitter.on('=').limit(2);
    private static final LanguageMap instance = new LanguageMap();
    private final Map<String, String> languageList = Maps.newHashMap();
    private long lastUpdateTimeInMilliseconds;

    public LanguageMap() {
        try {
            InputStream inputstream = LanguageMap.class.getResourceAsStream("/assets/minecraft/lang/en_us.lang");
            Iterator iterator = IOUtils.readLines(inputstream, StandardCharsets.UTF_8).iterator();

            while (iterator.hasNext()) {
                String s = (String) iterator.next();

                if (!s.isEmpty() && s.charAt(0) != 35) {
                    String[] astring = (String[]) Iterables.toArray(LanguageMap.EQUAL_SIGN_SPLITTER.split(s), String.class);

                    if (astring != null && astring.length == 2) {
                        String s1 = astring[0];
                        String s2 = LanguageMap.NUMERIC_VARIABLE_PATTERN.matcher(astring[1]).replaceAll("%$1s");

                        this.languageList.put(s1, s2);
                    }
                }
            }

            this.lastUpdateTimeInMilliseconds = System.currentTimeMillis();
        } catch (IOException ioexception) {
            ;
        }

    }

    static LanguageMap getInstance() {
        return LanguageMap.instance;
    }

    public synchronized String translateKey(String s) {
        return this.tryTranslateKey(s);
    }

    public synchronized String translateKeyFormat(String s, Object... aobject) {
        String s1 = this.tryTranslateKey(s);

        try {
            return String.format(s1, aobject);
        } catch (IllegalFormatException illegalformatexception) {
            return "Format error: " + s1;
        }
    }

    private String tryTranslateKey(String s) {
        String s1 = (String) this.languageList.get(s);

        return s1 == null ? s : s1;
    }

    public synchronized boolean isKeyTranslated(String s) {
        return this.languageList.containsKey(s);
    }

    public long getLastUpdateTimeInMilliseconds() {
        return this.lastUpdateTimeInMilliseconds;
    }
}
