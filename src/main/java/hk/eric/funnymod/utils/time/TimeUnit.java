package hk.eric.funnymod.utils.time;

public enum TimeUnit {
    MILLISECOND(1, "ms"),
    SECOND(1000, "s"),
    MINUTE(1000 * 60, "m"),
    HOUR(1000 * 60 * 60, "h"),
    DAY(1000 * 60 * 60 * 24, "d"),
    WEEK(1000 * 60 * 60 * 24 * 7, "w"),
    MONTH(1000L * 60 * 60 * 24 * 30, "m"),
    YEAR(1000L * 60 * 60 * 24 * 365, "y");

    private final long millisecond;
    private final String abbreviation;

    TimeUnit(long millisecond, String abbreviation) {
        this.millisecond = millisecond;
        this.abbreviation = abbreviation;
    }

    public long getMillisecond() {
        return millisecond;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public long convert(long time, TimeUnit unit) {
        return time * unit.millisecond / this.millisecond;
    }

    public static TimeUnit getTimeUnit(String unit) {
        for (TimeUnit tu : TimeUnit.values()) {
            if (tu.getAbbreviation().equalsIgnoreCase(unit) || tu.name().equalsIgnoreCase(unit)) {
                return tu;
            }
        }
        return null;
    }
}