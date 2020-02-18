package s4.B171302;
import s4.specification.FrequencerInterface;


/*package s4.specification;
  public interface FrequencerInterface {     // This interface provides the design for frequency counter.
  void setTarget(byte  target[]); // set the data to search.
  void setSpace(byte  space[]);  // set the data to be searched target from.
  int frequency(); //It return -1, when TARGET is not set or TARGET's length is zero
  //Otherwise, it return 0, when SPACE is not set or SPACE's length is zero
  //Otherwise, get the frequency of TAGET in SPACE
  int subByteFrequency(int start, int end);
  // get the frequency of subByte of taget, i.e target[start], taget[start+1], ... , target[end-1].
  // For the incorrect value of START or END, the behavior is undefined.
  }
*/



public class Frequencer implements FrequencerInterface{
    // Code to start with: This code is not working, but good start point to work.
    byte [] myTarget;
    byte [] mySpace;
    boolean targetReady = false;
    boolean spaceReady = false;

    int []  suffixArray; // Suffix Array int[]


    // The variable, "suffixArray" is the sorted array of all suffixes of mySpace.
    // Each suffix is expressed by a integer, which is the starting position in mySpace.

    // The following is the code to print the contents of suffixArray.
    // This code could be used on debugging.

    private void printSuffixArray() {
        if(spaceReady) {
            for(int i=0; i< mySpace.length; i++) {
                int s = suffixArray[i];
                for(int j=s;j<mySpace.length;j++) {
                    System.out.write(mySpace[j]);
                }
                System.out.write('\n');
            }
        }
    }

    private int suffixCompare(int i, int j) {
        // suffixCompare is sorting method.
        // Define as follow.
        // comparing two suffixes by dictionary order.
        // suffix_i is a string starting with the position i in "byte [] mySpace".
        // Each i and j denote suffix_i, and suffix_j.
        // Example of dictionary order
        // "i"      <  "o"        : compare by code
        // "Hi"     <  "Ho"       ; if head is same, compare the next element
        // "Ho"     <  "Ho "      ; if the prefix is identical, longer string is big
        //
        //The return value of "int suffixCompare" is as follows.
        // if suffix_i > suffix_j, it returns 1
        // if suffix_i < suffix_j, it returns -1
        // if suffix_i = suffix_j, it returns 0;

    	for (int k = 0; k < mySpace.length; k++) {
    		if (suffixArray[i] + k >= mySpace.length) return -1;
    		if (suffixArray[j] + k >= mySpace.length) return 1;
	    	if (mySpace[suffixArray[i] + k] > mySpace[suffixArray[j] + k]) return 1;
	    	if (mySpace[suffixArray[i] + k] < mySpace[suffixArray[j] + k]) return -1;
    	}
        return 0;
    }

    public void setSpace(byte []space) {
        // suffixArray preprocess define in setSpace.
        mySpace = space; if(mySpace.length>0) spaceReady = true;

        // First, create unsorted suffix array.
        suffixArray = new int[space.length];

        // put all suffixes in suffixArray.
        for(int i = 0; i< space.length; i++) {
            suffixArray[i] = i; // Please note that each suffix is expressed by one integer.
        }
        //

		/*
    	int t;
    	for (int i = 0; i < suffixArray.length - 1; i++) {
    		for (int j = i + 1; j < suffixArray.length; j++) {
    			//System.out.println("i:" + i + ", j:" + j + " result : " + suffixCompare(i, j));
    			if (suffixCompare(i, j) == 1) {
    				t = suffixArray[i];
    				suffixArray[i] = suffixArray[j];
    				suffixArray[j] = t;
    			}
    		}
		}
		*/

		//quickSort(suffixArray, 0, suffixArray.length - 1);
		sort(suffixArray);	// heap sort

		//printSuffixArray();
	}

	private void sort(int[] array) {
        int n = array.length;

        for (int i = n / 2 -1; i >= 0; i--){
            heap(array, n, i);
        }

        for (int i = n - 1 ; i >= 0; i--){
            if (suffixCompare(0, i) == 1) {
                int tmp = array[0];
                array[0] = array[i];
                array[i] = tmp;

                heap(array, i-1, 0);
            }

        }
    }

    private void heap(int[] array, int n , int root){
        int largest = root;
        int left = 2 * root + 1;
        int right = 2 * root + 2;

        if (left < n && (suffixCompare(left, largest) == 1)){
            largest = left;
        }

        if (right < n && (suffixCompare(right, largest) == 1)){
            largest = right;
        }

        if (largest != root){
            int swap = array[root];
            array[root] = array[largest];
            array[largest] = swap;

            heap(array, n ,largest);
        }
    }

	/*
	void quickSort(int[] array, int left, int right){
		int index = partition(array, left, right);
		if(left < index - 1){
			quickSort(array, left, index - 1);
		}
		if(index < right){
			quickSort(array, index, right);
		}
	}
	int partition(int[] array, int left, int right){
		int pivot = (left + right) / 2;
		while (left < right) {
			while (suffixCompare(left, pivot) == -1) left++;
			while (suffixCompare(right, pivot) == 1) right--;
			if (left <= right){
				int tmp = array[left];
				array[left] = array[right];
				array[right] = tmp;
				left++;
				right--;
			}
		}
		return left;
	}
*/

    public void setTarget(byte [] target) {
        myTarget = target; if(myTarget.length>0) targetReady = true;
    }

    public int frequency() {
        if(targetReady == false) return -1;
        if(spaceReady == false) return 0;
        return subByteFrequency(0, myTarget.length);
    }



    public int subByteFrequency(int start, int end) {
        /* This method be work as follows, but much more efficient
           int spaceLength = mySpace.length;
           int count = 0;
           for(int offset = 0; offset< spaceLength - (end - start); offset++) {
            boolean abort = false;
            for(int i = 0; i< (end - start); i++) {
             if(myTarget[start+i] != mySpace[offset+i]) { abort = true; break; }
            }
            if(abort == false) { count++; }
           }
        */

    	//for (int i = 0; i < suffixArray.length; i++) { System.out.println("i=" + i + " : " + targetCompare(i, start, end)); };

        int first = subByteStartIndex(start, end);
        int last1 = subByteEndIndex(start, end);
    	//System.out.println("first : " + first + ", last1 : " + last1);
        return last1 - first;
    }

    private int targetCompare(int i, int j, int k) {
        // suffix_i is a string in mySpace starting at i-th position.
        // target_i_k is a string in myTarget start at j-th postion ending k-th position.
        // comparing suffix_i and target_j_k.
        // if the beginning of suffix_i matches target_i_k, it return 0.
        // The behavior is different from suffixCompare on this case.
        // if suffix_i > target_i_k it return 1;
        // if suffix_i < target_i_k it return -1;
        // It should be used to search the appropriate index of some suffix.
        // Example of search
        // suffix          target
        // "o"       >     "i"
        // "o"       <     "z"
        // "o"       =     "o"
        // "o"       <     "oo"
        // "Ho"      >     "Hi"
        // "Ho"      <     "Hz"
        // "Ho"      =     "Ho"
        // "Ho"      <     "Ho "   : "Ho " is not in the head of suffix "Ho"
        // "Ho"      =     "H"     : "H" is in the head of suffix "Ho"

    	//System.out.println("Compared : " + i + " & " + j + "~" + k);

		for (int n = 0; n < k - j; n++) {
			if (suffixArray[i] + n >= mySpace.length) return -1;
			if (j + n >= myTarget.length) return 1;
			if (mySpace[suffixArray[i] + n] > myTarget[j + n]) return 1;
			if (mySpace[suffixArray[i] + n] < myTarget[j + n]) return -1;
		}
        return 0;
    }


    private int subByteStartIndex(int start, int end) {
        /* Example of suffix created from "Hi Ho Hi Ho"
           0: Hi Ho
           1: Ho
           2: Ho Hi Ho
           3:Hi Ho
           4:Hi Ho Hi Ho
           5:Ho
           6:Ho Hi Ho
           7:i Ho
           8:i Ho Hi Ho
           9:o
           A:o Hi Ho
        */

        // It returns the index of the first suffix
        // which is equal or greater than target_start_end.
        // Assuming the suffix array is created from "Hi Ho Hi Ho",
        // if target_start_end is "Ho", it will return 5.
        // Assuming the suffix array is created from "Hi Ho Hi Ho",
        // if target_start_end is "Ho ", it will return 6.

    	// binary search
    	int cmp;
        int lower = 0;
        int upper = suffixArray.length - 1;
        while (lower <= upper) {
            int mid = (lower + upper) / 2;
            cmp = targetCompare(mid, start, end);
            if (cmp == 0) {
            	if (mid <= 0) {
            		return mid;
            	}
            	if (targetCompare(mid - 1, start, end) == -1) {
            		return mid;
            	} else {
            		upper = mid - 1;
            	}
            } else if (cmp == -1) {
                lower = mid + 1;
            } else {
                upper = mid - 1;
            }
        }

        /*
    	for (int i = 0; i < suffixArray.length; i++) {
    		if (targetCompare(i, start, end) == 0) {
				return i;
			}
    	}
    	*/
        return mySpace.length;
    }

    private int subByteEndIndex(int start, int end) {
        /* Example of suffix created from "Hi Ho Hi Ho"
           0: Hi Ho
           1: Ho
           2: Ho Hi Ho
           3:Hi Ho
           4:Hi Ho Hi Ho
           5:Ho
           6:Ho Hi Ho
           7:i Ho
           8:i Ho Hi Ho
           9:o
           A:o Hi Ho
        */
        // It returns the index of the first suffix
        // which is greater than target_start_end; (and not equal to target_start_end)
        // Assuming the suffix array is created from "Hi Ho Hi Ho",
        // if target_start_end is "Ho", it will return 7 for "Hi Ho Hi Ho".
        // Assuming the suffix array is created from "Hi Ho Hi Ho",
        // if target_start_end is"i", it will return 9 for "Hi Ho Hi Ho".

    	// binary search
    	int cmp;
        int lower = 0;
        int upper = suffixArray.length - 1;
        while (lower <= upper) {
            int mid = (lower + upper) / 2;
            cmp = targetCompare(mid, start, end);
            if (cmp == 0) {
            	if (mid >= suffixArray.length - 1) {
            		return mid + 1;
            	}
            	if (targetCompare(mid + 1, start, end) == 1) {
            		return mid + 1;
            	} else {
            		lower = mid + 1;
            	}
            } else if (cmp == -1) {
                lower = mid + 1;
            } else {
                upper = mid - 1;
            }
        }

        //System.out.println("Not Found");

        /*for (int i = suffixArray.length - 1; i >= 0; i--) {
  			if (targetCompare(i, start, end) == 0) {
				return i + 1;
			}
    	}*/
        return mySpace.length;
    }



    public static void main(String[] args) {
    	InformationEstimator frequencerObject;
        try {
        	double result;
        	long start, end;
        	String s;

        	for (int digit = 10; digit <= 100000; digit *= 10) {
	        	s = "";
	            for(int i = 0; i < digit; i++) {
	            	if(Math.round(Math.random()) == 1) {
	            		s += "1";
	            	} else {
	            		s += "0";
	            	}
	            }

	        	//start = System.nanoTime();
	            frequencerObject = new InformationEstimator();
	            frequencerObject.setSpace(s.getBytes());	// Hi Ho Hi Ho
	            frequencerObject.setTarget("01".getBytes());
	            start = System.nanoTime();
	            result = frequencerObject.estimation();
	            end = System.nanoTime();
	            System.out.print("digit = "+ digit+" \t");
	            System.out.println("Time = " + ((double)(end - start) / 1000000.0)  + " ms");

        	}





        }
        catch(Exception e) {
            System.out.println("STOP");
        }
    }
}