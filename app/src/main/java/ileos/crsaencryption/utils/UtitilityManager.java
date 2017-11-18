package ileos.crsaencryption.utils;


import android.util.Log;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.math.BigInteger;

import ileos.crsaencryption.algorithm.CRSAManager;
import ileos.crsaencryption.algorithm.RSAManager;
import ileos.crsaencryption.entity.FileEntity;
import ileos.crsaencryption.entity.KeyEntity;

public class UtitilityManager {
    private FileEntity fileEntity = new FileEntity();
    private KeyEntity keyEntity = new KeyEntity();

    //baca file dan simpan string ke entity file
    public void openFile(String pathFile) throws IOException {
        String data = readFile(pathFile);
        fileEntity.setDataString(data);
        fileEntity.setPathFile(pathFile);
    }

    //baca key file dan simpan ke entity key
    public void openKey(String pathKey) throws IOException {
        String key = readFile(pathKey);

        String[] enKey = key.split("e#");
        String[] deKey = key.split("d#");
        String[] moKey  = key.split("n#");
        BigInteger e = new BigInteger(enKey[1].trim());
        BigInteger d = new BigInteger(deKey[1].trim());
        BigInteger n = new BigInteger(moKey[1].trim());

        keyEntity.setE(e);
        keyEntity.setD(d);
        keyEntity.setN(n);
    }

    //proses enkripsi keseluruhan
    public void encryptPerformed(String selectedAlgorithm) throws UnsupportedEncodingException {
        BigInteger[] ciphertext = new BigInteger[0];
        BigInteger[] plaintext = encodeASCII(fileEntity.getDataString());

        if (selectedAlgorithm.equals("RSA")){
            RSAManager rsaManager = new RSAManager();
            rsaManager.setE(keyEntity.getE());
            rsaManager.setN(keyEntity.getN());

            long freeMe = runtime.freeMemory()/1024;
            double startCom = System.nanoTime();

            ciphertext = rsaManager.encrypt(plaintext);

            usedTime = System.nanoTime() - startCom;
            usedMemory = freeMe - runtime.freeMemory()/1024;
        }

        else if (selectedAlgorithm.equals("Compression RSA")) {
            CRSAManager crsaManager = new CRSAManager();
            crsaManager.setE(keyEntity.getE());
            crsaManager.setN(keyEntity.getN());

            long freeMe = runtime.freeMemory()/1024;
            double startCom = System.nanoTime();

            BigInteger[] compressedPlaintext = crsaManager.compressionProcedure(plaintext);
            ciphertext = crsaManager.encrypt(compressedPlaintext);

            usedTime = System.nanoTime() - startCom;
            usedMemory = freeMe - runtime.freeMemory()/1024;

            setPlainSize(plaintext);
            setCompressedPlainSize(compressedPlaintext);
        }

        String str = toString(ciphertext);
        saveFile(str, "enc");
    }

    //proses dekripsi keseluruhan
    public void decryptPerformed(String selectedAlgorithm) throws UnsupportedEncodingException {
        BigInteger[] plaintext = new BigInteger[0];
        BigInteger[] ciphertext = getBlock(fileEntity.getDataString());

        if(selectedAlgorithm.equals("RSA")){
            RSAManager rsaManager = new RSAManager();
            rsaManager.setD(keyEntity.getD());
            rsaManager.setN(keyEntity.getN());

            double startCom = System.nanoTime();
            long freeMe = runtime.freeMemory()/1024;

            plaintext = rsaManager.decrypt(ciphertext);     //dec

            usedTime = System.nanoTime() - startCom;
            usedMemory = freeMe - runtime.freeMemory()/1024;
        }
        else if (selectedAlgorithm.equals("Compression RSA")) {
            CRSAManager crsaManager = new CRSAManager();
            crsaManager.setD(keyEntity.getD());
            crsaManager.setN(keyEntity.getN());

            double startCom = System.nanoTime();
            long freeMe = runtime.freeMemory()/1024;

            BigInteger[] cp = crsaManager.decrypt(ciphertext);
            plaintext = crsaManager.decompressionProcedure(cp[0],cp[1]);
            usedTime = System.nanoTime() - startCom;
            usedMemory = freeMe - runtime.freeMemory()/1024;
        }

        String str = decodeASCII(plaintext);
        saveFile(str,"dec");
    }

    //baca file dari directory
    public String readFile(String pathFile) throws IOException {
        File fileDir = new java.io.File(pathFile);
        BufferedReader in = new BufferedReader(new InputStreamReader(
                new FileInputStream(fileDir), "ASCII"));
        String str;
        //String ls = System.getProperty("line.separator");
        StringBuilder stringBuilder = new StringBuilder();
        while ((str = in.readLine()) != null) {
            stringBuilder.append(str);
            //stringBuilder.append(ls);
        }
        in.close();

        return stringBuilder.toString();
    }

    //simpan file ke directory
    private void saveFile(String str, String mode) {
        String mPathFile = fileEntity.getPathFile();
        String pathFile = mPathFile.substring(0,mPathFile.lastIndexOf(File.separator));
        String fileName = mPathFile.substring(mPathFile.lastIndexOf(File.separator) + 1);

        if(mode ==  "enc"){
            pathFile = pathFile + File.separator + "Enc-" + fileName;
        }
        else if(mode == "dec"){
            pathFile = pathFile + File.separator + "Dec-" + fileName;
        }
        try {
            java.io.File file = new java.io.File(pathFile);
            if (!file.exists()) {   // if file doesnt exists, then create it
                file.createNewFile();}
            FileOutputStream fosStream = new FileOutputStream(
                    file.getAbsoluteFile());
            Writer bufferedWriter = new OutputStreamWriter(fosStream, "ASCII");
            bufferedWriter.write(str);
            bufferedWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //konversi plainteks ke ASCII
    private BigInteger[] encodeASCII(String str)
            throws UnsupportedEncodingException {
        int n = 0;
        char[] arrayStr = str.toCharArray();
        BigInteger[] encodeStr = new BigInteger[arrayStr.length];
        for (int i = 0; i < arrayStr.length; i++) {
            n = arrayStr[i];

            encodeStr[i] = new BigInteger(String.valueOf(n));
        }
        return encodeStr;
    }

    //konversi ASCII plainteks ke plainteks asli
    private String decodeASCII(BigInteger[] bigIn)
            throws UnsupportedEncodingException {
        int n[] = new int[bigIn.length];
        char[] m = new char[bigIn.length];

        for (int i = 0; i < bigIn.length; i++) {
            n[i] = Integer.valueOf(bigIn[i].toString());
            m[i] = (char) n[i];
        }
        return String.valueOf(m);
    }
    //ambil blok cipherteks dari String cipherteks
    private BigInteger[] getBlock(String str) {
        String[] temp = str.split(" ");

        BigInteger[] blok = new BigInteger[temp.length];


        for (int i = 0; i < temp.length; i++) {
            blok[i] = new BigInteger(temp[i]);

        }
        return blok;
    }

    //ubah biginteger cipherteks ke string ( untuk penyimpanan file)
    private String toString(BigInteger[] big) {
        String str = "";

        StringBuilder stringBuffer = new StringBuilder();
        for (int i = 0; i < big.length; i++) {
            stringBuffer.append(str.valueOf(big[i]));
            stringBuffer.append(" ");
            str = stringBuffer.toString();

        }
        return str;
    }

    private Runtime runtime = Runtime.getRuntime();
    private double usedTime;
    private long usedMemory;
    public double getUsedTime() {
        return usedTime/1000000;
    }
    public long getUsedMemory(){
        return  usedMemory;
    }


    private int plainSize;
    private int compressedPlainSize;

    public int getPlainSize() {
        return plainSize;
    }

    public void setPlainSize(BigInteger[] plaintext) {
        int byteSize = 0;
        byte[] tempByte;
        for (int i =0; i <plaintext.length; i++){
            tempByte = plaintext[i].toByteArray();
            byteSize = byteSize + tempByte.length;
        }
        this.plainSize = byteSize;
    }

    public int getCompressedPlainSize() {
        return compressedPlainSize;
    }

    public void setCompressedPlainSize(BigInteger[] compressedPlainSize) {
        int byteSize = 0;
        byte[] tempByte;
        for (int i =0; i <compressedPlainSize.length; i++){
            tempByte = compressedPlainSize[i].toByteArray();
            byteSize = byteSize + tempByte.length;
        }
        this.compressedPlainSize = byteSize;
    }



    //=======================
    public static void showLog(String TAG, String string){
        Log.w(TAG, string);
    }
    public static void showLog(String TAG, BigInteger[] bigIn){
        for(int i = 0; i < bigIn.length; i++){
            Log.w(TAG, bigIn[i].toString());
        }
    }
}
