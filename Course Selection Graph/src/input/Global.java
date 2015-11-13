package input;

import main.*;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.*;

/**
 * Created by zhan on 7/30/15.
 */
public class Global {

    public static final String NEWLINE = System.getProperty("line.separator");
    public static ArrayList<String> all_courses;
    public static ArrayList<Decision> all_decisions;
    public static Prerequisite prerequisite;
    public static Schedule schedule;
    public static HashSet<Status> constructed_nodes;
    public static HashMap<Integer, Status> existing_node;  // store constructed nodes

    public static Queue<Status> to_be_pruned;

    public static HashMap<String, Integer> semester_to_idx;  // map a semester to index
    public static HashMap<String, Integer> course_to_idx;  // map a course to index
    public static HashMap<Integer, String> idx_to_semester;  // map a index to corresponding semester
    public static HashMap<Integer, String> idx_to_course;  // map a index to corresponding course

    public static HashMap<String, ArrayList<String>> courses_semesters;  // course and offered semesters
    public static HashMap<String, ArrayList<String>> courses_prereq;  // course and its prerequisites

    public static int maxperSemester;  // max # courses students can/want to take per semester

    public static Semester start;  // start semester
    public static Semester deadline;  // graduation semester

    public static boolean time_pruning;  // switch of completion time pruning strategy
    public static boolean availability_pruning;  // switch of course availability pruning strategy
    public static boolean radical_pruning;

    public static int time_pruned;
    public static int ava_pruned;

    public static ArrayList<Path> top_k;  // top k paths
    public static int k;  // the k in top k

    public Global(JSONObject config) throws Exception {
        time_pruned = 0;
        ava_pruned = 0;
        all_courses = new ArrayList<>();
        all_decisions = new ArrayList<>();
        constructed_nodes = new HashSet<>();
        to_be_pruned = new LinkedList<>();
        existing_node = new HashMap<>();
        top_k = new ArrayList<>();

        JSONObject edu = config.getJSONObject("education");
        JSONObject algo = config.getJSONObject("algorithm");

        k = algo.getInt("k");

        start = new Semester(edu.getInt("start_year"), edu.getString("start_term"));
        deadline = new Semester(edu.getInt("end_year"), edu.getString("end_term"));

        time_pruning = algo.getBoolean("time_pruning");
        availability_pruning = algo.getBoolean("availability_pruning");
        radical_pruning = algo.getBoolean("radical_pruning");

        JSONArray courses_info = config.getJSONArray("courses");

        maxperSemester = edu.getInt("max_courses_per_semester");
        semester_to_idx = new HashMap<>();
        course_to_idx = new HashMap<>();
        idx_to_course = new HashMap<>();
        idx_to_semester = new HashMap<>();

        courses_prereq = new HashMap<>();
        courses_semesters = new HashMap<>();

        // initialize course/offering semesters and course/prerequisites
        for (int i = 0; i < courses_info.length(); i++) {
            JSONObject c = courses_info.getJSONObject(i);
            String code = c.getString("code");
            JSONArray prereq = c.getJSONArray("prerequisite");
            JSONArray term = c.getJSONArray("term");
            ArrayList<String> prereq_list = new ArrayList<>();
            ArrayList<String> terms = new ArrayList<>();
            for (int j = 0; j < prereq.length(); j++) {
                prereq_list.add(prereq.get(j).toString());
            }
            for (int j = 0; j < term.length(); j++) {
                terms.add(term.get(j).toString());
            }
            courses_semesters.put(code, terms);
            courses_prereq.put(code, prereq_list);
        }

        // all offered courses
        for (String c: courses_prereq.keySet()) {
            all_courses.add(c);
        }

        // map semesters to integer index
        int idx = 0;
        for (ArrayList<String> l : courses_semesters.values()) {
            for (String s : l) {
                if (!semester_to_idx.keySet().contains(s)) {
                    semester_to_idx.put(s, idx);
                    idx_to_semester.put(idx, s);
                    idx++;
                }
            }
        }

        // map courses to integer index
        idx = 0;
        for (String c : courses_prereq.keySet()) {
            if (!course_to_idx.containsKey(c)) {
                course_to_idx.put(c, idx);
                idx_to_course.put(idx, c);
                idx++;
            }
        }

        // initialize prerequisites graph
        String graph = "";
        graph += course_to_idx.size() + " ";
        idx = 0;
        for (ArrayList<String> l : courses_prereq.values()) {
            idx += l.size();
        }
        graph += idx + " ";
        for (Map.Entry<String, ArrayList<String>> entry : courses_prereq.entrySet()) {
            String key = entry.getKey();
            ArrayList<String> value = entry.getValue();
            int c_i = course_to_idx.get(key);
            for (String v : value) {
                graph += course_to_idx.get(v) + " " + c_i + " ";
            }
        }
        prerequisite = new Prerequisite(graph);  // construct prerequisite graph

        // initialize schedule matrix
        schedule = new Schedule(course_to_idx.size(), semester_to_idx.size());
        String matrix = "";
        for (int i = 0; i < semester_to_idx.size(); i++) {
            for (int j = 0; j < course_to_idx.size(); j++) {
                if (courses_semesters.get(idx_to_course.get(j)).contains(idx_to_semester.get(i))) {
                    matrix += "1 ";
                } else {
                    matrix += "0 ";
                }
            }
            matrix += "\n";
        }

        new ScheduleConstructor(schedule, matrix);  // construct schedule
    }

}
