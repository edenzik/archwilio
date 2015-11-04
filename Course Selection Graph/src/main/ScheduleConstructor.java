package main;

import input.Schedule;

/**
 * Read schedule from file, initialize Schedule object
 * Created by zhan on 7/28/15.
 */
public class ScheduleConstructor {

    public ScheduleConstructor(Schedule schedule, String lines) {
        int i = 0;
        for (String line : lines.split("\n")) {
            String[] tokens = line.split("\\s+");
            for (int j = 0; j < tokens.length; j++) {
                if (tokens[j].equals("1")) {
                    schedule.set(i, j);
                }
            }
            i += 1;
        }
    }

    public static void main(String[] args) {
        Schedule schedule = new Schedule(10, 17);
        new ScheduleConstructor(schedule, args[0]);
        schedule.display();
    }
}
