package hk.eric.funnymod.event;

public class CancellableEvent extends Event{
    private boolean cancelled = false;

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public CancellableEvent call() {
        return super.call();
    }


}
