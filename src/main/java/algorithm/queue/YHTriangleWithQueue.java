package algorithm.queue;

import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * Created by zsq on 16/12/13.
 * 使用队列求解杨辉三角第k层的所有元素
 */
public class YHTriangleWithQueue {

    public static void main(String[] args) {
        int k = 6;
        int[][] nums = kTier(k);

        for(int j=0; j < k; j++) {
            System.out.print(nums[k % 2][j] + " ");
        }
    }

    private static int[][] kTier(int k) {
        int[][] nums = new int[2][k];
        if(k <= 0) {
            throw new IllegalArgumentException("Argument k MUST be > 0.");
        }
        ConcurrentLinkedDeque<Event> queue = new ConcurrentLinkedDeque<Event>();
        Event head = new Event(1,1);
        Event tail = new Event();
        int level = 0;
        int pos = 0;
        queue.add(head);
        while (!queue.isEmpty()) {
            head = queue.getFirst();
            level = head.getLevel();
            pos = head.getPos();
            if(pos == 1 || pos == level) {
                nums[level % 2][pos - 1] = 1;
            } else {
                nums[level % 2][pos - 1] = nums[(level - 1) % 2][pos - 1] + nums[(level - 1) % 2][pos - 2];
            }
            if(level < k) {
                Event tt = new Event(level + 1, pos);
                queue.addLast(tt);
                if(pos == level) {
                    Event t = new Event(tt.getLevel(), pos + 1);
                    queue.addLast(t);
                }
            }
            queue.pop();
        }
        return nums;
    }

    public static class Event {
        //表示第几层
        private int level;
        //第几位
        private int pos;

        public Event() {
        }

        public Event(int level, int pos) {
            this.level = level;
            this.pos = pos;
        }

        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
        }

        public int getPos() {
            return pos;
        }

        public void setPos(int pos) {
            this.pos = pos;
        }
    }
}
