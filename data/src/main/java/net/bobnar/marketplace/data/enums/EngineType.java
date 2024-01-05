package net.bobnar.marketplace.data.enums;

public enum EngineType {
    UNKNOWN,
    GASOLINE,
    DIESEL,
    HYBRID,
    LPG,
    ELECTRIC;

    public static EngineType convertType(String s) {
        if ("Bencin".equalsIgnoreCase(s) || "Bencinski motor".equalsIgnoreCase(s)) {
            return GASOLINE;
        } else if ("Diesel".equalsIgnoreCase(s) || "Dizel".equalsIgnoreCase(s) ||  "Diesel motor".equalsIgnoreCase(s)) {
            return DIESEL;
        } else if ("Plin".equalsIgnoreCase(s) || "LPG avtoplin".equalsIgnoreCase(s) || "CNG zemeljski plin".equalsIgnoreCase(s)) {
            return LPG;
        } else if ("Hibridni pogon".equalsIgnoreCase(s)) {
            return HYBRID;
        } else if ("Elektro pogon".equalsIgnoreCase(s)) {
            return ELECTRIC;
        }

        throw new RuntimeException("Unknown engine type: " + s);
//        return UNKNOWN;
    }

    public String getTypeString() {
        switch (this) {
            case UNKNOWN -> {
                return "Neznano";
            }
            case GASOLINE -> {
                return "Bencin";
            }
            case DIESEL -> {
                return "Diesel";
            }
            case HYBRID -> {
                return "Hibridni pogon";
            }
            case LPG -> {
                return "Plin";
            }
            case ELECTRIC -> {
                return "Električni pogon";
            }
        }

        throw new RuntimeException("Switch case not covered: " + this);
    }
}
