import java.io.*;
import java.util.*;

public class sampling {

    public static void main(String[] args) throws IOException {
        // size of sample to be collected
        int ssize = Integer.parseInt(args[0]);
        // copy of size of sample to be used below
        int copyssize = ssize;
        // size of dataset
        int dsize = Integer.parseInt(args[1]);
        // failure probability
        double delta = Double.parseDouble(args[2]);
        // min frequency threshold
        double thres = Double.parseDouble(args[3]);
        // path to dataset
        String path = args[4];
        // maximum length of a transaction
        int maxTr = 0;
        // index of transaction being processed in ranNums
        int sampleIndex = 0;
        // track number of transcriptions
        int trackTr = 0;

        // create file
        File toRead = new File(args[4]);
        BufferedReader reader = new BufferedReader(new FileReader(toRead));

        // create dictionary of items/occurrences with no keys/values (blank to start)
        HashMap<Integer, Integer> holdVals = new HashMap<Integer, Integer>();
        // create list called frequent items (ArrayList)
        HashMap <Integer, Double> holdFreq = new HashMap<>();
        // create arraylist to store random numbers
        ArrayList<Integer> ranNums = new ArrayList<>();

        // generate ssize random numbers between 1 and dsize (or 0 and dsize - 1)
        while (ssize > 0){
            // add random number to data structure
            ranNums.add((int)(Math.random() * (dsize - 1)));
            // decrement ssize by 1
            ssize--;
        }

        // sort ssize random numbers in increasing order
        ranNums.sort(Comparator.naturalOrder());

        String s;

        trackTr = ranNums.get(sampleIndex);

        // while each line in the file is nonempty
        while ((s = reader.readLine()) != null && (sampleIndex + 1) != copyssize){
            // if the line that is to be processed is equal to the line in ranNums
            if (trackTr == ranNums.get(sampleIndex)) {
                // extract the separate numbers in each line by removing spaces
                String[] tempholder = s.split(" ");
                int sizeth = tempholder.length;
                // check if length of this transaction is greater than maxTr, and if so set maxTr equal to it
                if (sizeth > maxTr) {
                    maxTr = sizeth;
                }
                // create int array dataholder to hold data values
                int[] dataholder = new int[sizeth];
                // for each element in tempholder, convert to int and place in dataholder
                for (int i = 0; i < dataholder.length; i++) {
                    dataholder[i] = Integer.parseInt(tempholder[i]);
                }
                // for each item in the transaction
                for (int j = 0; j < dataholder.length; j++) {
                    // if the item already exists in the dictionary
                    if (holdVals.containsKey(dataholder[j])) {
                        // increment the associated item in the dictionary (ex 1 -> dictionary++ for 1}
                        holdVals.put(dataholder[j], holdVals.get(dataholder[j]) + 1);
                    }
                    // if the item does not exist yet in the dictionary
                    else {
                        // add it to the dictionary (using a hash map)
                        holdVals.put(dataholder[j], 1);
                    }
                }
                // edge case if the sample index is 2
                if (ranNums.get(sampleIndex) == ranNums.get(0) && (sampleIndex + 1) != copyssize){
                    // if next sample index equals current sample index, process same transaction
                    if (ranNums.get(sampleIndex).equals(ranNums.get(sampleIndex + 1))){
                        sampleIndex++;
                    }
                    // if next sample index does not equal current sample index, process next transaction
                    if (!ranNums.get(sampleIndex).equals(ranNums.get(sampleIndex + 1))){
                        sampleIndex++;
                        trackTr = ranNums.get(sampleIndex);
                    }
                }
                // if we have reached the end of the transactions
                else if (ranNums.get(sampleIndex) == ranNums.get(0) && (sampleIndex + 1) == copyssize){
                    break;
                }
                // if the next element in ranNums is the same as the current element then process the same transaction
                if (ranNums.get(sampleIndex).equals(ranNums.get(sampleIndex - 1))){
                    if ((sampleIndex + 1) == copyssize){
                        break;
                    }
                    // increment sampleIndex and repeat transaction
                    sampleIndex++;
                }
                // if the next element in ranNums is not equal to the current element then move to next element
                if (!ranNums.get(sampleIndex).equals(ranNums.get(sampleIndex - 1))){
                    if ((sampleIndex + 1) == copyssize){
                        break;
                    }
                    // set trackTr to next index
                    sampleIndex++;
                    trackTr = ranNums.get(sampleIndex);
                }
            }

        }

        // compute dS - check log function here
        int dS = (int)((Math.log(maxTr) / Math.log(2)) + 1);
        // output eps (quality of the approximation)
        double eps = (Math.sqrt((2 *(dS + Math.log(1 / delta)))) / copyssize);

        // so then we can calculate eps/2
        double halfEps = eps/2;

        // so now we can calculate the exactFrequentItems
        // iterate through elements of hashMap
        for (Map.Entry<Integer, Integer> a : holdVals.entrySet()){
            // if the value divided by the # of transactions is greater than the threshold frequency
            if ((thres - halfEps) <= ((double)a.getValue() / copyssize)){
                holdFreq.put(a.getKey(), ((double)a.getValue() / copyssize));
            }
            // if the value divided by the # of transactions is less than the threshold frequency
            else if (thres > (a.getValue() / dsize)){
                // do nothing (not in set of exactFrequentItems)
            }
        }

        // sort the exactFrequentItems using hashmap sorting algorithm as shown in exact.java
        Map<Integer, Double> finalFreq = exact.sortFinalVals(holdFreq);

        // finally, print out value of epsilon and all frequent items/values
        System.out.println(eps);
        for (Map.Entry<Integer, Double> b : finalFreq.entrySet()) {
            System.out.println(b.getKey() + ", " + b.getValue());
        }

    }
}
