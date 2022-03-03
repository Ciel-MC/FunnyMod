package hk.eric.funnymod.utils.time;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TimeTest {
    @Test
    public void test() {
        String time = "1WEEK";
        assertEquals(Time.of(time), Time.of(1, TimeUnit.WEEK));
    }

    @Test
    public void test2() {
        String time = "1DAY";
        assertEquals(Time.of(time), Time.of(1, TimeUnit.DAY));
    }
}