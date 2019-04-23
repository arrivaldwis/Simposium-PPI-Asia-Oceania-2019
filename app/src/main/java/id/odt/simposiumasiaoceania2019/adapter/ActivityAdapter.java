package id.odt.simposiumasiaoceania2019.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentSnapshot;
import com.pixplicity.fontview.FontAppCompatTextView;
import com.squareup.picasso.Picasso;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.ArrayList;

import id.odt.simposiumasiaoceania2019.BaseApp;
import id.odt.simposiumasiaoceania2019.R;
import id.odt.simposiumasiaoceania2019.model.ActionModel;
import id.odt.simposiumasiaoceania2019.model.UserModel;
import id.odt.simposiumasiaoceania2019.util.DateUtils;

public class ActivityAdapter extends RecyclerView.Adapter<ActivityAdapter.ViewHolder> {

    Context mContext;
    private ArrayList<ActionModel> items;

    public ActivityAdapter(ArrayList<ActionModel> items, Context mContext) {
        this.items = items;
        this.mContext = mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_activity_action, parent, false);
        return new ViewHolder(v);
    }

    private long value = 0;

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final ActionModel item = items.get(position);
        holder.tvNama.setText(item.getNama());
        holder.tvDate.setText(DateUtils.formatDateTime(item.getCreated_at()));
        holder.btnApprove.setText(item.getAction().toUpperCase());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public FontAppCompatTextView tvNama, tvDate, btnApprove;

        public ViewHolder(View v) {
            super(v);
            tvNama = v.findViewById(R.id.tv_nama);
            tvDate = v.findViewById(R.id.tv_date);
            btnApprove = v.findViewById(R.id.btn_approve);
        }
    }
}
