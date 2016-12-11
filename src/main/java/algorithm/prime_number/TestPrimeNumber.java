package algorithm.prime_number;

/**
 * Created by zsq on 16/12/11.
 *  求出[1,n]之间的所有素数
 * 最原始的做法
 */
public class TestPrimeNumber {

    private static int opsNum = 0;

    public static void main(String[] args) {
        //假设求1-100以内的素数
        int n = 100;
        boolean isPrime;
        for (int i = 2; i < n; i++) {
            isPrime = isPrime(i); //算法1
            //isPrime = isPrime2(i); //算法2
            //如果是素数,则打印
            if (isPrime) {
                System.out.println(i);
            }
            System.out.println("Total loop times:"+opsNum);
        }
    }

    private static boolean isPrime(int n) {
        //若传入的数字n小于2,则直接return false,因为最小的素数为2
        if (n < 2) {
            return false;
        }
        //从2遍历到n-1
        for (int i = 2; i < n; i++) {
            opsNum++;
            //如果发现能被其中任意一个数整除,那么说明数字n不是素数
            if (n % i == 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * 循环次数少
     * @param n
     * @return
     */
    private static boolean isPrime2(int n) {
        //若传入的数字n小于2,则直接return false,因为最小的素数为2
        if (n < 2) {
            return false;
        }
        if (n == 2) {
            return true;
        }
        //如果传入的数字是偶数,那么一定不是素数
        if (n % 2 == 0) {
            return false;
        }
        //从3开始遍历剩下所有的奇数
        for (int i = 3; i < n; i += 2) {
            opsNum++;
            if (n % i == 0) {
             return false;
            }
        }
        return true;
    }
}
