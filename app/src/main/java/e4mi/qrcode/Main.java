package e4mi.qrcode;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;

import io.nayuki.qrcodegen.QrCode;

import java.util.Objects;

public class Main extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
                if (sharedText != null) {
                    showQR(sharedText);
                }
            }
        } else {
            
        final EditText input = new EditText(this);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Content")
                .setView(input) // Set the EditText as the dialog view
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String userInput = input.getText().toString();
                        showQR(userInput);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        finish();
                    }
                })
                .create();
            
        }
    }

    private showQR(String text) {
        QrCode qrCode = QrCode.encodeText(text, QrCode.Ecc.LOW);
        Bitmap bitmap = toBitmap(qrCode, 10, 4);
        ImageView imageView = new ImageView(this);
        imageView.setImageBitmap(bitmap);
        setContentView(imageView);
    }

    private static Bitmap toBitmap(QrCode qr, int scale, int border) {
        Objects.requireNonNull(qr);
        if (scale <= 0 || border < 0)
            throw new IllegalArgumentException("Value out of range");
        int size = qr.size + border * 2;
        Bitmap result = Bitmap.createBitmap(size * scale, size * scale, Bitmap.Config.ARGB_8888);
        for (int y = 0; y < size * scale; y++) {
            for (int x = 0; x < size * scale; x++) {
                boolean val = qr.getModule(x / scale - border, y / scale - border);
                result.setPixel(x, y, val ? Color.BLACK : Color.WHITE);
            }
        }
        return result;
    }
}