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

public class Q0515_RecyclerAdapter extends RecyclerView.Adapter<Q0515_RecyclerAdapter.ViewHolder> implements View.OnClickListener {
    private Context mContext;
    private ArrayList<Q0515_Post> mData;
    private OnItemClickListener mOnItemClickListener = null;
    public Q0515_RecyclerAdapter(Context context, ArrayList<Q0515_Post> data) {
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
                .inflate(R.layout.q0515_cell_post, parent, false);
        ViewHolder holder = new ViewHolder(view);
        holder.FarmNm_CH = (TextView) view.findViewById(R.id.Name);
        holder.WebURL = (TextView) view.findViewById(R.id.ProduceOrg);
        holder.TEL= (TextView) view.findViewById(R.id.ContactTel);
        holder.Content = (TextView) view.findViewById(R.id.Content);
        holder.Address_CH = (TextView) view.findViewById(R.id.SalePlace);
        view.setOnClickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Q0515_Post post = mData.get(position);
        holder.FarmNm_CH.setText(post.FarmNm_CH);
        holder.WebURL.setText(post.WebURL);
        holder.TEL.setText(post.TEL);
        holder.Address_CH.setText(post.Address_CH);
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
        public TextView FarmNm_CH;
        public TextView Address_CH,Introduction;
        public TextView Content, WebURL, TEL;
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

}
