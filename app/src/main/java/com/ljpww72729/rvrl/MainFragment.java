package com.ljpww72729.rvrl;

import com.google.gson.Gson;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ljpww72729.rvrl.databinding.MainFragBinding;
import com.ww.lp.rvrl_lib.LPConstants;
import com.ww.lp.rvrl_lib.LPRecyclerViewAdapter;
import com.ww.lp.rvrl_lib.LPRefreshLoadListener;
import com.ww.lp.rvrl_lib.ScrollChildSwipeRefreshLayout;
import com.ww.lp.rvrl_lib.SingleItemClickListener;

import java.util.ArrayList;


/**
 * Created by LinkedME06 on 16/11/10.
 */

public class MainFragment extends Fragment {
    private LinearLayoutManager mLayoutManager;
    private ArrayList<ListDataEntry> mRVData;
    private LPRecyclerViewAdapter<ListDataEntry> lpRecyclerViewAdapter;
    protected Handler handler;
    private MainFragBinding binding;

    public static MainFragment newInstance() {

        Bundle args = new Bundle();

        MainFragment fragment = new MainFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        handler = new Handler();
        binding = MainFragBinding.inflate(inflater, container, false);
        binding.lpRv.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.lpRv.setLayoutManager(mLayoutManager);
        // Set up progress indicator
        final ScrollChildSwipeRefreshLayout swipeRefreshLayout = binding.lpScsr;
        swipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getActivity(), R.color.colorPrimary),
                ContextCompat.getColor(getActivity(), R.color.colorAccent),
                ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark)
        );
        // Set the scrolling view in the custom SwipeRefreshLayout.
        swipeRefreshLayout.setScrollUpChild(binding.lpRv);
        // specify an adapter (see also next example)
        mRVData = new ArrayList<>();
//        lpRecyclerViewAdapter = new LPRecyclerViewAdapter<>(mRVData, R.layout.recycler_view_item, BR.lp_rv_item);
        lpRecyclerViewAdapter = new LPRecyclerViewAdapter<>(mRVData, R.layout.recycler_view_item, com.ljpww72729.rvrl.BR.lp_rv_item, swipeRefreshLayout, binding.lpRv);
        lpRecyclerViewAdapter.setPageStartNum(0);
        lpRecyclerViewAdapter.setOnLoadMoreListener(new LPRefreshLoadListener.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                lpRecyclerViewAdapter.showLoadingMore(true);
                loadData(lpRecyclerViewAdapter.getPageCurrentNum() + 1);
            }
        });
        lpRecyclerViewAdapter.setOnRefreshListener(new LPRefreshLoadListener.onRefreshListener() {
            @Override
            public void onRefresh() {
                //数据刷新操作
                loadData(lpRecyclerViewAdapter.getPageStartNum());
            }
        });
        binding.lpRv.setAdapter(lpRecyclerViewAdapter);
        loadData(lpRecyclerViewAdapter.getPageStartNum());
        binding.lpRv.addOnItemTouchListener(new SingleItemClickListener(binding.lpRv, new SingleItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(getActivity(), String.valueOf(position), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));

        return binding.getRoot();
    }

    private void loadData(final int pageIndex) {
        // TODO: 16/11/26 数据是否这样更新有待优化
        Log.d(LPConstants.TAG, "loadData: start!");
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d(LPConstants.TAG, "loadData: success!");
                boolean result = true;
                String jsonStr = ServerData.getListData(pageIndex);
                if (TextUtils.isEmpty(jsonStr)) {
                    result = false;
                }
                Log.d(LPConstants.TAG, "recyclerview is loading more ? " + lpRecyclerViewAdapter.isLoadingMore());
                if (result) {
                    ServerDataEntry serverData = new Gson().fromJson(jsonStr, ServerDataEntry.class);
                    lpRecyclerViewAdapter.loadDataSuccess(serverData.getData(), serverData.getPageCount());
                } else {
                    Toast.makeText(getActivity(), "获取数据失败，请重试！", Toast.LENGTH_SHORT).show();
                    lpRecyclerViewAdapter.loadDataFailed();
                }

            }
        }, 2000);

    }

}
