package main;

import java.util.*;

/**
 * Created by zhan on 7/30/15.
 */
public class Permutations<T> {
    /**
     * compute the permutation of the input
     * @param input
     * @return
     */
    public Collection<List<T>> permute(Collection<T> input) {
        Collection<List<T>> output = new ArrayList<>();
        if (input.isEmpty()) {
            output.add(new ArrayList<>());
            return output;
        }
        List<T> list = new ArrayList<>(input);
        T head = list.get(0);
        List<T> rest = list.subList(1, list.size());
        for (List<T> permutations : permute(rest)) {
            List<List<T>> subLists = new ArrayList<>();
            for (int i = 0; i <= permutations.size(); i++) {
                List<T> subList = new ArrayList<>();
                subList.addAll(permutations);
                subList.add(i, head);
                subLists.add(subList);
            }
            output.addAll(subLists);
        }
        return output;
    }

    public static void main(String args[]) {
        Permutations<Integer> obj = new Permutations<Integer>();
        Collection<Integer> input = new ArrayList<Integer>();
        input.add(1);
        input.add(2);
        input.add(3);
        input.add(4);

        Collection<List<Integer>> output = obj.permute(input);
        //int k = 0;
        Set<List<Integer>> pnr;
        for (int i = 0; i <= input.size(); i++) {
            pnr = new HashSet<>();
            for(List<Integer> integers : output){
                List<Integer> l = new ArrayList<> (integers.subList(i, integers.size()));
                Collections.sort(l);
                pnr.add(l);
            }
            //k = input.size()- i;
            //System.out.println("P("+input.size()+","+k+") :"+
            //        "Count ("+pnr.size()+") :- "+pnr);
        }
    }


}