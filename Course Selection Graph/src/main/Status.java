package main;

import input.Global;
import input.Requirement;
import java.util.*;

/**
 * The nodes on the courses selection graph
 * Created by zhan on 7/30/15.
 */
public class Status {

    public ArrayList<String> enrolled;  // list of courses the student has taken
    public ArrayList<String> options;  // list of eligible courses for the student to take in the current semester
    public Semester semester;  // current semester
    public ArrayList<Decision> adj;  // list of adjacent edges
    public boolean isGoal;  // if the vertex is a goal vertex
    public Set<Status> fathers;  // vertices leading to this vertex
    public int min;  // min # of courses the student has to take this semester in order to complete the major
    public boolean safe_checked;  // if the vertex is already checked by safe pruning stragety
    public boolean radical_checked;
    public boolean on_path_to_goal;  // if the vertex is on any path to goals

    public Status(ArrayList<String> enrolled, Semester semester) {
        this.enrolled = new ArrayList<>(enrolled);
        this.semester = new Semester(semester);
        this.options = new ArrayList<>();
        this.adj = new ArrayList<>();
        this.cal_options();
        this.isGoal = isFinished();
        this.fathers = new HashSet<>();
        this.min = Integer.MIN_VALUE;
        this.safe_checked = false;
        this.radical_checked = false;
    }

    /**
     * check if the vertex is a goal vertex
     * @return true if the vertex is a goal, false otherwise
     */
    public boolean isFinished() {
        if (this.enrolled.size() < Requirement.total_number) {
            return false;
        }
        ArrayList<String> temp = new ArrayList<>(this.enrolled);
        temp.retainAll(Requirement.group_A);
        if (temp.size() < Requirement.A_number)  // # of courses of group A taken
            return false;
        temp = new ArrayList<>(this.enrolled);
        temp.retainAll(Requirement.group_B);
        if (temp.size() < Requirement.B_number)  // # of courses of group B taken
            return false;
        temp = new ArrayList<>(this.enrolled);
        temp.retainAll(Requirement.group_C);
        if (temp.size() < Requirement.C_number)  // # of courses of group C taken
            return false;
        return true;
    }

    /**
     * how many courses left to take in order to reach the goal
     * this computation is specified to the computer science major requirement at brandeis
     * the general way to compute this is min cost max flow algorithm
     * @return the number of courses needs to take to get a major
     */
    public int left() {
        int count_rest = 0;
        int needed_a;
        int needed_b;
        int needed_c;
        int needed_rest;
        ArrayList<String> temp = new ArrayList<>(this.enrolled);
        temp.retainAll(Requirement.group_A);
        if (temp.size() >= Requirement.A_number) {
            count_rest += temp.size() - Requirement.A_number;
            needed_a = 0;
        } else {
            needed_a = Requirement.A_number - temp.size();
        }
        temp = new ArrayList<>(this.enrolled);
        temp.retainAll(Requirement.group_B);
        if (temp.size() >= Requirement.B_number) {
            count_rest += temp.size() - Requirement.B_number;
            needed_b = 0;
        } else {
            needed_b = Requirement.B_number - temp.size();
        }
        temp = new ArrayList<>(this.enrolled);
        temp.retainAll(Requirement.group_C);
        if (temp.size() >= Requirement.C_number) {
            count_rest += temp.size() - Requirement.C_number;
            needed_c = 0;
        } else {
            needed_c = Requirement.C_number - temp.size();
        }

        temp = new ArrayList<>(this.enrolled);
        temp.removeAll(Requirement.group_A);
        temp.removeAll(Requirement.group_B);
        temp.removeAll(Requirement.group_C);
        count_rest += temp.size();
        needed_rest = Requirement.total_number - Requirement.A_number - Requirement.B_number - Requirement.C_number - count_rest;
        needed_rest = needed_rest > 0?needed_rest:0;
        return needed_a + needed_b + needed_c + needed_rest;
    }

    /**
     * computer the list of eligible courses for the student to take this semester
     */
    public void cal_options() {
        ArrayList<String> list = new ArrayList<>(Global.all_courses);
        list.removeAll(this.enrolled);  // remove enrolled courses from all courses
        HashSet<String> pointed_to = new HashSet<>();  // nodes pointed by other nodes
        for (String c: list) {
            for (int w: Global.prerequisite.adj(Global.course_to_idx.get(c))) {
                String tmp = Global.idx_to_course.get(w);  // tmp is pointed by c, meaning its prereq is not satisfied
                pointed_to.add(tmp);
            }
        }
        list.removeAll(pointed_to);  // remove all courses whose prereq has not been satisfied
        for (String l: list) {
            // check if the course is offered this semester
            if (Global.schedule.get(Global.semester_to_idx.get(this.semester.toString()), Global.course_to_idx.get(l))) {
                if (!this.options.contains(l))
                    this.options.add(l);
            }
        }
    }

    /**
     * computer all course combination with size up to Global.maxperSemester out of this.options
     * @return all course combination
     */
    public Set<List<String>> cal_adj() {
        ArrayList<Integer> input = new ArrayList<>();
        for (int i = 0; i < this.options.size(); i++) {
            input.add(i);
        }
        Permutations<Integer> obj = new Permutations<>();
        Collection<List<Integer>> output = obj.permute(input);
        Set<List<String>> permutation = new HashSet<>();
        Set<List<String>> pnr;
        // computer all permutations with size from 1 to Global.maxperSemester
        for (int i = Math.max(0, input.size()-Global.maxperSemester); i < input.size(); i++) {
            pnr = new HashSet<>();
            for(List<Integer> integers : output){
                List<Integer> l = new ArrayList<> (integers.subList(i, integers.size()));
                Collections.sort(l);
                List<String> cs = new ArrayList<>();
                for (int t: l) {
                    cs.add(this.options.get(t));
                }
                pnr.add(cs);
                permutation.add(cs);
            }
        }
        return permutation;
    }

    @Override
    public int hashCode() {
        Collections.sort(enrolled);
        int result = enrolled.hashCode();
        Collections.sort(options);
        result = 31 * result + options.hashCode();
        result = 31 * result + semester.hashCode();
        return result;
    }

    /**
     * if two status are in the same semester, and have same courses enrolled, then they are equal
     * @param obj
     * @return true if two status are equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Status))
            return false;
        if (obj == this)
            return true;
        Status s = (Status) obj;
        if (!this.semester.equals(s.semester))
            return false;
        if (this.enrolled.size() != s.enrolled.size())
            return false;
        ArrayList<String> tmp = new ArrayList<>(this.enrolled);
        tmp.removeAll(s.enrolled);
        return tmp.size() == 0;
    }

    public String toString() {
        String s = Global.NEWLINE;
        s += ("enrolled: " + this.enrolled + "\n");
        s += ("option: " + this.options + "\n");
        s += ("semester: " + this.semester);
        return s;
    }

    public void display() {
        System.out.println("enrolled:");
        System.out.println(enrolled);
        System.out.println("options:");
        System.out.println(options);
        System.out.println("semster");
        System.out.println(semester.term() + semester.year());
    }
}
