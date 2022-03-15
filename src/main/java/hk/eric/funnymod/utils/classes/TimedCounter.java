package hk.eric.funnymod.utils.classes;

import java.util.Timer;
import java.util.TimerTask;

public class TimedCounter {
    private final Timer timer = new Timer();
    private int count;
    private final long delay;

    public TimedCounter(long delay) {
        this.delay = delay;
    }

    public void count() {
        this.count++;
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                count--;
            }
        }, this.delay);
    }

    public int getCount() {
        return this.count;
    }
}
