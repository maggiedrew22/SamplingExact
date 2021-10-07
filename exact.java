import java.io.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.lang.String;
import java.util.*;
import java.lang.*;
import java.util.Map.*;


public class exact {
    // start time
    double start = System.currentTimeMillis();

    // links that helped me are https://www.w3schools.com/java/java_hashmap.asp
    // and https://www.geeksforgeeks.org/sorting-a-hashmap-according-to-values/
    // and https://www.baeldung.com/java-csv

    // input = minimum frequency thres, path to dataset data
    // output = frequent items in data, listed in non-ascending order by frequency, tiebreaker by increasing value of item, in form (item, frequency)
    // must go through one transaction at a time

    public static void main(String[] args) throws IOException, FileNotFoundException {
        double start = System.currentTimeMillis();
        // create dictionary of items/occurrences with no keys/values (blank to start)
        HashMap<Integer, Integer> holdVals = new HashMap<Integer, Integer>();
        // create list called frequent items (ArrayList)
        HashMap<Integer, Double> holdFreq = new HashMap<>();

        double thres = Double.parseDouble(args[0]);
        //String path = args[1];
        File toRead = new File(args[1]);
        BufferedReader reader = new BufferedReader(new FileReader(toRead));

        String s;
        // used this to count the number of transcriptions in each file
        int trCount = 0;

        // for each transaction in data go line by line
        while ((s = reader.readLine()) != null){
            // extract the separate numbers in each line by removing spaces
            String[] tempholder = s.split(" ");
            int sizeth = tempholder.length;
            // create int array dataholder to hold data values
            int[] dataholder = new int[sizeth];
            // for each element in tempholder, convert to int and place in dataholder
            for (int i = 0; i < dataholder.length; i++){
                dataholder[i] = Integer.parseInt(tempholder[i]);
            }
            // for each item in the transaction
            for (int j = 0; j < dataholder.length; j++){
                // if the item already exists in the dictionary
                if (holdVals.containsKey(dataholder[j])){
                    // increment the associated item in the dictionary (ex 1 -> dictionary++ for 1}
                    holdVals.put(dataholder[j], holdVals.get(dataholder[j]) + 1);
                }
                // if the item does not exist yet in the dictionary
                else {
                    // add it to the dictionary (using a hash map)
                    holdVals.put(dataholder[j], 1);
                }
            }
            trCount++;
        }


        // iterate through elements of hashMap
        for (Map.Entry<Integer, Integer> a : holdVals.entrySet()){
            // if the value divided by the # of transactions is greater than the threshold frequency
            if (thres <= (a.getValue() / (double)trCount)){
                holdFreq.put(a.getKey(), (a.getValue() / (double)trCount));
            }
            // if the value divided by the # of transactions is less than the threshold frequency
            else if (thres > (a.getValue() / trCount)){
            }
        }

        // sort hashmap according to hw instructions for output
        Map<Integer, Double> finalFreq = sortFinalVals(holdFreq);


        for (Map.Entry<Integer, Double> b : finalFreq.entrySet()) {
            System.out.println(b.getKey() + ", " + b.getValue());
        }

    }

    // helper method for sorting a hashmap, code adopted from website posted at the top
    public static HashMap<Integer, Double> sortFinalVals(HashMap<Integer, Double> hv){
        // create a linked list to hold elements in original hashmap
        List<Map.Entry<Integer, Double>> tempList = new LinkedList<Map.Entry<Integer, Double>>(hv.entrySet());
        // sort the linked list according to its elements
        Collections.sort(tempList, new Comparator<Map.Entry<Integer, Double>>() {
            @Override
            public int compare(Map.Entry<Integer, Double> o1, Map.Entry<Integer, Double> o2) {
                if (o1.getValue().compareTo(o2.getValue()) == 0){
                    if (o1.getKey().compareTo(o2.getKey()) > 0){
                        return 1;
                    } else if (o1.getKey().compareTo(o2.getKey()) < 0){
                        return -1;
                    }
                }
                return (o2.getValue().compareTo(o1.getValue()));
            }
        });

        // copy data from sorted list into hashmap
        HashMap<Integer, Double> tempHM = new LinkedHashMap<Integer, Double>();
        for (Map.Entry<Integer, Double> a : tempList){
            tempHM.put(a.getKey(), a.getValue());
        }

        // return sorted hashmap
        return tempHM;
    }

}


