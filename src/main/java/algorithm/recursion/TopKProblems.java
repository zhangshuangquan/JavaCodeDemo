package algorithm.recursion;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zsq on 16/12/13.
 * 从一个无序的数字序列中查找出前K个最大的数字,
 * 要求返回的K个数字在目标无序数组中是前K个最大的,但是不要求这前K个数字是有序的
 * 比如：8 9 5 0 6 2 7 1 4 3 且K = 5,那么最终应该返回
 * 9 8 7 6 5 或者 6 5 8 7 9
 */
public class TopKProblems {

    public static void main(String[] args) {
        int k = 5;
        int[] array = {8, 9, 5, 0, 6, 2, 7, 1, 4, 3};
        List<Integer> result = new ArrayList<>();
        findTopKBig(array, 0, array.length, k, result);
        if(null == result || result.size() <= 0) {
            System.out.println("No results.");
            return;
        }
        for(int i=0; i < result.size(); i++) {
            System.out.print(result.get(i) + " ");
        }
    }

    /**
     * 从一个无序的数字序列中查找出前K个最大的数字
     * @param array     待查找的无序数组
     * @param offset    查找的起始偏移量
     * @param n         待查找的前N个
     * @param k         表示最终需要返回的前K个
     * @param result
     * @return
     */
    private static List<Integer> findTopKBig(int[] array, int offset, int n, int k, List<Integer> result) {
        if(n <= 0) {
            return null;
        }
        if(result == null) {
            result = new ArrayList<>();
        }
        //如果k >= n,则直接返回整个数组即可
        if(k >= n) {
            for(int i=offset; i < offset + n; i++) {
                result.add(array[i]);
            }
            return result;
        }
        //a1表示中间那个元素,a1将整个数组分成左大右小两部分
        int a1 = array[offset];
        //头指针
        int y = offset;
        //尾指针
        int z = offset + n - 1;
        int n1;
        int n2;
        while (y < z) {
            //从头指针往尾指针遍历
            while (array[z] <= a1 && y < z) {
                z--;
            }
            if(y < z) {
                array[y] = array[z];
            }
            //从尾指针往头指针遍历
            while (array[y] > a1 && y < z) {
                y++;
            }
            if(y < z) {
                array[z] = array[y];
            }
        }
        //此时头尾指针重合,找到a1的位置即中间的那个元素
        array[y] = a1;
        //计算a1前面的元素总个数即左边比较大的元素总个数
        n1 = y - offset + 1;
        //计算a1后面的元素总个数即右边比较小的元素总个数
        n2 = n - n1;
        //如果k >= n1,那么直接返回前n1个元素
        if(n1 <= k) {
            for(int i = offset; i <= y; i++) {
                result.add(array[i]);
            }
        }
        //如果k < n1,那么就需要在[offset,n1 - 1]范围内查找前K个最大的数字
        if(n1 > k) {
            return findTopKBig(array, offset, n1 - 1 ,k, result);
        }
        //如果k > n1,那么就需要在[y + 1,n2]范围内查找前K个最大的数字,这里的y+1其实表示的就是a1后面的那个元素即
        //从a1后面的n2个比较小元素里查找剩下的符合要求的数字
        if(n1 < k) {
            return findTopKBig(array, y + 1, n2, k - n1, result);
        }
        return result;
    }
}
