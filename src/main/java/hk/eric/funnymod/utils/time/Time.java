package hk.eric.funnymod.utils.time;

import org.apache.logging.log4j.LogManager;

public record Time(long value, TimeUnit unit) {

    public Time convert(TimeUnit unit) {
        return new Time(unit.convert(this.value, this.unit), unit);
    }

    public String toText(boolean abbreviate) {
        return String.format("%d %s", this.value, abbreviate? this.unit.getAbbreviation() : this.unit.name());
    }

    public long get(TimeUnit unit) {
        return unit.convert(this.value, this.unit);
    }

    public static Time of(long value, TimeUnit unit) {
        return new Time(value, unit);
    }

    public static Time of(String time) {
        LogManager.getLogger().info("Parsing time: \"" + time + "\"");
        String[] timeParts = time.trim().split("(?<=\\d)(?=\\D)");
        int timeValue = Integer.parseInt(timeParts[0]);
        TimeUnit timeUnit = TimeUnit.getTimeUnit(timeParts[1]);
        return new Time(timeValue, timeUnit);
    }
}
