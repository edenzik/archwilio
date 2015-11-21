package main;

import input.Global;
import java.util.ArrayList;

/**
 * Created by zhan on 11/6/15.
 */
public class Path implements Comparable<Path> {
    private ArrayList<Status> path;
    private int cost;

    public Path() {
        path = new ArrayList<>();
        cost = 0;
    }

    public Path(Path p) {
        path = new ArrayList<>(p.path);
        cost = p.cost;
    }

    public Status getLast() {
        return path.get(path.size() - 1);
    }

    public void add(Status status) {
        path.add(status);
    }

    public void cal_cost() {
        Status last = path.get(path.size() - 1);
        cost = last.cost + last.left / Global.maxperSemester;
    }

    @Override
    public int compareTo(Path other) {
        if (other == null)
            throw new NullPointerException();
        return this.cost - other.cost;
    }

    public String toString() {
        String s = "";
        for (Status status: this.path) {
            s += status.toString() + "\n";
        }
        return s;
    }
}
