package net.bobnar.marketplace.data.enums;

public enum TransmissionType {
    UNKNOWN,
    MANUAL,
    AUTOMATIC;


    public static TransmissionType convertType(String s) {
        if ("Ročni menjalnik".equalsIgnoreCase(s) || "Ročni".equalsIgnoreCase(s)) {
            return MANUAL;
        } else if ("Avtomatski menjalnik".equalsIgnoreCase(s) || "Avtomatik".equalsIgnoreCase(s)) {
            return AUTOMATIC;
        }


        throw new RuntimeException("Unknown transmission type: " + s);
//        return UNKNOWN;
    }

    public String getTypeString() {
        switch (this) {
            case UNKNOWN -> {
                return "Neznano";
            }
            case MANUAL -> {
                return "Ročni";
            }
            case AUTOMATIC -> {
                return "Avtomatik";
            }
        }

        throw new RuntimeException("Switch case not covered: " + this);
    }
}
