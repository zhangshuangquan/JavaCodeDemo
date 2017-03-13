package demo.luceneZoie;

import proj.zoie.api.ZoieException;

import java.util.TimerTask;

/**
 * Created by zsq on 2017/3/13.
 *
 * zoie 定时增量 索引任务
 */
public class ZoieIndexTimerTask extends TimerTask{

    private ZoieIndex zoieIndex;

    @Override
    public void run() {
        try {
            zoieIndex.init();
            zoieIndex.updateIndexData();
        } catch (ZoieException e) {
            e.printStackTrace();
        }
    }

    public ZoieIndexTimerTask(ZoieIndex zoieIndex) {
        super();
        this.zoieIndex = zoieIndex;
    }

    public ZoieIndex getZoieIndex() {
        return zoieIndex;
    }

    public void setZoieIndex(ZoieIndex zoieIndex) {
        this.zoieIndex = zoieIndex;
    }
}
