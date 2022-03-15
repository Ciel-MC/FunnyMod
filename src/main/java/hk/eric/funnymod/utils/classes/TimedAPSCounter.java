package hk.eric.funnymod.utils.classes;

public class TimedAPSCounter {

    private long msBetweenAttack;

    private long lastAttackTime = -1;

    public TimedAPSCounter(int APS) {
        msBetweenAttack = 1000 / APS;
    }

    public TimedAPSCounter() {
    }

    public boolean canAttack() {
        long now = System.currentTimeMillis();
        if (now - lastAttackTime >= msBetweenAttack) {
            lastAttackTime = now;
            return true;
        }
        return false;
    }

    public void reset() {
        lastAttackTime = System.currentTimeMillis();
    }

    public void setAPS(int APS) {
        msBetweenAttack = 1000 / APS;
    }

    public int getAPS() {
        return (int) (1000 / msBetweenAttack);
    }
}
