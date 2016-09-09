package example.com.lab5;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;


public class MainActivity extends Activity {
    EditText input;
    TextView output;
    Button bEncrypt,bDecrypt;
    byte[] encryptedData;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        input= (EditText) findViewById(R.id.editText);
        output= (TextView) findViewById(R.id.textView);
        bEncrypt= (Button) findViewById(R.id.buttonE);
        bDecrypt= (Button) findViewById(R.id.buttonD);
        KeyGenerator keyGen= null;
        final Key key;
        encryptedData=null;
        try {
            keyGen = KeyGenerator.getInstance("AES");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        key=keyGen.generateKey();

        keyGen.init(128);
        bEncrypt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String data = input.getText().toString();
                if (data.equals("")) {
                    Toast.makeText(MainActivity.this, "Enter some data", Toast.LENGTH_SHORT).show();
                } else {
                    encryptedData = encryptData(data, key);
                    output.setText("Encrypted:  "+encryptedData);
                }
            }
        });
        bDecrypt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(encryptedData==null) {
                    Toast.makeText(MainActivity.this, "Encrypt the data first", Toast.LENGTH_SHORT).show();
                    output.setText("");
                    input.setText("");
                }
                else {
                    byte[] decryptedData=decryptData(encryptedData,key);
                    String x="";
                    try {
                        x=new String(decryptedData,"UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    output.setText("Decrypted:   "+x);
                    encryptedData=null;
                    input.setText("");
                }

            }
        });
    }

    public byte[] encryptData(String data,Key key) {
        byte[] plainBytes=data.getBytes();
        try {
            Cipher cipher=Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE,key);
            byte[] cipherBytes=cipher.doFinal(plainBytes);
            return cipherBytes;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public byte[] decryptData(byte[] data,Key key) {
        Cipher cipher= null;
        try {
            cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE,key);
            byte[] plainBytes=cipher.doFinal(data);
            return plainBytes;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
