package com.ruetgmail.taufiqur.tipsntricks.adapters;

import android.app.Activity;
import android.content.Context;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ruetgmail.taufiqur.tipsntricks.R;
import com.ruetgmail.taufiqur.tipsntricks.listeners.ListItemClickListener;
import com.ruetgmail.taufiqur.tipsntricks.models.content.Contents;

import java.util.ArrayList;

public class ContentAdapter extends RecyclerView.Adapter<ContentAdapter.ViewHolder> {

    private Context mContext;
    private Activity mActivity;

    private ArrayList<Contents> mContentList;
    private ListItemClickListener mItemClickListener;

    public ContentAdapter(Context mContext, Activity mActivity, ArrayList<Contents> mContentList) {
        this.mContext = mContext;
        this.mActivity = mActivity;
        this.mContentList = mContentList;
    }

    public void setItemClickListener(ListItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_recycler, parent, false);
        return new ViewHolder(view, viewType, mItemClickListener);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private CardView cardView;
        private ImageView imgPost;
        private TextView tvTitle, tvDescription;
        private ImageButton btnFav;
        private ListItemClickListener itemClickListener;


        public ViewHolder(View itemView, int viewType, ListItemClickListener itemClickListener) {
            super(itemView);

            this.itemClickListener = itemClickListener;
            // Find all views ids
            cardView = (CardView) itemView.findViewById(R.id.card_view_top);
            imgPost = (ImageView) itemView.findViewById(R.id.post_img);
            tvTitle = (TextView) itemView.findViewById(R.id.title_text);
            tvDescription = (TextView) itemView.findViewById(R.id.description_text);
            btnFav = (ImageButton) itemView.findViewById(R.id.btn_fav);

            btnFav.setOnClickListener(this);
            cardView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            if (itemClickListener != null) {
                itemClickListener.onItemClick(getLayoutPosition(), view);
            }
        }
    }

    @Override
    public int getItemCount() {
        return (null != mContentList ? mContentList.size() : 0);

    }

    @Override
    public void onBindViewHolder(ContentAdapter.ViewHolder mainHolder, int position) {
        final Contents model = mContentList.get(position);

        // setting data over views
        String imgUrl = model.getImageUrl();
        if (imgUrl != null && !imgUrl.isEmpty()) {
            Glide.with(mContext)
                    .load(imgUrl)
                    .into(mainHolder.imgPost);
        }

        if (model.isFavorite()) {
            mainHolder.btnFav.setImageResource(R.drawable.ic_fav);
        } else {
            mainHolder.btnFav.setImageResource(R.drawable.ic_un_fav);
        }

        mainHolder.tvTitle.setText(Html.fromHtml(model.getTitle()));
        mainHolder.tvDescription.setText(Html.fromHtml(model.getSubTitle()));
    }
}