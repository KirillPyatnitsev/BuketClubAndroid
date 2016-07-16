package ru.creators.buket.club.tools;

/**
 * Created by user on 16.07.2016.
 */
public class Stopwatch {

    private long start;

    public Stopwatch() {
        this.start = System.currentTimeMillis();
    }

    public long getMillis() {
        return System.currentTimeMillis() - start;
    }

    public String getMillisString() {
        return getMillis() + "ms";
    }

    @Override
    public String toString() {
        return getMillisString();
    }
}
