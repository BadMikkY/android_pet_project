package el.fit.bstu.by.collectionapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import el.fit.bstu.by.collectionapp.entity.Item;
import el.fit.bstu.by.collectionapp.middleware.DbBitmapUtility;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    private final LayoutInflater inflater;
    private final List<Item> items;

    interface OnStateClickListener {
        void onStateClick(Item item, int position);
    }

    private final OnStateClickListener onClickListener;

    public ItemAdapter(Context context, List<Item> items, OnStateClickListener onClickListener) {
        this.items = items;
        this.inflater = LayoutInflater.from(context);
        this.onClickListener = onClickListener;
    }

    @Override
    public ItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = inflater.inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Item item = items.get(position);
        holder.nameView.setText(item.getName());
        holder.categoryView.setText(item.getCategory());

        try{
            holder.imageView.setImageBitmap(DbBitmapUtility.getImage(item.getImage()));
        }
        catch (Exception e){

        }
        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                onClickListener.onStateClick(item, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView nameView, categoryView;
        final ImageView imageView;
        ViewHolder(View view){
            super(view);
            nameView = view.findViewById(R.id.name);
            categoryView = view.findViewById(R.id.category);
            imageView = view.findViewById(R.id.image);
        }
    }
}
