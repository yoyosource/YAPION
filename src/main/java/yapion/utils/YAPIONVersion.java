package yapion.utils;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public enum YAPIONVersion {

    V1(1, "reference.id.md5"),
    V2(2, "reference.id.own");

    public static final YAPIONVersion latest = V2;

    private int versionNumber;
    private Set<String> options;

    YAPIONVersion(int versionNumber, final String... options) {
        this.versionNumber = versionNumber;
        this.options = Arrays.stream(options).collect(Collectors.toSet());
    }

    public int version() {
        return versionNumber;
    }

    public boolean hasOption(String option) {
        return options.contains(option);
    }

}
