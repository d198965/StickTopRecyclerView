package com.zdh.demo;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.widgets.hai.sticktoprecyclerviewdemo.R;
import com.zdh.widgets.StickTopRecyclerView;

import java.util.ArrayList;

/**
 * Created by hai on 2017/7/16.
 */

public class MainActivity extends Activity {
    private StickTopRecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private FrameLayout mContainer;
    private Button mSetButton;
    private StickTopRecyclerView.SetTopParams mParams;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mParams = new StickTopRecyclerView.SetTopParams();
        mParams.marginTopHeight = 0;

        setContentView(R.layout.main_layout);

        mRecyclerView = (StickTopRecyclerView) findViewById(R.id.top_recycler_view);
        mLayoutManager = new LinearLayoutManager(MainActivity.this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new DefaultDividerItemDecoration(MainActivity.this, R.drawable.main_divider));
        mContainer = (FrameLayout) findViewById(R.id.top_container);
        mSetButton = (Button) findViewById(R.id.set_button);
        mSetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mContainer != null && mRecyclerView != null) {
                    if (mParams.marginTopHeight == 0) {
                        mParams.marginTopHeight = 100;
                        mSetButton.setText("重置");
                    } else {
                        mParams.marginTopHeight = 0;
                        mSetButton.setText("距离顶部100置顶");
                    }
                    mRecyclerView.updateSetTopParams(mParams);
                    mRecyclerView.invalidate();
                }
            }
        });

        initValues();
    }


    private void initValues() {
        ArrayList<String> topValues = new ArrayList<>();
        for (int k = 0; k < 16; k++) {
            topValues.add("测试" + k);
        }
        MyAdapter adapter = new MyAdapter(topValues);
        mRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private class MyAdapter extends RecyclerView.Adapter<MyHolder> {
        private final int TOP_VIEW_TYPE = 1;
        private final int ORIGIN_VIEW_TYPE = 0;
        private ArrayList<String> itemValues;
        private TextView topView;

        public MyAdapter(ArrayList<String> messages) {
            itemValues = messages;
        }

        @Override
        public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            if (viewType == TOP_VIEW_TYPE) {
                topView = new TextView(MainActivity.this);
                topView.setGravity(Gravity.CENTER);
                topView.setTextSize(16);
                topView.setTextColor(getResources().getColor(R.color.orange));
                topView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 200));
                topView.setBackgroundResource(R.color.green);

                View emptyView = mRecyclerView.setTopView(mContainer, topView, mParams);
                return new MyHolder(emptyView);
            } else {
                TextView textView = new TextView(MainActivity.this);
                textView.setGravity(Gravity.CENTER);
                textView.setTextSize(15);
                textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 400));
                textView.setBackgroundResource(R.color.background);

                return new MyHolder(textView);
            }
        }

        @Override
        public void onBindViewHolder(MyHolder holder, int position) {
            if (getItemViewType(position) == TOP_VIEW_TYPE) {
                topView.setText("置顶TextView" + itemValues.get(position));
            } else {
                ((TextView) holder.itemView).setText(itemValues.get(position));
            }
        }

        @Override
        public int getItemCount() {
            return itemValues.size();
        }

        @Override
        public int getItemViewType(int position) {
            return position == 3 ? TOP_VIEW_TYPE : ORIGIN_VIEW_TYPE;
        }
    }

    private class MyHolder extends RecyclerView.ViewHolder {
        public View itemView;

        public MyHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
        }
    }

    public class DefaultDividerItemDecoration extends RecyclerView.ItemDecoration {
        private Drawable mDivider;
        public DefaultDividerItemDecoration(Context context, int divider) {
            mDivider = context.getResources().getDrawable(divider);
        }

        @Override
        public void onDraw(Canvas c, RecyclerView parent) {
            drawVertical(c, parent);
        }


        public void drawVertical(Canvas c, RecyclerView parent) {
            final int left = parent.getPaddingLeft();
            final int right = parent.getWidth() - parent.getPaddingRight();

            final int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                final View child = parent.getChildAt(i);
                final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                        .getLayoutParams();
                final int top = child.getBottom() + params.bottomMargin;
                final int bottom = top + mDivider.getIntrinsicHeight();
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }
        }

        @Override
        public void getItemOffsets(Rect outRect, int itemPosition, RecyclerView parent) {
            outRect.set(0, 0, 0, mDivider.getIntrinsicHeight());
        }
    }
}
