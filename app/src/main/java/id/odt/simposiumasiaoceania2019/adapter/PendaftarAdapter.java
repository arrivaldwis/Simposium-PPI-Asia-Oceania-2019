package id.odt.simposiumasiaoceania2019.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.pixplicity.fontview.FontAppCompatTextView;
import com.squareup.picasso.Picasso;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.text.NumberFormat;
import java.util.ArrayList;
import id.odt.simposiumasiaoceania2019.BaseApp;
import id.odt.simposiumasiaoceania2019.R;
import id.odt.simposiumasiaoceania2019.model.UserModel;

public class PendaftarAdapter extends RecyclerView.Adapter<PendaftarAdapter.ViewHolder> {

    Context mContext;
    private ArrayList<UserModel> items;
    private ProgressDialog pDialog;

    public PendaftarAdapter(ArrayList<UserModel> items, Context mContext) {
        this.items = items;
        this.mContext = mContext;
        pDialog = new ProgressDialog(mContext);
        pDialog.setMessage("Harap tunggu...");
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pendaftar_detail, parent, false);
        return new ViewHolder(v);
    }

    private long value = 0;

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final UserModel item = items.get(position);
        Picasso.get().load(item.getBukti_url()).into(holder.imgBukti);
        holder.tvNama.setText(item.getNama());
        holder.tvKota.setText("Asal: " + item.getKota() + ", " + item.getNegara());
        holder.tvPassport.setText("No. Passport: " + item.getNo_passport());
        holder.btnApprove.setOnClickListener(v -> updateApprove(item.getUid()));
        holder.llBukti.setOnClickListener(v -> {
            if (holder.expandable.isExpanded()) {
                holder.expandable.collapse();
            } else
                holder.expandable.expand();
        });
    }

    private void updateApprove(String uid) {
        pDialog.show();
        BaseApp.db
                .collection("user")
                .whereEqualTo("uid", uid)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.getResult() != null) {
                        if (task.getResult().size() > 0) {
                            for (DocumentSnapshot documentSnapshot : task.getResult()) {
                                String key = documentSnapshot.getId();
                                BaseApp.db
                                        .collection("user")
                                        .document(key)
                                        .update("approve", true)
                                        .addOnSuccessListener(aVoid -> {
                                            Toast.makeText(mContext, "Pendaftar berhasil di approve!", Toast.LENGTH_SHORT).show();
                                            pDialog.dismiss();
                                        });
                            }
                        }
                    }
                });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public FontAppCompatTextView tvNama, tvKota, tvPassport, btnApprove;
        public ImageView imgBukti;
        public LinearLayout llBukti;
        public ExpandableLayout expandable;

        public ViewHolder(View v) {
            super(v);
            tvNama = v.findViewById(R.id.tv_nama);
            tvKota = v.findViewById(R.id.tv_kota);
            tvPassport = v.findViewById(R.id.tv_passport);
            btnApprove = v.findViewById(R.id.btn_approve);
            imgBukti = v.findViewById(R.id.img_bukti);
            llBukti = v.findViewById(R.id.ll_bukti);
            expandable = v.findViewById(R.id.expandable);
        }
    }
}
