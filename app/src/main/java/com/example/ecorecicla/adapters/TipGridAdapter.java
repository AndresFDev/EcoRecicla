package com.example.ecorecicla.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.ecorecicla.R;
import com.example.ecorecicla.activities.TipsDetailActivity;
import com.example.ecorecicla.models.Message;
import com.example.ecorecicla.models.Tip;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;
import java.util.List;

public class TipGridAdapter extends RecyclerView.Adapter<TipGridAdapter.TipGridViewHolder> {
    private List<Tip> tipList;
    private Context context;

    public TipGridAdapter(List<Tip> tipList, Context context) {
        this.tipList = tipList;
        this.context = context;
    }

    @Override
    public TipGridViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tips_grid_item_layout, parent, false);
        return new TipGridViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TipGridViewHolder holder, int position) {
        Tip tip = tipList.get(position);
        holder.bind(tip);

        ArrayList<Message> messages = (ArrayList<Message>) tip.getMessages();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(holder.itemView.getContext(), TipsDetailActivity.class);
                intent.putExtra("title", tip.getTitle());
                intent.putExtra("stikerImage", tip.getStikerImage());
                intent.putParcelableArrayListExtra("messages", messages);
                context.startActivity(intent);
            }
        });

        holder.btnEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.itemView.getContext(), TipsDetailActivity.class);
                intent.putExtra("title", tip.getTitle());
                intent.putExtra("stikerImage", tip.getStikerImage());
                intent.putParcelableArrayListExtra("messages", messages);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return tipList.size();
    }

    public static class TipGridViewHolder extends RecyclerView.ViewHolder {

        private MaterialTextView tvTitle;
        private ImageView ivStiker;
        private Button btnEntry;
        private ShimmerFrameLayout sflStikers;

        public TipGridViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            sflStikers = itemView.findViewById(R.id.sflStikers);
            ivStiker = itemView.findViewById(R.id.ivStiker);
            btnEntry = itemView.findViewById(R.id.btnEntry);
        }

        public void bind(Tip tip) {
            sflStikers.startShimmer();

            tvTitle.setText(tip.getTitle());

            if (tip.getStikerImage() != null) {
                Glide.with(itemView.getContext())
                        .load(tip.getStikerImage())
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                ivStiker.setImageResource(R.drawable.img_not_available);
                                sflStikers.stopShimmer();
                                sflStikers.setVisibility(View.GONE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                sflStikers.stopShimmer();
                                sflStikers.setVisibility(View.GONE);
                                return false;
                            }
                        })
                        .into(ivStiker);
            } else {
                ivStiker.setImageResource(R.drawable.img_not_available);
                sflStikers.stopShimmer();
                sflStikers.setVisibility(View.GONE);
            }

        }
    }

}
