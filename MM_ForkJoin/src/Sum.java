import java.util.concurrent.RecursiveTask;


public class Sum extends RecursiveTask<Integer> {
    static final int SEQUENTIAL_THRESHOLD = 5000;

    int low;
    int high;
    int[] array;
    
	public Sum(int[] arr, int lo, int hi) {
        array = arr;
        low   = lo;
        high  = hi;				
	}
	
	@Override
	protected Integer compute() {
        if(high - low <= SEQUENTIAL_THRESHOLD) {
            int sum = 0;
            for(int i=low; i < high; ++i) 
                sum += array[i];
            return sum;
         } else {
            int mid = low + (high - low) / 2;
            Sum left  = new Sum(array, low, mid);
            Sum right = new Sum(array, mid, high);
            left.fork();
            int rightAns = right.compute();
            int leftAns  = left.join();
            return leftAns + rightAns;
         }
	}
}
