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

public class MengBiListActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;

    private TypedArray mDrawableList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meng_bi_list);
        mDrawableList = getResources().obtainTypedArray(R.array.source);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        GridLayoutManager manager = new GridLayoutManager(MengBiListActivity.this, 2);

        MengbiAdapter mengbiAdapter = new MengbiAdapter(MengBiListActivity.this);
        mRecyclerView.addItemDecoration(new SpaceItemDecoration(20));
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(mengbiAdapter);
    }

    class MengbiAdapter extends RecyclerView.Adapter<MengbiViewHolder> {

        private final Context context;

        public MengbiAdapter(Context context) {
            this.context = context;
        }

        @Override
        public MengbiViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MengbiViewHolder(LayoutInflater.from(context).inflate(R.layout.item_mengbi, null));
        }

        @Override
        public void onBindViewHolder(MengbiViewHolder holder, int position) {
            holder.bind(position);
        }

        @Override
        public int getItemCount() {
            return mDrawableList.length();
        }
    }

    class MengbiViewHolder extends RecyclerView.ViewHolder {

        private ImageView mImageView;

        private TextView mTextView;

        public MengbiViewHolder(View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.image);
            mTextView = (TextView) itemView.findViewById(R.id.position);
        }

        public void bind(int position) {
            mImageView.setImageResource(mDrawableList.getResourceId(position, -1));
            mTextView.setText("" + (position + 1) + "");
        }
    }

    class SpaceItemDecoration extends RecyclerView.ItemDecoration {

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


