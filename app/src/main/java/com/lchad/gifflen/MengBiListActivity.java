package com.lchad.gifflen;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MengBiListActivity extends AppCompatActivity {

    @BindView(R.id.recycler_view) RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meng_bi_list);
        ButterKnife.bind(this);
        GridLayoutManager manager = new GridLayoutManager(MengBiListActivity.this, 2);

        MengbiAdapter mengbiAdapter = new MengbiAdapter(MengBiListActivity.this);
        mRecyclerView.addItemDecoration(new SpaceItemDecoration(20));
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(mengbiAdapter);
    }

    private static class MengbiAdapter extends RecyclerView.Adapter<MengbiViewHolder> {

        private final Context context;

        private TypedArray mDrawableList;

        public MengbiAdapter(Context context) {
            this.context = context;
            mDrawableList = context.getResources().obtainTypedArray(R.array.source);
        }

        @Override
        public MengbiViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MengbiViewHolder(LayoutInflater.from(context).inflate(R.layout.item_mengbi, null));
        }

        @Override
        public void onBindViewHolder(MengbiViewHolder holder, int position) {
            holder.bind(position, mDrawableList.getResourceId(position, -1));
        }

        @Override
        public int getItemCount() {
            return mDrawableList.length();
        }
    }

    static class MengbiViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.image) ImageView mImageView;
        @BindView(R.id.position) TextView mTextView;

        public MengbiViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(int position, int resId) {
            mImageView.setImageResource(resId);
            mTextView.setText("" + (position + 1) + "");
        }
    }

    private static class SpaceItemDecoration extends RecyclerView.ItemDecoration {

        private int space;

        public SpaceItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            if (parent.getChildLayoutPosition(view) < 2) {
                outRect.top = space;
            }
            if (parent.getChildLayoutPosition(view) % 2 == 0) {
                outRect.left = space;
                outRect.bottom = space;
            } else {
                outRect.left = space;
                outRect.bottom = space;
                outRect.right = space;
            }
        }
    }
}


