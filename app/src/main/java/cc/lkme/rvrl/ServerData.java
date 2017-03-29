package cc.lkme.rvrl;

import com.google.gson.Gson;

import android.util.Log;

import com.ww.lp.rvrl_lib.LPConstants;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by LinkedME06 on 24/03/2017.
 *
 * 模拟服务器数据
 */

public class ServerData {

    /**
     * 模拟获取列表数据
     *
     * @param pageIndex 当前页号
     * @return 返回列表json字符串
     */
    public static String getListData(int pageIndex) {
        ServerDataEntry serverDataEntry = new ServerDataEntry();
        serverDataEntry.setData(loadData(pageIndex));
        //模拟列表页数为5页
        serverDataEntry.setPageCount(5);
        serverDataEntry.setPageIndex(pageIndex);
        String jsonStr = new Gson().toJson(serverDataEntry);
        //此处模拟获取数据失败的情况
        if (new Random().nextInt(10) == 0) {
            jsonStr = null;
        }
        Log.d(LPConstants.TAG, "Server simulated data: " + jsonStr);
        return jsonStr;
    }


    /**
     * 列表数据
     *
     * @param pageIndex 当前页号
     */
    private static ArrayList<ListDataEntry> loadData(int pageIndex) {
        ArrayList<ListDataEntry> mRVData = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            ListDataEntry dataEntry = new ListDataEntry();
            dataEntry.setName(pageIndex + "-lp-" + i);
            mRVData.add(dataEntry);
        }
        return mRVData;
    }


}
