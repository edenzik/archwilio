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

    public void removeLat() {
        path.remove(path.size() - 1);
    }

    public void add(Status status) {
        path.add(status);
    }

    /**
     * calculate the cost of the path
     */
    public void calCost() {
        Status last = path.get(path.size() - 1);
        cost = last.cost + last.left / Global.maxperSemester;
    }

    /**
     * priority queue will call this to compare paths
     * @param other path
     * @return 0 if this and other are equal, < 0 if this is "smaller" than other, > 0 if this is "bigger" than other
     */
    @Override
    public int compareTo(Path other) {
        if (other == null)
            throw new NullPointerException();
        return this.cost - other.cost;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Path)) {
            return false;
        }
        if (other == this) {
            return true;
        }
        Path other_p = (Path) other;
        if (this.path.size() != other_p.path.size()) {
            return false;
        }
        for (Status s: this.path) {
            if (!other_p.path.contains(s)) {
                return false;
            }
        }
        return true;
    }

    public String toString() {
        String s = "";
        for (Status status: this.path) {
            s += status.toString() + "\n";
        }
        return s;
    }
}
