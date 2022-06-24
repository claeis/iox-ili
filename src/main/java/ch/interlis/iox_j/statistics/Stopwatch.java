package ch.interlis.iox_j.statistics;

import java.util.concurrent.TimeUnit;

/**
 * Stopwatch allows measuring time between the calls of Start() and Stop(). Reset() does reset the measured time to 0.
 * The Stopwatch is not threadsafe
 */
public class Stopwatch {
    private long startTime;
    private long elapsedTime;

    public Stopwatch(){
        Reset();
    }

    /**
     * Mark the beginning of the time measuring period if not already running.
     */
    public void Start(){
        if (startTime == -1){
            startTime = System.currentTimeMillis();
        }
    }

    /**
     * Mark the end of the time measuring period if beginning is marked.
     */
    public void Stop(){
        if (startTime != -1){
            elapsedTime += System.currentTimeMillis() - startTime;
            startTime = -1;
        }
    }

    /**
     * @return Total running time in ms.
     */
    public long getElapsedTime() {
        long runningTimeMs = startTime == -1 ? 0 : System.currentTimeMillis() - startTime;
        return elapsedTime + runningTimeMs;
    }

    /**
     * Reset the clock to 0ms. The Stopwatch is stopped after call to Reset.
     */
    public void Reset(){
        startTime = -1;
        elapsedTime = 0;
    }

    @Override
    public String toString() {
        long days       = TimeUnit.MILLISECONDS.toDays(elapsedTime);
        long hours      = TimeUnit.MILLISECONDS.toHours(elapsedTime) -
                          TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(elapsedTime));
        long minutes    = TimeUnit.MILLISECONDS.toMinutes(elapsedTime) -
                          TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(elapsedTime));
        long seconds    = TimeUnit.MILLISECONDS.toSeconds(elapsedTime) -
                          TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(elapsedTime));
        long millis     = TimeUnit.MILLISECONDS.toMillis(elapsedTime) -
                          TimeUnit.SECONDS.toMillis(TimeUnit.MILLISECONDS.toSeconds(elapsedTime));

        if (days == 0)
            return String.format("%02dh:%02dm:%02ds.%04dms", hours, minutes, seconds, millis);
        else
            return String.format("%ddd %02dh:%02dm:%02ds.%04dms", days, hours, minutes, seconds, millis);
    }
}
