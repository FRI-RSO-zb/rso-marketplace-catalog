package net.bobnar.marketplace.data.enums;

public enum BodyType {
    UNKNOWN,
    HATCHBACK, // Kombilimuzina
    COUPE,
    CABRIO,
    PICKUP,
    SEDAN, // Limuzina
    WAGON, // Karavan
    SUV,
    MINIVAN; // Enoprostorec

    public static BodyType convertType(String s) {
        if ("Hatchback".equalsIgnoreCase(s) || "Kombilimuzina".equalsIgnoreCase(s)) {
            return HATCHBACK;
        } else if ("Coupe".equalsIgnoreCase(s)) {
            return COUPE;
        } else if ("Cabrio".equalsIgnoreCase(s) || "Kabriolet".equalsIgnoreCase(s)) {
            return CABRIO;
        } else if ("Pickup".equalsIgnoreCase(s) || "Pick-up".equalsIgnoreCase(s) || "Pick up".equalsIgnoreCase(s)) {
            return PICKUP;
        } else if ("Sedan".equalsIgnoreCase(s) || "Limuzina".equalsIgnoreCase(s)) {
            return SEDAN;
        } else if ("Wagon".equalsIgnoreCase(s) || "Karavan".equalsIgnoreCase(s)) {
            return WAGON;
        } else if ("SUV".equalsIgnoreCase(s)) {
            return SUV;
        } else if ("Minivan".equalsIgnoreCase(s) || "Enoprostorec".equalsIgnoreCase(s)) {
            return MINIVAN;
        }


        throw new RuntimeException("Unknown body type: " + s);
//        return UNKNOWN;
    }

    public String getTypeString() {
        switch (this) {
            case UNKNOWN -> {
                return "Neznano";
            }
            case HATCHBACK -> {
                return "Kombilimuzina";
            }
            case COUPE -> {
                return "Coupe";
            }
            case CABRIO -> {
                return "Kabriolet";
            }
            case PICKUP -> {
                return "Pickup";
            }
            case SEDAN -> {
                return "Limuzina";
            }
            case WAGON -> {
                return "Karavan";
            }
            case SUV -> {
                return "SUV";
            }
            case MINIVAN -> {
                return "Enoprostorec";
            }
        }

        throw new RuntimeException("Switch case not covered: " + this);
    }
}
