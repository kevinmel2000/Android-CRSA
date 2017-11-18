package ileos.crsaencryption.gui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.File;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import ileos.crsaencryption.R;
import ileos.crsaencryption.utils.UtitilityManager;
import ileos.crsaencryption.utils.FileChooser;

public class UI_Main extends AppCompatActivity {
    private Button encryptBtn, decryptBtn, openFileBtn, openKeyBtn;
    private AlertDialog.Builder logBuilder;
    private Spinner algorithmSpn;
    private TextView filePathTextView, keyPathTextView;

    private UtitilityManager mUtilManager = new UtitilityManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        openFileBtn = (Button) findViewById(R.id.chooseFileBtn);
        openKeyBtn = (Button) findViewById(R.id.openKeyBtn);
        encryptBtn = (Button) findViewById(R.id.encryptBtn);
        decryptBtn = (Button) findViewById(R.id.decryptBtn);
        filePathTextView = (TextView) findViewById(R.id.filePath);
        keyPathTextView = (TextView) findViewById(R.id.keyPath);
        algorithmSpn = (Spinner) findViewById(R.id.algorithmSpinner);
        List<String> list = new ArrayList<String>();
        list.add("RSA");
        list.add("Compression RSA");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        algorithmSpn.setAdapter(dataAdapter);

        logBuilder = new AlertDialog.Builder(
                UI_Main.this);
        logBuilder.setNeutralButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        dialog.dismiss();
                    }
                });

        //select file
        openFileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFile();
            }
        });

        //select key
        openKeyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openKey();
            }
        });

        //encrypt
        encryptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    encrypt();
            }
        });

        //decrypt
        decryptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                decrypt();
            }
        });

        /*
        Button comdecom = (Button) findViewById(R.id.comdecom);
        comdecom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UI_Main.this, ComDeCom.class);
                startActivity(intent);
            }
        });
        */
    }

    //open file dari directory android
    private void openFile(){
        new FileChooser(UI_Main.this).setFileListener(new FileChooser.FileSelectedListener() {
            @Override
            public void fileSelected(File file) {
                try {
                    mUtilManager.openFile(file.getAbsolutePath());
                    filePathTextView.setText("Path : " + file.getCanonicalPath());
                } catch (Exception e) {
                    logBuilder.setTitle("WARNING!!!");
                    logBuilder.setMessage("Incorrect File");
                    logBuilder.show();
                }
            }
        }).showDialog();
    }

    //open key dari directory android
    private void openKey(){
        new FileChooser(UI_Main.this).setFileListener(new FileChooser.FileSelectedListener() {
            @Override
            public void fileSelected(File file) {
                try {
                    mUtilManager.openKey(file.getAbsolutePath());
                    keyPathTextView.setText("Path : " + file.getCanonicalPath());
                } catch (Exception e1) {
                    logBuilder.setTitle("WARNING!!!");
                    logBuilder.setMessage("Incorret Key");
                    logBuilder.show();
                }
            }
        }).showDialog();
    }

    //btn encrypt
    private void encrypt(){
        try {
            mUtilManager.encryptPerformed(String.valueOf(algorithmSpn.getSelectedItem()).toString());

            logBuilder.setTitle(String.valueOf(algorithmSpn.getSelectedItem()) + " Encryption");
            logBuilder.setMessage("Used Time : " + mUtilManager.getUsedTime() + " ms"
                    + "\nUsed memory : " + mUtilManager.getUsedMemory() + " Kb"
                    + "\nPlaintext Size : " + mUtilManager.getPlainSize() +" Byte"
                    + "\nCompressed Plain Size : " + mUtilManager.getCompressedPlainSize()+" Byte");
            logBuilder.show();
        } catch (Exception e) {
            logBuilder.setTitle("WARNING!!");
            logBuilder.setMessage(e.getMessage());
            logBuilder.show();
        }
    }

   //btn decrypt
    private void decrypt(){
        try {
            mUtilManager.decryptPerformed(String.valueOf(algorithmSpn.getSelectedItem()));

            logBuilder.setTitle(String.valueOf(algorithmSpn.getSelectedItem()) + " Decryption");
            logBuilder.setMessage("Used Time : " +
                    mUtilManager.getUsedTime() + " ms"+
                    "\nUsed memory : " + mUtilManager.getUsedMemory() + " Kb");
            logBuilder.show();
        } catch (Exception e) {
            logBuilder.setTitle("WARNING!!");
            logBuilder.setMessage(e.getMessage());
            logBuilder.show();
        }
    }

    //=====================================================================
    public static void showLog(String TAG, BigInteger[] big){
        for (int i=0; i<big.length; i++){
            Log.w(TAG, big[i].toString());
        }
    }
}