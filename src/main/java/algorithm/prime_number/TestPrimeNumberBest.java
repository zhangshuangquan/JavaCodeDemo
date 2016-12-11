package algorithm.prime_number;

/**
 * Created by zsq on 16/12/11.
 * 求出[1,n]之间的所有素数[最高效的算法,算法复杂度O(n)]
 * 定理: 拿已知的素数列表去过滤合数,那么能够保证被过滤掉的合数有且只被过滤一次
 */
public class TestPrimeNumberBest {

    private static int opsNum = 0;

    public static void main(String[] args) {
        int n = 100;
        boolean[] result = getPrimes(n);
        boolean isPrime;
        for (int i = 2; i < n; i++) {
            isPrime = result[i-1];
            //如果是素数,则打印出来
            if (!isPrime) {
                System.out.println(i);
            }
        }
    }

    /**
     * 标记[1, n]之间的所有素数
     * @param n
     * @return
     */
    private static boolean[] getPrimes(int n) {
        //使用一个数组标记[1,n]之间的每个数字是否为素数,false表示素数,true表示合数
        boolean[] isPrimes = new boolean[n];
        //目前已知的素数
        int[] primeList = new int[n];
        //目前已知素数列表的索引
        int p = 0;
        for(int i = 2; 2 * i <= n; i++) {
            //若为素数,则记录到目前已知的素数数组中
            if(!isPrimes[i - 1]) {
                primeList[p++] = i;
            }
            //遍历目前已知的素数来过滤合数
            for(int j = 0; j < p; j++) {
                if(i * primeList[j] <= n) {
                    // i * primeList[j]一定是合数,因为已知primeList[j]是素数,而i > 1,因此得以证明i * primeList[j]一定是合数.
                    isPrimes[i * primeList[j] - 1] = true;
                    //统计遍历次数,主要是为了了解算法的性能
                    opsNum++;
                }
                // 如果i不是一个素数
                if(i % primeList[j] == 0) {
                    break;
                }
            }
        }
        System.out.println("Total loop times:" + opsNum);
        return isPrimes;
    }
}
