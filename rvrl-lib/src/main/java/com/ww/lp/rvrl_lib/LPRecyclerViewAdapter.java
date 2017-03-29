package com.ww.lp.rvrl_lib;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.ww.lp.rvrl_lib.databinding.RecyclerViewLoadMoreBinding;

import java.util.ArrayList;

/**
 * RecyclerView Adapter Created by LinkedME06 on 16/11/10.
 */
// TODO: 22/03/2017 应该可以插入多个header或者footer
public class LPRecyclerViewAdapter<E> extends RecyclerView.Adapter {

    public static final int TYPE_ITEM = 0; // 数据项
    public static final int TYPE_PROG = 1; //加载更多
    public static final int TYPE_HEADER = 2;  //说明是带有Header的
    public static final int TYPE_FOOTER = 3;  //说明是带有Footer的

    //剩余几个页面未展示时开始加载更多
    private int visibleThreshold = 2;
    //当前可见数，第一个可见项，所有项
    private int visibleItemCount, firstVisibleItem, totalItemCount;

    //开始页面
    private int pageStartNum = 0;

    //当前页面
    private volatile int pageCurrentNum = pageStartNum;

    //页面总数
    private int pageCount;

    //是否正在加载更多
    private boolean isLoadingMore;
    //数据列表
    private ArrayList<E> mData;
    //item布局
    private int item_layout;
    //databing布局中设置的item对应的variable名称,如：BR.lp_rv_item
    private int item_data_variable;

    //加载更多监听
    private LPRefreshLoadListener.OnLoadMoreListener onLoadMoreListener;
    //刷新监听
    private LPRefreshLoadListener.onRefreshListener onRefreshListener;

    //是否开启下拉刷新功能
    private boolean enableRefresh = true;

    //是否开启加载更多功能
    private boolean enableLoadMore = true;

    private int mHeaderViewId = -1;

    private ViewDataBinding mHeaderBinding;

    private ViewDataBinding mFooterBinding;

    private ScrollChildSwipeRefreshLayout mSwipeRefreshLayout;

    public boolean isEnableLoadMore() {
        return enableLoadMore;
    }

    /**
     * 设置是否开启加载更多
     *
     * @param enableLoadMore true:开启（默认） false:关闭
     */
    public void setEnableLoadMore(boolean enableLoadMore) {
        this.enableLoadMore = enableLoadMore;
    }

    public boolean isEnableRefresh() {
        return enableRefresh;
    }

    /**
     * 是否开启刷新
     *
     * @param enableRefresh true:开启（默认） false:关闭
     */
    public void setEnableRefresh(boolean enableRefresh) {
        this.enableRefresh = enableRefresh;
    }

    public boolean isLoadingMore() {
        return isLoadingMore;
    }

    public void setLoadingMore(boolean loadingMore) {
        isLoadingMore = loadingMore;
    }

    public int getPageStartNum() {
        return pageStartNum;
    }

    public void setPageStartNum(int pageStartNum) {
        this.pageStartNum = pageStartNum;
        this.pageCurrentNum = pageStartNum;
    }

    public int getPageCurrentNum() {
        return pageCurrentNum;
    }

    public void setPageCurrentNum(int pageCurrentNum) {
        this.pageCurrentNum = pageCurrentNum;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    /**
     * 只有在数据获取成功后才调用该方法
     */
    public void loadMoreSuccess() {
        pageCurrentNum += 1;
    }

    public int getVisibleThreshold() {
        return visibleThreshold;
    }

    /**
     * 设置加载更多前剩余项数
     *
     * @param visibleThreshold 默认为2
     */
    public void setVisibleThreshold(int visibleThreshold) {
        this.visibleThreshold = visibleThreshold;
    }

    //HeaderView和FooterView的get和set函数
    public int getHeaderViewId() {
        return mHeaderViewId;
    }

    public void setHeaderViewId(final int headerViewId) {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                mHeaderViewId = headerViewId;
                notifyItemInserted(0);
            }
        });
    }

    public ViewDataBinding getmHeaderBinding() {
        return mHeaderBinding;
    }

    public void setmHeaderBinding(final ViewDataBinding headerBinding) {

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                mHeaderBinding = headerBinding;
                notifyItemInserted(0);

            }
        });
    }

    public ViewDataBinding getmFooterBinding() {
        return mHeaderBinding;
    }

    public void setmFooterBinding(final ViewDataBinding footerBinding) {

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                mFooterBinding = footerBinding;
                notifyItemInserted(getItemCount() - 1);

            }
        });
    }

    private LPHeaderViewHolder lpHeaderViewHolder;
    private LPFooterViewHolder lpFooterViewHolder;

    /**
     * 基本形式，关闭刷新及加载更多功能
     *
     * @param mData       ArrayList数据列表
     * @param item_layout item布局
     */
    public LPRecyclerViewAdapter(ArrayList<E> mData, int item_layout, int item_data_variable) {
        this(mData, item_layout, item_data_variable, null, null);
    }

    public LPRecyclerViewAdapter(ArrayList<E> mData, int item_layout, int item_data_variable, RecyclerView recyclerView) {
        this(mData, item_layout, item_data_variable, null, recyclerView);
    }

    public LPRecyclerViewAdapter(ArrayList<E> mData, int item_layout, int item_data_variable, ScrollChildSwipeRefreshLayout swipeRefreshLayout, RecyclerView recyclerView) {
        this.mData = mData;
        this.item_layout = item_layout;
        this.item_data_variable = item_data_variable;
        this.mSwipeRefreshLayout = swipeRefreshLayout;
        //判断是否开启上拉加载更多
        if (recyclerView != null && enableLoadMore && recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
            //垂直滑动
            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                }

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    Log.d(LPConstants.TAG, "onScrolled() is called!");
                    if (mSwipeRefreshLayout != null && mSwipeRefreshLayout.isRefreshing()) {
                        return;
                    }
                    visibleItemCount = recyclerView.getChildCount();
                    Log.d(LPConstants.TAG, "onScrolled: visibleItemCount = " + visibleItemCount);
                    totalItemCount = linearLayoutManager.getItemCount();
                    Log.d(LPConstants.TAG, "onScrolled: totalItemCount = " + totalItemCount);
                    firstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition();
                    Log.d(LPConstants.TAG, "onScrolled: firstVisibleItem = " + firstVisibleItem);
                    if (!isLoadingMore && pageCount > pageCurrentNum + (pageStartNum == 0 ? 1 : 0) && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
                        if (onLoadMoreListener != null) {
                            isLoadingMore = true;
                            Log.d(LPConstants.TAG, "onLoadMore() is call-back!");
                            onLoadMoreListener.onLoadMore();
                        }
                    }
                }
            });
        } else {
            enableLoadMore = false;
        }
        //设置刷新监听
        if (swipeRefreshLayout != null && enableRefresh) {
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    //加载更多时不处理刷新操作
                    if (!isLoadingMore() && onRefreshListener != null) {
                        onRefreshListener.onRefresh();
                    } else {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }
            });
        } else {
            enableRefresh = false;
        }

//        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder;
        if (viewType == TYPE_HEADER) {
            holder = new LPHeaderViewHolder(mHeaderBinding);
        } else if (viewType == TYPE_FOOTER) {
            holder = new LPFooterViewHolder(mFooterBinding);
        } else if (viewType == TYPE_ITEM) {
            //data binding
            ViewDataBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), item_layout, parent, false);
            holder = new LPRecyclerViewHolder(binding);
        } else {
            ViewDataBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.recycler_view_load_more, parent, false);
            holder = new LPLoadMoreViewHolder(binding);
        }

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof LPHeaderViewHolder) {
            Log.d(LPConstants.TAG, "LPHeaderViewHolder set.");
            setLPHeaderViewHolder((LPHeaderViewHolder) holder);
        } else if (holder instanceof LPFooterViewHolder) {
            setLPFooterViewHolder((LPFooterViewHolder) holder);
        } else if (holder instanceof LPRecyclerViewHolder) {
            int actualPostion = position;
            if (mHeaderBinding != null) {
                actualPostion = actualPostion - 1;
            }
            final E item = mData.get(actualPostion);
            //固定格式，采用databinding设计，因此item的layout中必须包含data variable
            ((LPRecyclerViewHolder) holder).getBinding().setVariable(item_data_variable, item);
            ((LPRecyclerViewHolder) holder).getBinding().executePendingBindings();
        } else {
            ((RecyclerViewLoadMoreBinding) ((LPLoadMoreViewHolder) holder).getBinding()).lpLoadMore.setIndeterminate(true);
        }
    }

    private void setLPHeaderViewHolder(LPHeaderViewHolder holder) {
        this.lpHeaderViewHolder = holder;
    }

    public LPFooterViewHolder getLPFooterViewHolder() {
        return lpFooterViewHolder;
    }

    private void setLPFooterViewHolder(LPFooterViewHolder holder) {
        this.lpFooterViewHolder = holder;
    }

    public LPHeaderViewHolder getLPHeaderViewHolder() {
        return lpHeaderViewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        if (mHeaderBinding != null && position == 0) {
            return TYPE_HEADER;
        }
        if (mFooterBinding != null && position == getItemCount() - 1) {
            return TYPE_FOOTER;
        }
        int actualPostion = position;
        if (mHeaderBinding != null) {
            actualPostion = actualPostion - 1;
        }
        return mData.get(actualPostion) != null ? TYPE_ITEM : TYPE_PROG;
    }

    @Override
    public int getItemCount() {
        if (mHeaderBinding != null && mFooterBinding != null) {
            return mData.size() + 2;
        }
        if (mHeaderBinding != null || mFooterBinding != null) {
            return mData.size() + 1;
        }
        return mData.size();
    }

    public void setOnLoadMoreListener(LPRefreshLoadListener.OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public void setOnRefreshListener(LPRefreshLoadListener.onRefreshListener onRefreshListener) {
        this.onRefreshListener = onRefreshListener;
    }

    /**
     * 设置是否显示加载更多视图，若存在加载更多则一定显示footer
     *
     * @param isProgress true:显示 false:不显示
     */
    public synchronized void showLoadingMore(final boolean isProgress) {
        setLoadingMore(isProgress);
        if (isProgress) {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    mData.add(null);
                    int actualSize = mData.size();
                    if (mHeaderBinding != null) {
                        actualSize = actualSize + 1;
                    }
                    notifyItemInserted(actualSize - 1);
                }
            });
        } else {
            int actualSize = mData.size();
            if (mHeaderBinding != null) {
                actualSize = actualSize + 1;
            }
            mData.remove(mData.size() - 1);
            notifyItemRemoved(actualSize);
        }
    }

    /**
     * 加载数据项
     */
    public static class LPRecyclerViewHolder extends RecyclerView.ViewHolder {

        private ViewDataBinding binding;

        public LPRecyclerViewHolder(ViewDataBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public ViewDataBinding getBinding() {
            return binding;
        }
    }

    /**
     * 加载更多
     */
    public static class LPLoadMoreViewHolder extends RecyclerView.ViewHolder {

        private ViewDataBinding binding;

        public LPLoadMoreViewHolder(ViewDataBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public ViewDataBinding getBinding() {
            return binding;
        }
    }

    /**
     * header
     */
    public static class LPHeaderViewHolder extends RecyclerView.ViewHolder {

        private ViewDataBinding binding;

        public LPHeaderViewHolder(ViewDataBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public ViewDataBinding getBinding() {
            return binding;
        }
    }

    /**
     * footer
     */
    public static class LPFooterViewHolder extends RecyclerView.ViewHolder {

        private ViewDataBinding binding;

        public LPFooterViewHolder(ViewDataBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public ViewDataBinding getBinding() {
            return binding;
        }
    }

    /**
     * 数据加载成功
     *
     * @param data      列表数据
     * @param pageCount 页数
     */
    public void loadDataSuccess(ArrayList<E> data, int pageCount) {
        setPageCount(pageCount);
        // TODO: 16/11/26 数据是否这样更新有待优化
        if (isLoadingMore()) {
            loadMoreSuccess();
            showLoadingMore(false);
        } else {
            mData.clear();
            if (mSwipeRefreshLayout.isRefreshing()) {
                setPageCurrentNum(getPageStartNum());
            }
        }
        mData.addAll(data);
        notifyDataSetChanged();
        loadDataFailed();
    }

    /**
     * 数据加载失败时调用
     */
    public void loadDataFailed() {
        if (isLoadingMore()) {
            showLoadingMore(false);
        }
        if (mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

}
