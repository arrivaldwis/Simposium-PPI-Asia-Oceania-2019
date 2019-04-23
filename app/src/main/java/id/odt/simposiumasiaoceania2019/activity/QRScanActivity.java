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

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import id.odt.simposiumasiaoceania2019.BaseApp;
import id.odt.simposiumasiaoceania2019.R;
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

    private void showDialogAction(String result) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_user_action);
        FontAppCompatTextView tv_nama = dialog.findViewById(R.id.tv_peserta);
        RadioButton rb_registrasi = dialog.findViewById(R.id.rb_registrasi);
        RadioButton rb_toilet = dialog.findViewById(R.id.rb_toilet);
        RadioButton rb_sakit = dialog.findViewById(R.id.rb_sakit);
        RadioButton rb_lainnya = dialog.findViewById(R.id.rb_lain);
        LinearLayout btn_submit = dialog.findViewById(R.id.btn_submit);

        rb_registrasi.setOnCheckedChangeListener(this);
        rb_toilet.setOnCheckedChangeListener(this);
        rb_sakit.setOnCheckedChangeListener(this);
        rb_lainnya.setOnCheckedChangeListener(this);

        BaseApp.db
                .collection("user")
                .whereEqualTo("uid", result)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.getResult() != null) {
                        if (task.getResult().size() > 0) {
                            for (DocumentSnapshot documentSnapshot : task.getResult()) {
                                nama = documentSnapshot.getData().get("nama").toString();
                                tv_nama.setText(nama);
                            }
                        }
                    }
                });

        btn_submit.setOnClickListener(v -> {
            pDialog.show();
            btn_submit.setEnabled(false);

            Map<String, Object> actions = new HashMap<>();
            actions.put("uid", result);
            actions.put("nama", nama);
            actions.put("action", action);
            actions.put("panitia_uid", currentUser.getUid());
            actions.put("created_at", System.currentTimeMillis());

            BaseApp.db
                    .collection("action")
                    .add(actions)
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful()) {
                            pDialog.dismiss();
                            Toast.makeText(QRScanActivity.this, "Perintah berhasil dikirim", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            finish();
                        }
                    });
        });

        dialog.show();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked)
            action = buttonView.getText().toString().toLowerCase();
    }
}
