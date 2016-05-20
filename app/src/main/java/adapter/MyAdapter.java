package adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.psj.qiushibaike.R;

import java.util.ArrayList;
import java.util.List;

import data.Data;

/**
 * Created by psj on 2016/5/17.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private static List<Data.ItemsEntity> newsDates = new ArrayList<>();
    public static Context mContext;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case

        public TextView textView, postTime;
        public SimpleDraweeView titleImg;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.e("MyAdapter", "当前的位置:" + getLayoutPosition());

                }
            });
            textView = (TextView) itemView.findViewById(R.id.info_text);
            postTime = (TextView) itemView.findViewById(R.id.post_time);
            titleImg = (SimpleDraweeView) itemView.findViewById(R.id.title_img);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(Context context) {
        mContext = context;
        Fresco.initialize(context);
    }

    public void setData(List<Data.ItemsEntity> myDataset) {
        this.newsDates = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                         int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_text_view, parent, false);
//        TextView textView = (TextView) v.findViewById(R.id.info_text);
        // set the view's size, margins, paddings and layout parameters
//        ViewHolder vh = new ViewHolder(textView);
        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.textView.setText(newsDates.get(position).getContent());
        if (newsDates.get(position).getImage() != null) {
            String url = newsDates.get(position).getImage() + "";
            String str = "http://pic.qiushibaike.com/system/pictures/11641/116410597/medium/app116410597.jpg";
            String str1 = "http://pic.qiushibaike.com/system/pictures/";
            String str2 = url.substring(3, 8) + "/";
            String str3 = url.substring(3, 12) + "/";
            String str4 = "medium/" + url;
            url = str1 + str2 + str3 + str4;
            Log.e("url",url);
            holder.titleImg.setVisibility(View.VISIBLE);
            holder.titleImg.setImageURI(Uri.parse(url));
        } else {
            holder.titleImg.setVisibility(View.GONE);
        }
//        holder.postTime.setText(newsDates.get(position).getPostdate());
//        holder.titleImg.setImageURI(Uri.parse(newsDates.get(position).getImage()));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return newsDates.size();
    }
}


