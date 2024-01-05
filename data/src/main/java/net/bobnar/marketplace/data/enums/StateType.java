package net.bobnar.marketplace.data.enums;

public enum StateType {
    UNKNOWN,
    NEW,
    TEST,
    USED,
    BROKEN;


    public static StateType convertType(String s) {
        if ("NOVO".equalsIgnoreCase(s) || "Novo vozilo".equalsIgnoreCase(s)) {
            return NEW;
        } else if ("Rabljeno".equalsIgnoreCase(s)) {
            return USED;
        } else if ("Testno".equalsIgnoreCase(s)) {
            return TEST;
        } else if ("V okvari".equalsIgnoreCase(s) || "Karambolirano".equalsIgnoreCase(s)) {
            return BROKEN;
        }

        throw new RuntimeException("Unknown state type: " + s);
//        return UNKNOWN;
    }

    public String getTypeString() {
        switch (this) {
            case UNKNOWN -> {
                return "Neznano";
            }
            case NEW -> {
                return "Novo";
            }
            case TEST -> {
                return "Testno";
            }
            case USED -> {
                return "Rabljeno";
            }
            case BROKEN -> {
                return "V okvari";
            }
        }

        throw new RuntimeException("Switch case not covered: " + this);
    }
}
