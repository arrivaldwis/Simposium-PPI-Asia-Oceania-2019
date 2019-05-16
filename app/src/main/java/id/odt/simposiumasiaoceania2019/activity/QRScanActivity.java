package id.odt.simposiumasiaoceania2019.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.zxing.Result;
import com.pixplicity.fontview.FontAppCompatTextView;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import id.odt.simposiumasiaoceania2019.BaseApp;
import id.odt.simposiumasiaoceania2019.R;
import id.odt.simposiumasiaoceania2019.util.Util;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class QRScanActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler, CompoundButton.OnCheckedChangeListener {

    private ZXingScannerView mScannerView;
    private FirebaseUser currentUser;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mScannerView = new ZXingScannerView(this);
        setContentView(mScannerView);
        currentUser = BaseApp.mAuth.getCurrentUser();
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Harap tunggu...");
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result rawResult) {
        Log.v("TAG", rawResult.getText()); // Prints scan results
        Log.v("TAG", rawResult.getBarcodeFormat().toString());
        showDialogAction(rawResult.getText());
        //mScannerView.resumeCameraPreview(this);
    }

    private String action = "registrasi";
    private String nama = "";
    private String textAdditional = "";
    private MaterialEditText et_lainnya;

    private void showDialogAction(String result) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_user_action);
        FontAppCompatTextView tv_nama = dialog.findViewById(R.id.tv_peserta);
        RadioButton rb_registrasi = dialog.findViewById(R.id.rb_registrasi);
        RadioButton rb_toilet = dialog.findViewById(R.id.rb_toilet);
        RadioButton rb_sakit = dialog.findViewById(R.id.rb_sakit);
        RadioButton rb_lainnya = dialog.findViewById(R.id.rb_lain);
        RadioButton rb_makan_pagi = dialog.findViewById(R.id.rb_makan_pagi);
        RadioButton rb_networking_dinner = dialog.findViewById(R.id.rb_networking_dinner);
        RadioButton rb_makan_siang = dialog.findViewById(R.id.rb_makan_siang);
        RadioButton rb_makan_malam = dialog.findViewById(R.id.rb_makan_malam);
        et_lainnya = dialog.findViewById(R.id.et_lainnya);
        LinearLayout btn_submit = dialog.findViewById(R.id.btn_submit);

        rb_registrasi.setOnCheckedChangeListener(this);
        rb_toilet.setOnCheckedChangeListener(this);
        rb_sakit.setOnCheckedChangeListener(this);
        rb_lainnya.setOnCheckedChangeListener(this);
        rb_makan_pagi.setOnCheckedChangeListener(this);
        rb_makan_siang.setOnCheckedChangeListener(this);
        rb_makan_malam.setOnCheckedChangeListener(this);
        rb_networking_dinner.setOnCheckedChangeListener(this);

        BaseApp.db
                .collection("user")
                .whereEqualTo("email", result)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.getResult() != null) {
                        if (task.getResult().size() > 0) {
                            for (DocumentSnapshot documentSnapshot : task.getResult()) {
                                nama = documentSnapshot.getData().get("nama").toString();
                                String alergi = documentSnapshot.getData().get("alergi").toString();
                                boolean puasa = (boolean) documentSnapshot.getData().get("puasa");
                                boolean vege = (boolean) documentSnapshot.getData().get("vege");
                                boolean net_dinner = (boolean) documentSnapshot.getData().get("net_dinner");

                                if (puasa)
                                    rb_makan_siang.setVisibility(View.GONE);
                                else
                                    rb_makan_pagi.setVisibility(View.GONE);

                                if (net_dinner)
                                    rb_networking_dinner.setVisibility(View.VISIBLE);
                                else
                                    rb_networking_dinner.setVisibility(View.GONE);

                                if (vege)
                                    textAdditional += "\nVegan: Ya";
                                else
                                    textAdditional += "\nVegan: Tidak";

                                if (!alergi.isEmpty())
                                    textAdditional += "\nAlergi: " + alergi;

                                tv_nama.setText(nama + textAdditional);

                            }
                        } else {
                            BaseApp.db
                                    .collection("panitia")
                                    .whereEqualTo("email", result)
                                    .get()
                                    .addOnCompleteListener(task1 -> {
                                        if (task1.getResult() != null) {
                                            if (task1.getResult().size() > 0) {
                                                for (DocumentSnapshot documentSnapshot : task1.getResult()) {
                                                    String email = documentSnapshot.getData().get("email").toString();
                                                    nama = documentSnapshot.getData().get("nama").toString();
                                                    tv_nama.setText(nama);
                                                }
                                            }
                                        }
                                    });
                        }
                    }
                });

        btn_submit.setOnClickListener(v -> {
            pDialog.show();
            btn_submit.setEnabled(false);

            if (action.equals("lainnya")) {
                action = et_lainnya.getText().toString().toLowerCase();
            }

            Map<String, Object> actions = new HashMap<>();
            actions.put("uid", result);
            actions.put("nama", nama);
            actions.put("action", action);
            actions.put("panitia_uid", currentUser.getUid());
            actions.put("created_at", System.currentTimeMillis());

            if (action.toLowerCase().contains("makan") || action.toLowerCase().contains("sarapan")) {
                BaseApp.db
                        .collection("action")
                        .whereEqualTo("email", result)
                        .whereEqualTo("action", action)
                        .get()
                        .addOnCompleteListener(task -> {
                            if (task.getResult() != null) {
                                if (task.getResult().size() > 0) {
                                    for (DocumentSnapshot documentSnapshot : task.getResult()) {
                                        long created_at = Long.parseLong(documentSnapshot.getData().get("created_at").toString());
                                        if (Util.getDate(created_at, "dd/MM/yyyy")
                                                .equals(Util.getDate(System.currentTimeMillis(), "dd/MM/yyyy"))) {
                                            Toast.makeText(QRScanActivity.this, "Maaf, peserta telah " + action + " sebelumnya", Toast.LENGTH_LONG).show();
                                            pDialog.dismiss();
                                            dialog.dismiss();
                                            mScannerView.resumeCameraPreview(QRScanActivity.this);
                                        }
                                    }
                                } else {
                                    addAction(actions);
                                    dialog.dismiss();
                                }
                            } else {
                                addAction(actions);
                                dialog.dismiss();
                            }
                        });
            } else {
                addAction(actions);
                dialog.dismiss();
            }


        });

        dialog.show();
    }

    private void addAction(Map<String, Object> actions) {
        BaseApp.db
                .collection("action")
                .add(actions)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        pDialog.dismiss();
                        Toast.makeText(QRScanActivity.this, "Perintah berhasil dikirim", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            action = buttonView.getText().toString().toLowerCase();
            if (action.equals("lainnya")) {
                et_lainnya.setVisibility(View.VISIBLE);
            } else {
                et_lainnya.setVisibility(View.GONE);
            }
        }
    }
}
