package com.example.hannes.choppic;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;

public class MainActivity extends AppCompatActivity {

    public Button b;
    public ImageView imageView;
    public static EditText editText;
    static final int PICK_IMAGE = 100;
    Uri imageUri;
    Bitmap bitmap;
    MyCanvas myCanvas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        b = (Button) findViewById(R.id.button);
        imageView = (ImageView) findViewById(R.id.imageView);
        editText = (EditText) findViewById(R.id.editText);

        b.setOnClickListener(e -> {
            openGallery();
        });

        imageView.setOnClickListener(e ->{
            try {
                bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                int pixels[][] = getImageData(bitmap);
                openMyCanvas(pixels);
            }catch (ClassCastException exc){
                Log.e("log", "no image selected");
                exc.printStackTrace();
            }
        });
    }

    private void openGallery(){
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE){
            RequestManager requestManager = Glide.with(this);
            imageUri = data.getData();
            RequestBuilder requestBuilder = requestManager.load(imageUri);
            requestBuilder.into(imageView);
        }
    }

    private void openMyCanvas(int[][] pixels){
        myCanvas = new MyCanvas(this, pixels);
        myCanvas.setBackgroundColor(Color.BLACK);
        setContentView(myCanvas);
    }

    private int[][] getImageData(Bitmap img) {
        Log.d("test", ""+img.getPixel(1, 1));
        int w = img.getWidth();
        int h = img.getHeight();
        int[] flatArray = new int[w * h];
        img.getPixels(flatArray, 0, w, 0, 0, w, h);

        int data[][] = new int[h][w];
        Log.d("var", "w = "+w+" h = "+h);
        for(int i=0; i<w;i++)
            for(int j=0;j<h;j++)
                data[j][i] = flatArray[(j*w) + i];

        long r = 0;
        long g = 0;
        long b = 0;
        for (int i : flatArray) {
            r+=Color.red(i);
            g+=Color.green(i);
            b+=Color.blue(i);
        }
        Log.d("var", "avg Color: r(" + r/flatArray.length +") g(" + g/flatArray.length +")b(" + b/flatArray.length +")");
        return data;
    }
}
