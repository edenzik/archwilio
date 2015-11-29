package main;

import input.Global;
import input.Requirement;

import java.util.*;

/**
 * Created by zhan on 7/30/15.
 */
public class CourseSelectionGraph {

    /**
     * check if there are enough eligible courses from topic left to satisfy requirement of topic
     * @param node current status
     * @param group the list of courses in the checked topic
     * @param left # semesters left
     * @return true if there are enough courses left, false otherwise
     */
    public boolean topic_availability(Status node, ArrayList<String> group, int left) {
        if (left == 0) {
            return false;
        }
        HashSet<String> offered = new HashSet<>();  // # courses offered in the remaining semesters
        ArrayList<String> unenrolled = new ArrayList<>(Global.all_courses);
        unenrolled.removeAll(node.enrolled);
        Semester cur = node.semester;
        // add offered unenrolled course in each remaining semester
        while (!cur.equals(Global.deadline.next_semester())) {
            int s_idx = Global.semester_to_idx.get(cur.toString());
            for (String c : unenrolled) {
                if (offered.contains(c)) {
                    continue;
                }
                if (!group.contains(c)) {
                    continue;
                }
                int c_idx = Global.course_to_idx.get(c);
                if (Global.schedule.get(s_idx, c_idx)) {
                    offered.add(c);
                }
            }
            cur = cur.next_semester();
        }

        HashSet<String> reachable = new HashSet<>();  // # courses reachable in prereq graph
        ArrayList<String> enrolled_copy = new ArrayList<>(node.enrolled);
        int time = Global.deadline.subtract(node.semester);

        for (int i = 0; i < time; i++) {
            unenrolled.clear();
            unenrolled.addAll(Global.all_courses);
            unenrolled.removeAll(enrolled_copy);  // remove enrolled courses from all courses
            HashSet<String> pointed_to = new HashSet<>();  // nodes pointed by other nodes
            for (String c : unenrolled) {
                for (int w : Global.prerequisite.adj(Global.course_to_idx.get(c))) {
                    String tmp = Global.idx_to_course.get(w);  // tmp is pointed by c, meaning its prereq is not satisfied
                    pointed_to.add(tmp);
                }
            }
            unenrolled.removeAll(pointed_to);  // remove all courses whose prereq has not been satisfied
            reachable.addAll(unenrolled);
            enrolled_copy.addAll(reachable);
        }
        offered.retainAll(reachable);
        offered.retainAll(group);
        return offered.size() < left;
    }

    /**
     * courses availability pruning strategy
     * @param node to be checked node
     * @return true if node should be pruned, false otherwise
     */
    public boolean availability_pruning(Status node) {
        node.availability_checked = true;  // mark this pruning strategy has been applied to node
        boolean group_A = topic_availability(node, Requirement.group_A, node.left_A);
        boolean group_B = topic_availability(node, Requirement.group_B, node.left_B);
        boolean group_C = topic_availability(node, Requirement.group_C, node.left_C);
        boolean whole = topic_availability(node, Global.all_courses, node.left);
        // if any topic cannot make it, prune
        if (group_A || group_B || group_C || whole) {
            Global.to_be_pruned.add(node);
            return true;
        }
        return false;
    }


    /**
     * completion time pruning strategy
     *
     * @param node to be checked node
     * @return true if node should be pruned, false otherwise
     */
    public boolean time_pruning(Status node) {
        node.time_checked = true;
        node.min = node.left - (Global.maxperSemester * (Global.deadline.subtract(node.semester) - 1));
        if (node.min > Math.min(node.options.size(), Global.maxperSemester)) {
            Global.to_be_pruned.add(node);
            return true;
        }
        return false;
    }

    /**
     * create a new edge
     * @param next new vertex
     * @param cur current vertex
     * @param cur_p current path
     * @param pathPriorityQueue queue which stores top k path
     * @return the new edge
     */
    public Decision makeDecision(Status next, Status cur, Path cur_p, Queue<Path> pathPriorityQueue) {
        Decision decision;
        if (Global.existing_node.containsKey(next.hashCode())) {
            Status node = Global.existing_node.get(next.hashCode());
            decision = new Decision(cur, node, 1);
            node.fathers.add(cur);
            Path next_p = new Path(cur_p);
            next_p.add(node);
            next_p.calCost();
            pathPriorityQueue.offer(next_p);
        } else {
            Global.existing_node.put(next.hashCode(), next);
            //q.offer(next);
            Global.constructed_nodes.add(next);
            decision = new Decision(cur, next, 1);
            next.fathers.add(cur);
            Path next_p = new Path(cur_p);
            next_p.add(next);
            next_p.calCost();
            pathPriorityQueue.offer(next_p);
        }
        return decision;
    }

    public Decision makeDecision(Status next, Status cur, Queue<Status> q) {
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
        return decision;
    }

    /**
     * construct shortest paths
     * @param source start status
     */
    public void constructShortest(Status source) {
        System.out.println("start searching for top " + Global.k + " shortest learning paths from semester " + Global.start + " to semester " + Global.deadline + "...");

        Queue<Path> pathPriorityQueue = new PriorityQueue<>();
        Path p = new Path();
        p.add(source);
        p.calCost();
        pathPriorityQueue.offer(p);
        while (!pathPriorityQueue.isEmpty()) {
            Path cur_p = pathPriorityQueue.poll();
            Status cur = cur_p.getLast();

            // if major requirement is satisfied, stop
            if (cur.isGoal) {
                Global.top_k.add(cur_p);
                if (Global.top_k.size() == Global.k) {
                    System.out.println("top " + Global.k + " paths are found!");
                    return;
                }
                continue;
            }

            // if reach deadline, stop
            if (cur.semester.equals(Global.deadline)) {
                continue;
            }

            // completion time pruning
            if (Global.time_pruning && !cur.time_checked) {
                if (time_pruning(cur)) {
                    continue;
                }
            }

            // courses availability pruning
            if (Global.availability_pruning && !cur.availability_checked) {
                if (availability_pruning(cur)) {
                    continue;
                }
            }

            Set<List<String>> permutations = cur.cal_adj();  // compute all courses combinations
            if (permutations.size() == 0) {  // no courses eligible to take in the current semester
                Status next = new Status(cur.enrolled, cur.semester.next_semester());
                Decision decision = makeDecision(next, cur, cur_p, pathPriorityQueue);
                cur.adj.add(decision);
            }
            for (List<String> list : permutations) {
                // this semester has to take cur.min courses
                if ((Global.time_pruning) && list.size() < cur.min)
                    continue;
                ArrayList<String> enrolled = new ArrayList<>(cur.enrolled);
                enrolled.addAll(list);  // the student takes all courses in the list
                Semester semester = cur.semester.next_semester();
                Status next = new Status(enrolled, semester);  // create next status
                Decision decision = makeDecision(next, cur, cur_p, pathPriorityQueue);
                cur.adj.add(decision);
            }
        }
    }

    /**
     * construct the course selection graph
     * @param source start status
     */
    public void constructGraph(Status source) {
        System.out.println("start constructing graph from semester " + Global.start + " to semester " + Global.deadline + "...");
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
            if (Global.time_pruning && !cur.time_checked) {
                if (time_pruning(cur)) {
                    Global.time_pruned++;
                    continue;
                }
            }

            // courses availability pruning
            if (Global.availability_pruning && !cur.availability_checked) {
                if (availability_pruning(cur)) {
                    Global.ava_pruned++;
                    continue;
                }
            }

            Set<List<String>> permutations = cur.cal_adj();  // compute all courses combinations
            if (permutations.size() == 0) {  // no courses eligible to take in the current semester
                Status next = new Status(cur.enrolled, cur.semester.next_semester());
                Decision decision = makeDecision(next, cur, q);
                cur.adj.add(decision);
            }
            for (List<String> list : permutations) {
                // this semester has to take cur.min courses
                if ((Global.time_pruning) && list.size() < cur.min)
                    continue;
                ArrayList<String> enrolled = new ArrayList<>(cur.enrolled);
                enrolled.addAll(list);  // the student takes all courses in the list
                Semester semester = cur.semester.next_semester();
                Status next = new Status(enrolled, semester);  // create next status
                Decision decision = makeDecision(next, cur, q);
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
