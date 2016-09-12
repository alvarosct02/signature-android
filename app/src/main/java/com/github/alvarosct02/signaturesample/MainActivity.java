package com.github.alvarosct02.signaturesample;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private SignatureView sv_field;
    private Button bt_clear;
    private Button bt_save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sv_field = (SignatureView) findViewById(R.id.sv_field);
        bt_clear = (Button) findViewById(R.id.bt_clear);
        bt_save = (Button) findViewById(R.id.bt_save);

        bt_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sv_field.clearView();
            }
        });

        bt_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveImage(sv_field.getSignature());
            }
        });
    }

    private void saveImage(Bitmap bitmap) {

        File myDir = getApplicationContext().getDir("saved_signature", Context.MODE_PRIVATE);

        // make the directory if it does not exist yet
        if (!myDir.exists()) {
            myDir.mkdirs();
        }

        // set the file name of your choice
        String fname = "signature.png";

        // in our case, we delete the previous file, you can remove this
        File file = new File(myDir, fname);
        if (file.exists()) {
            file.delete();
        }

        try {

            // save the signature
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();

            Toast.makeText(MainActivity.this, "Signature saved.", Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_LONG).show();

        }
    }


}
