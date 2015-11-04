package main;

import input.Global;
import java.util.*;

/**
 * Created by zhan on 7/30/15.
 */
public class CourseSelectionGraph {

    /**
     * completion time pruning strategy
     * @param node to be checked node
     * @return true if node should be pruned, false otherwise
     */
    public boolean time_pruning(Status node) {
        node.safe_checked = true;
        node.min = node.left() - (Global.maxperSemester * (Global.deadline.subtract(node.semester) - 1));
        if (node.min > Math.min(node.options.size(), Global.maxperSemester)) {
            Global.to_be_pruned.add(node);
            return true;
        }
        return false;
    }

    public void constructGraph(Status source) {
        System.out.println("start building course selection graph from semester " + Global.start + " to semester " + Global.deadline + "...");
        Queue<Status> q = new LinkedList<>();
        q.offer(source);
        Global.constructed_nodes.add(source);
        while (!q.isEmpty()) {
            Status cur = q.poll();

            // if major requirement is satisfied, stop
            if (cur.isGoal) {
                continue;
            }

            // if reach deadline, stop
            if (cur.semester.equals(Global.deadline)) {
                Global.to_be_pruned.add(cur);  // prune unwanted nodes
                continue;
            }

            // completion time pruning
            if (Global.safe_pruning && !cur.safe_checked) {
                if (time_pruning(cur))
                    continue;
            }

            Set<List<String>> permutations = cur.cal_adj();  // compute all courses combinations
            if (permutations.size() == 0) {  // no courses eligible to take in the current semester
                Status next = new Status(cur.enrolled, cur.semester.next_semester());
                Decision decision;
                if (Global.existing_node.containsKey(next.hashCode())) {
                    Status node = Global.existing_node.get(next.hashCode());
                    decision = new Decision(cur, node, 1);
                    node.fathers.add(cur);
                } else {
                    Global.existing_node.put(next.hashCode(), next);
                    q.offer(next);
                    Global.constructed_nodes.add(next);
                    decision = new Decision(cur, next, 1);
                    next.fathers.add(cur);
                }
                cur.adj.add(decision);
            }
            for (List<String> list : permutations) {
                // this semester has to take cur.min courses
                if ((Global.safe_pruning || Global.radical_pruning) && list.size() < cur.min)
                    continue;
                ArrayList<String> enrolled = new ArrayList<>(cur.enrolled);
                enrolled.addAll(list);  // the student takes all courses in the list
                Semester semester = cur.semester.next_semester();
                Status next = new Status(enrolled, semester);  // create next status
                Decision decision;
                // check if next status is already created
                if (Global.existing_node.containsKey(next.hashCode())) {
                    Status node = Global.existing_node.get(next.hashCode());
                    decision = new Decision(cur, node, 1);
                    node.fathers.add(cur);
                } else {
                    Global.existing_node.put(next.hashCode(), next);
                    q.offer(next);
                    Global.constructed_nodes.add(next);
                    decision = new Decision(cur, next, 1);
                    next.fathers.add(cur);
                }
                cur.adj.add(decision);
            }
        }
    }

    public void displayGraph(Status source) {
        for (Decision d : source.adj) {
            System.out.println(d);
            this.displayGraph(d.to());
        }
    }

    public static void main(String[] args) {
        Status source = new Status(new ArrayList<>(), new Semester(10, "Fall"));
    }

}
