package com.example.no1.photongallery

import android.app.WallpaperManager
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.PointF
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ImageView
import com.davemorrissey.labs.subscaleview.ImageSource
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import java.io.File
import java.io.FileOutputStream

class PictureActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_picture)

        lateinit var Mohammad_bitmap :Bitmap

        val wallpaper = findViewById<ImageView>(R.id.btnWallpaper)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            wallpaper.elevation = 15f
        }
        wallpaper.bringToFront()
        val imageView = findViewById<SubsamplingScaleImageView>(R.id.imgPicture)

        val download_image = findViewById(R.id.btnDownload) as ImageView
        download_image.setOnClickListener {
            //  Download image
            SAVE_TO_GALLERY(Mohammad_bitmap)
            Toast.makeText(this, "عکس مورد نظر در گالری ذخیره شد", Toast.LENGTH_SHORT).show()

        }

        val btnWallpaper2_ = findViewById(R.id.btnWallpaper2) as ImageView
        btnWallpaper2_.setOnClickListener {

            SET_ZOOMED_IMAGE_TO_BACKGROUND(Mohammad_bitmap)
            Toast.makeText(this, "تصویر زمینه گوشی شما عوض شد", Toast.LENGTH_SHORT).show()
        }

        val close = findViewById(R.id.btnClose) as ImageView
        close.setOnClickListener {

            finish()
        }


         val description_btn = findViewById(R.id.btnDescription) as ImageView
        description_btn.setOnClickListener {

         }

        val t = object : Target {
            override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom) {
                if (bitmap != null) {
                     Mohammad_bitmap = bitmap

                    imageView.setImage(ImageSource.bitmap(bitmap))
                    val zoom = Math.min((bitmap.width - 1) / imageView.width + 1,
                            (bitmap.height - 1) / imageView.height + 1)
                    imageView.setScaleAndCenter(zoom.toFloat(),
                            PointF((bitmap.width/2).toFloat(), (bitmap.height/2).toFloat()))
                }
            }

            override fun onBitmapFailed(errorDrawable: Drawable?) {
//                loadDefaultMarker(listener)
            }

            override fun onPrepareLoad(placeHolderDrawable: Drawable?) {}
        }
        imageView.tag = t
        Picasso.with(this).load(intent.getStringExtra("address")).into(t)
    }

//     fun SET_ZOOMED_IMAGE_TO_BACKGROUND(imageView:SubsamplingScaleImageView){
//     fun SET_ZOOMED_IMAGE_TO_BACKGROUND(imageView:ImageView){
    fun SET_ZOOMED_IMAGE_TO_BACKGROUND(bitmap: Bitmap){

//         val bmp =  imageView.getDrawingCache()
         val   myWallpaperManager: WallpaperManager
         myWallpaperManager = WallpaperManager.getInstance(getApplicationContext())
//         myWallpaperManager.setBitmap(bmp);
         myWallpaperManager.setBitmap(bitmap);
     }


//    fun SAVE_TO_GALLERY(imageView:SubsamplingScaleImageView){
//     fun SAVE_TO_GALLERY(imageView:ImageView){
     fun SAVE_TO_GALLERY(bitmap: Bitmap){

//        val draw = imageView.drawingCache as BitmapDrawable
//        val draw = imageView.getDrawingCache(true)  as BitmapDrawable
//        val draw = imageView.getDrawable() as BitmapDrawable
//        val bitmap = draw.bitmap
        var outStream: FileOutputStream? = null
        val sdCard = Environment.getExternalStorageDirectory()
        val dir = File(sdCard.getAbsolutePath() + "/DCIM")
        dir.mkdirs()
        val fileName = String.format("%d.jpg", System.currentTimeMillis())
        val outFile = File(dir, fileName)
        outStream = FileOutputStream(outFile)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream)
        outStream!!.flush()
        outStream!!.close()


//    Additionally, in order to refresh the gallery and to view the image there:
    val intent : Intent
    intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
    intent.setData(Uri.fromFile(outFile));
    sendBroadcast(intent);

    }

}




