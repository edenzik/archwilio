package main;

import input.Global;
import input.Requirement;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Queue;

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
    public static void count_path(Status node, Path p, Queue<Path> paths) {
        if (node == null) {
            return;
        }
        if (node.isGoal) {
            total_path += 1;
            p.calCost();
            paths.add(new Path(p));
            return;
        }
        for (Decision d : node.adj) {
            p.add(d.to());
            count_path(d.to(), p, paths);
            p.removeLat();
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
        System.out.println("Note: test with start semester in 2010 or later (to change, go to config.json)");
        String configFileName = args[0];
        BufferedReader br = new BufferedReader(new FileReader(configFileName));
        JSONTokener token = new JSONTokener(br);
        JSONObject config = new JSONObject(token);

        String configFileName1 = args[1];
        BufferedReader br1 = new BufferedReader(new FileReader(configFileName1));
        JSONTokener token1 = new JSONTokener(br1);
        JSONObject requirement = new JSONObject(token1);

        new Global(config);
        new Requirement(requirement);

        Status source = new Status(new ArrayList<>(), Global.start);

        CourseSelectionGraph g = new CourseSelectionGraph();
        long start = System.currentTimeMillis();
        g.constructGraph(source);  // graph construction happens here
        System.out.println("searching done");

        /*System.out.println("# to be pruned nodes: " + Global.to_be_pruned.size());
        System.out.println("time pruned: " + Global.time_pruned);
        System.out.println("ava pruned: " + Global.ava_pruned);*/
        clean_up();  // delete unwanted nodes and edges
        System.out.println("clean up done");

        Queue<Path> paths = Global.allPaths;
        Path p = new Path();
        p.add(source);
        count_path(source, p, paths);
        long end = System.currentTimeMillis();
        System.out.println("time for whole graph construction : " + (end - start) + "ms");
        System.out.println("number of all nodes: " + Global.constructed_nodes.size());
        System.out.println("number of all paths: " + Global.allPaths.size());
        System.out.println("------");

        start = System.currentTimeMillis();
        g.constructShortest(source);
        end = System.currentTimeMillis();
        System.out.println("time for constructing shortest " + Global.k + " paths : " + (end - start) + "ms");

        int count = 1;
        for (Path pt: Global.top_k) {
            System.out.println("Path " + count + ": ");
            System.out.println(pt);
        }

    }
}
