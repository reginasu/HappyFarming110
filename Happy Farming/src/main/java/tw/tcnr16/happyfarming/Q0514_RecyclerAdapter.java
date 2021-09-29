package tw.tcnr16.happyfarming;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class Q0514_RecyclerAdapter extends RecyclerView.Adapter<Q0514_RecyclerAdapter.ViewHolder> implements View.OnClickListener {
    private Context mContext;
    private ArrayList<Q0514_Post> mData;
    private OnItemClickListener mOnItemClickListener = null;
    public Q0514_RecyclerAdapter(Context context, ArrayList<Q0514_Post> data) {
        this.mContext = context;
        this.mData = data;
    }

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(mContext)
                .inflate(R.layout.q0514_cell_post, parent, false);
        ViewHolder holder = new ViewHolder(view);
        holder.img = (ImageView) view.findViewById(R.id.img);
        holder.Name = (TextView) view.findViewById(R.id.Name);
        holder.Coordinate = (TextView) view.findViewById(R.id.ProduceOrg);
        holder.Tel = (TextView) view.findViewById(R.id.ContactTel);
        holder.Content = (TextView) view.findViewById(R.id.Content);
        holder.Address = (TextView) view.findViewById(R.id.SalePlace);
        view.setOnClickListener(this);
        return holder;
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Q0514_Post post = mData.get(position);
        holder.Name.setText(post.Name);
        holder.Coordinate.setText(post.Coordinate);
        holder.Tel.setText(post.Tel);
        holder.Address.setText(post.Address);

        Glide.with(mContext)
                .load(post.posterThumbnailUrl)
                .override(100, 75)
                .transition(withCrossFade())
                .error(
                        Glide.with(mContext)
                                .load("https://tcnr2021a116.000webhostapp.com/post_img/nopic1.jpg")) // 無提供照片的話 使用此網址的照片
                .into(holder.img);

        holder.itemView.setTag(position);
    }
    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(v, (int) v.getTag());
        }
    }
    public static interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
    class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView img;
        public TextView Name;
        public TextView Address,Introduction;
        public TextView Content, Coordinate, Tel;
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
