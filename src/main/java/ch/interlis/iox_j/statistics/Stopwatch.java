package ch.interlis.iox_j.statistics;

import java.util.concurrent.TimeUnit;

public class Stopwatch {
    private long startTime;
    private long stopTime;

    public Stopwatch(){
        Reset();
    }

    public void Start(){
        if (startTime == -1){
            startTime = System.currentTimeMillis();
        }
    }

    public void Stop(){
        if (stopTime == -1 && startTime != -1){
            stopTime = System.currentTimeMillis();
        }
    }

    public void Reset(){
        startTime = -1;
        stopTime = -1;
    }

    @Override
    public String toString() {
        long duration = stopTime - startTime;
        long days       = TimeUnit.MILLISECONDS.toDays(duration);
        long hours      = TimeUnit.MILLISECONDS.toHours(duration) -
                          TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(duration));
        long minutes    = TimeUnit.MILLISECONDS.toMinutes(duration) -
                          TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(duration));
        long seconds    = TimeUnit.MILLISECONDS.toSeconds(duration) -
                          TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration));
        long millis     = TimeUnit.MILLISECONDS.toMillis(duration) -
                          TimeUnit.SECONDS.toMillis(TimeUnit.MILLISECONDS.toSeconds(duration));

        if (days == 0)
            return String.format("%02dh:%02dm:%02ds.%04dms", hours, minutes, seconds, millis);
        else
            return String.format("%ddd %02dh:%02dm:%02ds.%04dms", days, hours, minutes, seconds, millis);
    }
}
