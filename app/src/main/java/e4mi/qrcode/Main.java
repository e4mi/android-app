package e4mi.qrcode;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import io.nayuki.qrcodegen.QrCode;
import java.util.Objects;

public class Main extends Activity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getWindow().requestFeature(Window.FEATURE_NO_TITLE);
    setContentView(R.layout.main);
    Intent intent = getIntent();
    String action = intent.getAction();
    String type = intent.getType();
    EditText text = findViewById(R.id.text);
    ImageView image = findViewById(R.id.image);

    if (Intent.ACTION_SEND.equals(action) && type != null) {
      if ("text/plain".equals(type)) {
        String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (sharedText != null) {

          showQR(sharedText);
        }
      }
    } else {
      EditText input = new EditText(this);
      new AlertDialog.Builder(this)
        .setTitle("Content")
        .setView(input)
        .setPositiveButton(
          "OK",
          new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
              String userInput = input.getText().toString();
              showQR(userInput);
            }
          }
        )
        .setNegativeButton(
          "Cancel",
          new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
              dialog.cancel();
              finish();
            }
          }
        )
        .create()
        .show();
    }
  }

  private Bitmap showQR(String text) {
    Bitmap bitmap = toBitmap(qrCode, 10, 4);
  }

  private static Bitmap toBitmap(String text) {
    QrCode qr = QrCode.encodeText(text, QrCode.Ecc.LOW);
    int border = 4;
    int scale = 8;
    int size = qr.size + border * 2;
    Bitmap result = Bitmap.createBitmap(
      size * scale,
      size * scale,
      Bitmap.Config.ARGB_8888
    );
    for (int y = 0; y < size * scale; y++) {
      for (int x = 0; x < size * scale; x++) {
        boolean val = qr.getModule(x / scale - border, y / scale - border);
        result.setPixel(x, y, val ? Color.BLACK : Color.WHITE);
      }
    }
    return result;
  }
}
