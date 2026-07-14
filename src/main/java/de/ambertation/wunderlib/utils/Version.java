package de.ambertation.wunderlib.utils;


import de.ambertation.wunderlib.WunderLib;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;

import java.util.Locale;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Version {
    public static final Version ZERO = new Version(0, 0, 0);

    public interface ModVersionProvider {
        Version getModVersion();
        String getModID();

        default String getNamespace() {
            return getModID();
        }
        default ResourceLocation mk(String key) {
            return ResourceLocation.fromNamespaceAndPath(getModID(), key);
        }
    }

    public final String version;

    public Version(String version) {
        if ("${version}".equals(version)) version = "0.0.0";
        this.version = version.trim();
    }

    public Version(int major, int minor, int patch) {
        this(String.format(Locale.ROOT, "%d.%d.%d", major, minor, patch));
    }

    private static int toInt(String version) {
        if (version == null || version.isEmpty()) return 0;

        try {
            final String semanticVersionPattern = "(\\d+)\\.(\\d+)(\\.(\\d+))?\\D*";
            final Matcher matcher = Pattern.compile(semanticVersionPattern).matcher(version);

            int major = 0;
            int minor = 0;
            int patch = 0;
            if (matcher.find()) {
                if (matcher.groupCount() > 0)
                    major = matcher.group(1) == null ? 0 : Integer.parseInt(matcher.group(1));
                if (matcher.groupCount() > 1)
                    minor = matcher.group(2) == null ? 0 : Integer.parseInt(matcher.group(2));
                if (matcher.groupCount() > 3)
                    patch = matcher.group(4) == null ? 0 : Integer.parseInt(matcher.group(4));
            }

            return FastColor.ARGB32.color(0, major & 0xFF, minor & 0xFF, patch & 0xFF);
        } catch (Exception e) {
            WunderLib.LOGGER.error("Failed to parse Version '" + version + "'.");
            return 0;
        }
    }

    public static int major(int version) {
        return FastColor.ARGB32.red(version);
    }

    public static int minor(int version) {
        return FastColor.ARGB32.green(version);
    }

    public static int patch(int version) {
        return FastColor.ARGB32.blue(version);
    }

    public static Version fromInt(int version) {
        return new Version(major(version), minor(version), patch(version));
    }

    public int toInt() {
        return toInt(version);
    }

    public boolean isLargerThan(Version v2) {
        return toInt() > v2.toInt();
    }

    public boolean isLargerOrEqualVersion(Version v2) {
        return toInt() >= v2.toInt();
    }

    public boolean isLargerThan(String v2) {
        return toInt() > toInt(v2);
    }

    public boolean isLargerOrEqualVersion(String v2) {
        return toInt() >= toInt(v2);
    }

    public boolean isLessThan(Version v2) {
        return toInt() < v2.toInt();
    }

    public boolean isLessOrEqualVersion(Version v2) {
        return toInt() <= v2.toInt();
    }

    public boolean isLessThan(String v2) {
        return toInt() < toInt(v2);
    }

    public boolean isLessOrEqualVersion(String v2) {
        return toInt() <= toInt(v2);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o instanceof String s) return version.equals(s.trim());
        if (!(o instanceof Version version1)) return false;
        return Objects.equals(version, version1.version);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(version);
    }

    @Override
    public String toString() {
        return version;
    }
}
