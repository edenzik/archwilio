package main;

import java.util.ArrayList;

/**
 * The edge on the course selection graph
 * Created by zhan on 7/30/15.
 */
public class Decision {

    private Status v;  // start vertex of the edge
    private Status u;  // end vertex of the edge
    private int w;  // weight of the edge
    private ArrayList<String> courses;  // the courses on the edge

    public Decision(Status v, Status u, int w) {
        this.v = v;
        this.u = u;
        this.w = w;
        ArrayList<String> e1 = new ArrayList<>(v.enrolled);
        ArrayList<String> e2 = new ArrayList<>(u.enrolled);
        e2.removeAll(e1);  // courses taken on the edge if the difference of u and v
        courses = new ArrayList<>(e2);
    }

    public Status from() {
        return this.v;
    }

    public Status to() {
        return this.u;
    }

    public int weight() {
        return this.w;
    }

    public ArrayList<String> courses() {
        return this.courses;
    }

    /*public String toString() {
        String s = "";
        s += ("FROM: " + this.v + Global.NEWLINE);
        s += ("TO: " + this.u + Global.NEWLINE);
        s += ("WEIGHT: " + this.w + Global.NEWLINE);
        s += ("courses chosen: " + courses + Global.NEWLINE);
        return s;
    }*/

    public String toString() {
        String s = "";
        //s += (courses + Global.NEWLINE);
        return s;
    }
}
