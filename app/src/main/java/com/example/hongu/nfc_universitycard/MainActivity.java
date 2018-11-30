package com.example.hongu.nfc_universitycard;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.NfcF;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private IntentFilter[] intentFiltersArray;
    private String[][] techListsArray;
    private NfcAdapter mAdapter;
    private PendingIntent pendingIntent;
    private NfcReader nfcReader = new NfcReader();
    private TextView text;
    private final static String TAG = NfcReader.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        text = (TextView) findViewById(R.id.text);
        pendingIntent = PendingIntent.getActivity(
                this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

        IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);

        try {
            ndef.addDataType("text/plain");
        }
        catch (IntentFilter.MalformedMimeTypeException e) {
            throw new RuntimeException("fail", e);
        }
        intentFiltersArray = new IntentFilter[] {ndef};

        // FelicaはNFC-TypeFなのでNfcFのみ指定でOK
        techListsArray = new String[][] {
                new String[] { NfcF.class.getName() }
        };

        // NfcAdapterを取得
        mAdapter = NfcAdapter.getDefaultAdapter(getApplicationContext());
    }

    @Override
    protected void onResume() {
        super.onResume();
        // NFCの読み込みを有効化
        mAdapter.enableForegroundDispatch(this, pendingIntent, intentFiltersArray, techListsArray);
    }

    @Override
    protected void onNewIntent(Intent intent) {
// IntentにTagの基本データが入ってくるので取得。
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        if (tag == null) {
            return;
        }
        NfcF nfc = NfcF.get(tag);
//        try {
//            nfc.connect();
//            // System 1のシステムコード -> 0xFE00
//            byte[] targetSystemCode = new byte[]{(byte) 0xfe,(byte) 0x00};
//
//            // polling コマンドを作成
//            byte[] polling = polling(targetSystemCode);
//
//            // コマンドを送信して結果を取得
//            byte[] pollingRes = nfc.transceive(polling);
//
//            // System 0 のIDｍを取得(1バイト目はデータサイズ、2バイト目はレスポンスコード、IDmのサイズは8バイト)
//            byte[] targetIDm = Arrays.copyOfRange(pollingRes, 2, 10);
//
//            // サービスに含まれているデータのサイズ(今回は4だった)
//            int size = 4;
//
//            // 対象のサービスコード -> 0x1A8B
//            byte[] targetServiceCode = new byte[]{(byte) 0x1A, (byte) 0x8B};
//
//            // Read Without Encryption コマンドを作成
//            byte[] req = readWithoutEncryption(targetIDm, size, targetServiceCode);
//
//            // コマンドを送信して結果を取得
//            byte[] res = nfc.transceive(req);
//
//            nfc.close();

            // 結果をパースしてデータだけ取得
//            return parse(res);
//        } catch (Exception e) {
//            Log.e(TAG, e.getMessage() , e);
//        }

        // ここで取得したTagを使ってデータの読み書きを行う。

        byte[] test = tag.getId();
        nfcReader.readTag(tag);
//        for(int i=0; i < test.length; i++) {
//            Log.i(TAG, String.valueOf(test[i]));
//        }

        text.setText(tag.getTechList()[0]);

    }

    @Override
    protected void onPause() {
        super.onPause();
        mAdapter.disableForegroundDispatch(this);
    }

}
