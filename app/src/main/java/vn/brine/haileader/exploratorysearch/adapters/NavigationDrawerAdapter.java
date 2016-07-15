package vn.brine.haileader.exploratorysearch.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import vn.brine.haileader.exploratorysearch.R;
import vn.brine.haileader.exploratorysearch.models.NavDrawerItem;

/**
 * Created by HaiLeader on 7/12/2016.
 */
public class NavigationDrawerAdapter extends RecyclerView.Adapter<NavigationDrawerAdapter.MyViewHolder> {
    List<NavDrawerItem> navDrawerItems = Collections.emptyList();
    private LayoutInflater inflater;
    private Context mContext;

    public NavigationDrawerAdapter(Context context, List<NavDrawerItem> navDrawerItems) {
        this.mContext = context;
        inflater = LayoutInflater.from(context);
        this.navDrawerItems = navDrawerItems;
    }

    public void delete(int position) {
        navDrawerItems.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.nav_drawer_row, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        NavDrawerItem current = navDrawerItems.get(position);
        holder.txtTitle.setText(current.getTitle());
        holder.imgIcon.setImageResource(current.getIcon());
        if(navDrawerItems.get(position).getCounterVisibility()){
            holder.txtCount.setText(current.getCount());
        }else{
            holder.txtCount.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return navDrawerItems.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitle;
        TextView txtCount;
        ImageView imgIcon;

        public MyViewHolder(View itemView) {
            super(itemView);
            txtTitle = (TextView) itemView.findViewById(R.id.title);
            imgIcon = (ImageView) itemView.findViewById(R.id.icon);
            txtCount = (TextView) itemView.findViewById(R.id.counter);
        }
    }
}