package id.odt.simposiumasiaoceania2019.activity;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import id.odt.simposiumasiaoceania2019.BaseApp;
import id.odt.simposiumasiaoceania2019.R;
import id.odt.simposiumasiaoceania2019.model.PanitiaModel;
import id.odt.simposiumasiaoceania2019.model.UserModel;


public class Excel2FirestoreActivity extends AppCompatActivity {

    private List<UserModel> weatherSamples = new ArrayList<>();
    private List<PanitiaModel> panitiaList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_excel2_firestore);
        //readPeserta();
        //readPanitia();
    }



    private void readPeserta() {
        // Read the raw csv file
        InputStream is = getResources().openRawResource(R.raw.peserta);

        // Reads text from character-input stream, buffering characters for efficient reading
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(is, Charset.forName("UTF-8"))
        );

        // Initialization
        String line = "";

        // Initialization
        try {
            // Step over headers
            reader.readLine();

            // If buffer is not empty
            while ((line = reader.readLine()) != null) {
                Log.d("MyActivity", "Line: " + line);
                // use comma as separator columns of CSV
                String[] tokens = line.split(",");
                // Read the data
                UserModel sample = new UserModel();

                // Setters
                sample.setAlergi(tokens[10].trim());
                sample.setGender(tokens[1].trim());
                sample.setNama(tokens[0].trim());
                sample.setJurusan(tokens[7].trim());
                sample.setKota(tokens[4].trim());
                sample.setNegara(tokens[5].trim());
                sample.setNo_passport(tokens[2].trim());
                sample.setNohp(tokens[11].trim());
                sample.setApprove(true);
                sample.setBukti_url("");
                sample.setCreated_at(System.currentTimeMillis());
                sample.setPuasa(tokens[8].replace(" ","").equals("TIDAK") ? false : true);

                ArrayList<String> status = new ArrayList<>();
                for (String s : tokens[3].split("\\*")) {
                    status.add(s);
                }

                sample.setStatus(status);
                sample.setUniversitas(tokens[6].trim());
                sample.setVege(tokens[9].replace(" ","").equals("TIDAK") ? false : true);
                sample.setNet_dinner(tokens[15].replace(" ","").equals("TIDAK") ? false : true);
                sample.setWechat(tokens[12].trim());
                sample.setWhatsapp(tokens[13].trim());
                sample.setUid("");
                sample.setEmail(tokens[14].trim().toLowerCase().replace(" ",""));

                BaseApp.db
                        .collection("user")
                        .whereEqualTo("email", tokens[14].trim().toLowerCase().replace(" ",""))
                        .get()
                        .addOnCompleteListener(task -> {
                            if(task.getResult()!=null) {
                                if(task.getResult().size()>0) {
                                    for (DocumentSnapshot documentSnapshot:task.getResult()) {
                                        BaseApp.db
                                                .collection("user")
                                                .document(documentSnapshot.getId())
                                                .update("net_dinner", tokens[15].equals("0") ? false : true);
                                    }
                                } else {
                                    // Adding object to a class
                                    BaseApp.mAuth.createUserWithEmailAndPassword(sample.getEmail(), "simpo19!")
                                            .addOnCompleteListener(Excel2FirestoreActivity.this, task1 -> {
                                                if (task1.isSuccessful()) {
                                                    // Sign in success, update UI with the signed-in user's information
                                                    Log.d("", "createUserWithEmail:success");
                                                    FirebaseUser users = BaseApp.mAuth.getCurrentUser();

                                                    BaseApp.db
                                                            .collection("user")
                                                            .add(sample)
                                                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                                @Override
                                                                public void onSuccess(DocumentReference documentReference) {
                                                                    BaseApp.db
                                                                            .collection("user")
                                                                            .document(documentReference.getId())
                                                                            .update("uid",documentReference.getId());
                                                                }
                                                            });
                                                    //updateUI(user);
                                                } else {
                                                    // If sign in fails, display a message to the user.
                                                    Log.w("", "createUserWithEmail:failure", task1.getException());
                                                    Toast.makeText(Excel2FirestoreActivity.this, "Authentication failed.",
                                                            Toast.LENGTH_SHORT).show();
                                                    //updateUI(null);
                                                }

                                                // ...
                                            });

                                    // Log the object
                                    Log.d("My Activity", "Just created: " + sample);
                                }
                            } else {// Adding object to a class
                                BaseApp.mAuth.createUserWithEmailAndPassword(sample.getEmail(), "simpo19!")
                                        .addOnCompleteListener(Excel2FirestoreActivity.this, task1 -> {
                                            if (task1.isSuccessful()) {
                                                // Sign in success, update UI with the signed-in user's information
                                                Log.d("", "createUserWithEmail:success");
                                                FirebaseUser users = BaseApp.mAuth.getCurrentUser();

                                                BaseApp.db
                                                        .collection("user")
                                                        .add(sample)
                                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                            @Override
                                                            public void onSuccess(DocumentReference documentReference) {
                                                                BaseApp.db
                                                                        .collection("user")
                                                                        .document(documentReference.getId())
                                                                        .update("uid",documentReference.getId());
                                                            }
                                                        });
                                                //updateUI(user);
                                            } else {
                                                // If sign in fails, display a message to the user.
                                                Log.w("", "createUserWithEmail:failure", task1.getException());
                                                Toast.makeText(Excel2FirestoreActivity.this, "Authentication failed.",
                                                        Toast.LENGTH_SHORT).show();
                                                //updateUI(null);
                                            }

                                            // ...
                                        });

                                // Log the object
                                Log.d("My Activity", "Just created: " + sample);
                            }
                        });
            }

            for (UserModel user : weatherSamples) {

            }

        } catch (IOException e) {
            // Logs error with priority level
            Log.wtf("MyActivity", "Error reading data file on line" + line, e);

            // Prints throwable details
            e.printStackTrace();
        }
    }

    boolean isExists = false;
    String uid = "";

    private void readPanitia() {
        // Read the raw csv file
        InputStream is = getResources().openRawResource(R.raw.panitia_new);

        // Reads text from character-input stream, buffering characters for efficient reading
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(is, Charset.forName("UTF-8"))
        );

        // Initialization
        String line = "";

        // Initialization
        try {
            // Step over headers
            reader.readLine();

            // If buffer is not empty
            while ((line = reader.readLine()) != null) {
                Log.d("MyActivity", "Line: " + line);
                // use comma as separator columns of CSV
                String[] tokens = line.split(",");
                // Read the data
                PanitiaModel sample = new PanitiaModel();

                // Setters
                sample.setEmail(tokens[1].trim().toLowerCase());
                sample.setNama(tokens[0].trim().toUpperCase());
                sample.setRole(tokens[2].trim());
                sample.setUid("");

                // Adding object to a class
                panitiaList.add(sample);

                // Log the object
                Log.d("My Activity", "Just created: " + sample);
            }

            for (PanitiaModel user : panitiaList) {

                BaseApp.mAuth.createUserWithEmailAndPassword(user.getEmail(), "@simpo19")
                        .addOnCompleteListener(Excel2FirestoreActivity.this, task1 -> {
                            if (task1.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("", "createUserWithEmail:success");
                                FirebaseUser users = BaseApp.mAuth.getCurrentUser();

                                BaseApp.db
                                        .collection("panitia")
                                        .add(user)
                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                BaseApp.db
                                                        .collection("panitia")
                                                        .document(documentReference.getId())
                                                        .update("uid",documentReference.getId());
                                            }
                                        });
                                //updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("", "createUserWithEmail:failure", task1.getException());
                                Toast.makeText(Excel2FirestoreActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                //updateUI(null);
                            }

                            // ...
                        });
            }

        } catch (IOException e) {
            // Logs error with priority level
            Log.wtf("MyActivity", "Error reading data file on line" + line, e);

            // Prints throwable details
            e.printStackTrace();
        }
    }
}
