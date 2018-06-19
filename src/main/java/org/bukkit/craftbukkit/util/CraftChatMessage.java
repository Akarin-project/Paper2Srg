package org.bukkit.craftbukkit.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent.Action;
import net.minecraft.util.text.ITextComponent;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import net.minecraft.util.text.TextComponentTranslation;
import org.bukkit.ChatColor;

public final class CraftChatMessage {

    private static final Pattern LINK_PATTERN = Pattern.compile("((?:(?:https?):\\/\\/)?(?:[-\\w_\\.]{2,}\\.[a-z]{2,4}.*?(?=[\\.\\?!,;:]?(?:[" + String.valueOf(org.bukkit.ChatColor.COLOR_CHAR) + " \\n]|$))))");
    private static final Map<Character, TextFormatting> formatMap;

    static {
        Builder<Character, TextFormatting> builder = ImmutableMap.builder();
        for (TextFormatting format : TextFormatting.values()) {
            builder.put(Character.toLowerCase(format.toString().charAt(1)), format);
        }
        formatMap = builder.build();
    }

    public static TextFormatting getColor(ChatColor color) {
        return formatMap.get(color.getChar());
    }

    public static ChatColor getColor(TextFormatting format) {
        return ChatColor.getByChar(format.field_96329_z);
    }

    private static class StringMessage {
        private static final Pattern INCREMENTAL_PATTERN = Pattern.compile("(" + String.valueOf(org.bukkit.ChatColor.COLOR_CHAR) + "[0-9a-fk-or])|(\\n)|((?:(?:https?):\\/\\/)?(?:[-\\w_\\.]{2,}\\.[a-z]{2,4}.*?(?=[\\.\\?!,;:]?(?:[" + String.valueOf(org.bukkit.ChatColor.COLOR_CHAR) + " \\n]|$))))", Pattern.CASE_INSENSITIVE);

        private final List<ITextComponent> list = new ArrayList<ITextComponent>();
        private ITextComponent currentChatComponent = new TextComponentString("");
        private Style modifier = new Style();
        private final ITextComponent[] output;
        private int currentIndex;
        private final String message;

        private StringMessage(String message,  boolean keepNewlines) {
            this.message = message;
            if (message == null) {
                output = new ITextComponent[] { currentChatComponent };
                return;
            }
            list.add(currentChatComponent);

            Matcher matcher = INCREMENTAL_PATTERN.matcher(message);
            String match = null;
            while (matcher.find()) {
                int groupId = 0;
                while ((match = matcher.group(++groupId)) == null) {
                    // NOOP
                }
                appendNewComponent(matcher.start(groupId));
                switch (groupId) {
                case 1:
                    TextFormatting format = formatMap.get(match.toLowerCase(java.util.Locale.ENGLISH).charAt(1));
                    if (format == TextFormatting.RESET) {
                        modifier = new Style();
                    } else if (format.func_96301_b()) {
                        switch (format) {
                        case BOLD:
                            modifier.func_150227_a(Boolean.TRUE);
                            break;
                        case ITALIC:
                            modifier.func_150217_b(Boolean.TRUE);
                            break;
                        case STRIKETHROUGH:
                            modifier.func_150225_c(Boolean.TRUE);
                            break;
                        case UNDERLINE:
                            modifier.func_150228_d(Boolean.TRUE);
                            break;
                        case OBFUSCATED:
                            modifier.func_150237_e(Boolean.TRUE);
                            break;
                        default:
                            throw new AssertionError("Unexpected message format");
                        }
                    } else { // Color resets formatting
                        modifier = new Style().func_150238_a(format);
                    }
                    break;
                case 2:
                    if (keepNewlines) {
                        currentChatComponent.func_150257_a(new TextComponentString("\n"));
                    } else {
                        currentChatComponent = null;
                    }
                    break;
                case 3:
                    if ( !( match.startsWith( "http://" ) || match.startsWith( "https://" ) ) ) {
                        match = "http://" + match;
                    }
                    modifier.func_150241_a(new ClickEvent(Action.OPEN_URL, match));
                    appendNewComponent(matcher.end(groupId));
                    modifier.func_150241_a((ClickEvent) null);
                }
                currentIndex = matcher.end(groupId);
            }

            if (currentIndex < message.length()) {
                appendNewComponent(message.length());
            }

            output = list.toArray(new ITextComponent[list.size()]);
        }

        private void appendNewComponent(int index) {
            if (index <= currentIndex) {
                return;
            }
            ITextComponent addition = new TextComponentString(message.substring(currentIndex, index)).func_150255_a(modifier);
            currentIndex = index;
            modifier = modifier.func_150232_l();
            if (currentChatComponent == null) {
                currentChatComponent = new TextComponentString("");
                list.add(currentChatComponent);
            }
            currentChatComponent.func_150257_a(addition);
        }

        private ITextComponent[] getOutput() {
            return output;
        }
    }

    public static ITextComponent[] fromString(String message) {
        return fromString(message, false);
    }
    
    public static ITextComponent[] fromString(String message, boolean keepNewlines) {
        return new StringMessage(message, keepNewlines).getOutput();
    }
    
    public static String fromComponent(ITextComponent component) {
        return fromComponent(component, TextFormatting.BLACK);
    }

    public static String fromComponent(ITextComponent component, TextFormatting defaultColor) {
        if (component == null) return "";
        StringBuilder out = new StringBuilder();
        
        for (ITextComponent c : (Iterable<ITextComponent>) component) {
            Style modi = c.func_150256_b();
            out.append(modi.func_150215_a() == null ? defaultColor : modi.func_150215_a());
            if (modi.func_150223_b()) {
                out.append(TextFormatting.BOLD);
            }
            if (modi.func_150242_c()) {
                out.append(TextFormatting.ITALIC);
            }
            if (modi.func_150234_e()) {
                out.append(TextFormatting.UNDERLINE);
            }
            if (modi.func_150236_d()) {
                out.append(TextFormatting.STRIKETHROUGH);
            }
            if (modi.func_150233_f()) {
                out.append(TextFormatting.OBFUSCATED);
            }
            out.append(c.func_150261_e());
        }
        return out.toString().replaceFirst("^(" + defaultColor + ")*", "");
    }

    public static ITextComponent fixComponent(ITextComponent component) {
        Matcher matcher = LINK_PATTERN.matcher("");
        return fixComponent(component, matcher);
    }

    private static ITextComponent fixComponent(ITextComponent component, Matcher matcher) {
        if (component instanceof TextComponentString) {
            TextComponentString text = ((TextComponentString) component);
            String msg = text.func_150265_g();
            if (matcher.reset(msg).find()) {
                matcher.reset();

                Style modifier = text.func_150256_b() != null ?
                        text.func_150256_b() : new Style();
                List<ITextComponent> extras = new ArrayList<ITextComponent>();
                List<ITextComponent> extrasOld = new ArrayList<ITextComponent>(text.func_150253_a());
                component = text = new TextComponentString("");

                int pos = 0;
                while (matcher.find()) {
                    String match = matcher.group();

                    if ( !( match.startsWith( "http://" ) || match.startsWith( "https://" ) ) ) {
                        match = "http://" + match;
                    }

                    TextComponentString prev = new TextComponentString(msg.substring(pos, matcher.start()));
                    prev.func_150255_a(modifier);
                    extras.add(prev);

                    TextComponentString link = new TextComponentString(matcher.group());
                    Style linkModi = modifier.func_150232_l();
                    linkModi.func_150241_a(new ClickEvent(Action.OPEN_URL, match));
                    link.func_150255_a(linkModi);
                    extras.add(link);

                    pos = matcher.end();
                }

                TextComponentString prev = new TextComponentString(msg.substring(pos));
                prev.func_150255_a(modifier);
                extras.add(prev);
                extras.addAll(extrasOld);

                for (ITextComponent c : extras) {
                    text.func_150257_a(c);
                }
            }
        }

        List extras = component.func_150253_a();
        for (int i = 0; i < extras.size(); i++) {
            ITextComponent comp = (ITextComponent) extras.get(i);
            if (comp.func_150256_b() != null && comp.func_150256_b().func_150235_h() == null) {
                extras.set(i, fixComponent(comp, matcher));
            }
        }

        if (component instanceof TextComponentTranslation) {
            Object[] subs = ((TextComponentTranslation) component).func_150271_j();
            for (int i = 0; i < subs.length; i++) {
                Object comp = subs[i];
                if (comp instanceof ITextComponent) {
                    ITextComponent c = (ITextComponent) comp;
                    if (c.func_150256_b() != null && c.func_150256_b().func_150235_h() == null) {
                        subs[i] = fixComponent(c, matcher);
                    }
                } else if (comp instanceof String && matcher.reset((String)comp).find()) {
                    subs[i] = fixComponent(new TextComponentString((String) comp), matcher);
                }
            }
        }

        return component;
    }

    private CraftChatMessage() {
    }
}
