package ileos.crsaencryption.gui;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;

import ileos.crsaencryption.R;
import ileos.crsaencryption.algorithm.CRSAManager;
import ileos.crsaencryption.utils.FileChooser;
import ileos.crsaencryption.utils.UtitilityManager;

public class ComDeCom extends AppCompatActivity {
    UtitilityManager util = new UtitilityManager();
    CRSAManager cRSAManager = new CRSAManager();
    private AlertDialog.Builder logBuilder;
    private Button compressBtn, decompressBtn, openFile;
    TextView textView;
    private Runtime runtime = Runtime.getRuntime();
    BigInteger[] tempBigArr;
    BigInteger[] comPlain;
    BigInteger[] decompPlain;


    String[] plainteks = {
            "undocument",
            "undocumented functio",
            "undocumented functions for tes",
            "undocumented functions for testing Windo",
            "undocumented functions for testing Windows compone",
            "undocumented functions for testing Windows components such a",
            "undocumented functions for testing Windows components such as USER.EXE",
            "undocumented functions for testing Windows components such as USER.EXE are named",
            "undocumented functions for testing Windows components such as USER.EXE are named things li",
            "undocumented functions for testing Windows components such as USER.EXE are named things like Bear351"
    };

    public ComDeCom() throws UnsupportedEncodingException {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_com_de_com);

        textView = (TextView) findViewById(R.id.textView);
        logBuilder = new AlertDialog.Builder(
                ComDeCom.this);
        logBuilder.setNeutralButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        dialog.dismiss();
                    }
                });


        openFile = (Button) findViewById(R.id.openFileC);
        openFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFile();
            }
        });
        compressBtn = (Button) findViewById(R.id.compressButton);
        compressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < plainteks.length; i++) {
                    tempBigArr = encodeASCII(plainteks[i]);
                    double startCom = System.nanoTime();
                    comPlain = cRSAManager.compressionProcedure(tempBigArr);
                    double usedTime = (System.nanoTime() - startCom) / 1000000;
                    //util.showLog("compressed " + i, String.valueOf(usedTime));

                    double startCom1 = System.nanoTime();
                    decompPlain = cRSAManager.decompressionProcedure(comPlain[0], comPlain[1]);
                    double usedTime1 = (System.nanoTime() - startCom1)/1000000;
                    //util.showLog("compressed :", comPlain);
                    Log.w("DeCompressed "+ i, String.valueOf(usedTime1));
                }
            }
        });

        decompressBtn = (Button) findViewById(R.id.decompressButton);
        decompressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < plainteks.length; i++) {
                    double startCom = System.nanoTime();
                    decompPlain = cRSAManager.decompressionProcedure(comPlain[0], comPlain[1]);
                    double usedTime = (System.nanoTime() - startCom)/1000000;
                    //util.showLog("compressed :", comPlain);
                    Log.w("DeCompressed "+ i, String.valueOf(usedTime));
                }
            }
        });
    }

    private void openFile(){
        new FileChooser(ComDeCom.this).setFileListener(new FileChooser.FileSelectedListener() {
            @Override
            public void fileSelected(File file) {
                try {
                    textView.setText("Path : " + file.getCanonicalPath());
                   // plainteks = util.readFile(file.getAbsolutePath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).showDialog();
    }

    public static BigInteger[] encodeASCII(String str) {
        int n = 0;
        char[] arrayStr = str.toCharArray();
        BigInteger[] encodeStr = new BigInteger[arrayStr.length];
        for (int i = 0; i < arrayStr.length; i++) {
            n = arrayStr[i];

            encodeStr[i] = new BigInteger(String.valueOf(n));
        }
        return encodeStr;
    }
}


/*




for (int i = 0; i < plainteks.length; i++) {
                    double startCom = System.nanoTime();
                    decompPlain = cRSAManager.decompressionProcedure(comPlain[0], comPlain[1]);
                    double usedTime = (System.nanoTime() - startCom)/1000000;
                    //util.showLog("compressed :", comPlain);

                    Log.w("DeCompressed "+ i, String.valueOf(usedTime));



                }
 */