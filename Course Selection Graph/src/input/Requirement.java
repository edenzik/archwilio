package input;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;

/**
 * Created by zhan on 10/19/15.
 */
public class Requirement {
    public static ArrayList<String> group_A;  // a list of courses belonging to group A
    public static ArrayList<String> group_B;
    public static ArrayList<String> group_C;
    public static int A_number;  // # of courses from group A required to take
    public static int B_number;
    public static int C_number;
    public static int total_number;  // total number of courses required to take


    /**
     * initialize the requirement from requirement json file
     * @param requirement json file
     * @throws Exception
     */
    public Requirement(JSONObject requirement) throws Exception{
        group_A = new ArrayList<>();
        group_B = new ArrayList<>();
        group_C = new ArrayList<>();
        JSONArray a = requirement.getJSONArray("group_A");
        JSONArray b = requirement.getJSONArray("group_B");
        JSONArray c = requirement.getJSONArray("group_C");
        A_number = requirement.getInt("A_number");
        B_number = requirement.getInt("B_number");
        C_number = requirement.getInt("C_number");
        total_number = requirement.getInt("total_number");

        for (int i = 0; i < a.length(); i++) {
            group_A.add(a.getString(i));
        }
        for (int i = 0; i < b.length(); i++) {
            group_B.add(b.getString(i));
        }
        for (int i = 0; i < c.length(); i++) {
            group_C.add(c.getString(i));
        }
    }
}
