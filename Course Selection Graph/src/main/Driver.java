package main;

import input.Global;
import input.Requirement;
import org.json.JSONObject;
import org.json.JSONTokener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

/**
 * Created by zhan on 7/30/15.
 */
public class Driver {
    public static int total_path = 0;
    private static boolean on_path_to_goal = false;

    /**
     * delete paths that do not lead to goals
     */
    public static void clean_up() {
        System.out.println("start cleaning up...");
        while (!Global.to_be_pruned.isEmpty()) {
            Status node = Global.to_be_pruned.poll();
            Global.constructed_nodes.remove(node);
            for (Status dad : node.fathers) {
                ArrayList<Decision> copy = new ArrayList<>(dad.adj);
                for (Decision d : copy) {
                    if (d.to().equals(node)) {
                        dad.adj.remove(d);  // delete any incoming edges to node
                        break;
                    }
                }
                dfs(dad);  // check if dad is on path to goal
                if (!on_path_to_goal) {
                    Global.to_be_pruned.offer(dad);
                } else {
                    dad.on_path_to_goal = true;
                }
                on_path_to_goal = false;
            }
        }
    }

    /**
     * count total number of paths
     * @param node
     */
    public static void count_path(Status node) {
        if (node == null) {
            return;
        }
        if (node.isGoal) {
            total_path += 1;
            return;
        }
        for (Decision d : node.adj) {
            count_path(d.to());
        }
    }

    /**
     * check if a node is on any path to goal
     * @param node
     */
    public static void dfs(Status node) {
        if (node == null) {
            return;
        }
        if (node.isGoal) {
            on_path_to_goal = true;
            return;
        }
        for (Decision d : node.adj) {
            dfs(d.to());
            if (on_path_to_goal) {
                break;
            }
        }
    }

    public static void main(String[] args) throws Exception {
        System.out.println("usage: Java Driver config.json requirement.json");
        System.out.println("Note: test with start semester in 2010 or later (modify in config.json)");
        long start = System.currentTimeMillis();
        String configFileName = args[0];
        BufferedReader br = new BufferedReader(new FileReader(configFileName));
        JSONTokener tokener = new JSONTokener(br);
        JSONObject config = new JSONObject(tokener);

        String configFileName1 = args[1];
        BufferedReader br1 = new BufferedReader(new FileReader(configFileName1));
        JSONTokener tokener1 = new JSONTokener(br1);
        JSONObject requirement = new JSONObject(tokener1);

        new Global(config);
        new Requirement(requirement);

        Status source = new Status(new ArrayList<>(), Global.start);

        CourseSelectionGraph g = new CourseSelectionGraph();

        g.constructGraph(source);  // graph construction happens here

        System.out.println("construction done");
        clean_up();  // delete unwanted nodes and edges
        System.out.println("clean up done");
        long end = System.currentTimeMillis();
        System.out.println("running time: " + (end-start)/1000 + " seconds");

        count_path(source);
        System.out.println("total path: " + total_path);
    }
}
