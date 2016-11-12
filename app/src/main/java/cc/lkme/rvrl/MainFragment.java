package cc.lkme.rvrl;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import cc.lkme.rvrl.databinding.MainFragBinding;

/**
 * Created by LinkedME06 on 16/11/10.
 */

public class MainFragment extends Fragment {
    private LinearLayoutManager mLayoutManager;
    private ArrayList<DataEntry> mRVData;
    private LPRecyclerViewAdapter<DataEntry> lpRecyclerViewAdapter;
    protected Handler handler;

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
        MainFragBinding binding = MainFragBinding.inflate(inflater, container, false);

        binding.lpRv.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
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
        loadData();
//        lpRecyclerViewAdapter = new LPRecyclerViewAdapter<>(mRVData, R.layout.recycler_view_item, BR.lp_rv_item);
        lpRecyclerViewAdapter = new LPRecyclerViewAdapter<>(mRVData, R.layout.recycler_view_item, BR.lp_rv_item, swipeRefreshLayout, binding.lpRv);
        lpRecyclerViewAdapter.setOnLoadMoreListener(new LPRefreshLoadListener.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                lpRecyclerViewAdapter.showLoadingMore(true);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("lp_log", "onLoadMore: ");
                        //   remove progress item
                        lpRecyclerViewAdapter.showLoadingMore(false);
                        //add items one by one
                        int start = mRVData.size();
                        for (int i = 0; i < 10; i++){
                            DataEntry dataEntry = new DataEntry();
                            dataEntry.setName("peng" + i);
                            mRVData.add(dataEntry);
                            lpRecyclerViewAdapter.notifyItemInserted(mRVData.size());
                        }
//                        lpRecyclerViewAdapter.notifyDataSetChanged();
                        lpRecyclerViewAdapter.setLoadingMore(false);
                        //or you can add all at once but do not forget to call mAdapter.notifyDataSetChanged();
                    }
                }, 2000);

            }
        });
        lpRecyclerViewAdapter.setOnRefreshListener(new LPRefreshLoadListener.onRefreshListener() {
            @Override
            public void onRefresh() {
                //数据刷新操作
                mRVData.clear();
                DataEntry dataEntry = new DataEntry();
                dataEntry.setName("lp");
                mRVData.add(dataEntry);
                mRVData.add(dataEntry);
                mRVData.add(dataEntry);
                mRVData.add(dataEntry);
                mRVData.add(dataEntry);
                mRVData.add(dataEntry);
                mRVData.add(dataEntry);
                mRVData.add(dataEntry);
                mRVData.add(dataEntry);
                lpRecyclerViewAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        binding.lpRv.setAdapter(lpRecyclerViewAdapter);
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

    private void loadData() {
        mRVData = new ArrayList<>();
        DataEntry dataEntry = new DataEntry();
        dataEntry.setName("lipeng");
        mRVData.add(dataEntry);
        mRVData.add(dataEntry);
        mRVData.add(dataEntry);
        mRVData.add(dataEntry);
        mRVData.add(dataEntry);
        mRVData.add(dataEntry);
        mRVData.add(dataEntry);
        mRVData.add(dataEntry);
        mRVData.add(dataEntry);
    }

}
