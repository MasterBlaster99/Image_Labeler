package com.example.handdec

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import java.io.IOException

class MainActivity : AppCompatActivity() {
    private lateinit var image : ImageView
    private lateinit var addImage : ImageButton
    private lateinit var textView : TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        image = findViewById(R.id.imageView)
        addImage=findViewById(R.id.addImageBtn)
        textView=findViewById(R.id.textView)

        addImage.setOnClickListener(View.OnClickListener {
            val intent = Intent()
            intent.setType("image/*")
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent,"select image"),123)
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==123){
            image.setImageURI(data!!.data)
            val image: InputImage
            try {
                image = InputImage.fromFilePath(applicationContext,data!!.data)
                val labeler = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS)
                labeler.process(image)
                    .addOnSuccessListener { labels ->
                        for (label in labels) {
                            val text = label.text
                            val confidence = label.confidence
                            val index = label.index
                            textView.append(text+"\n")
                        }
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(applicationContext, "please try again", Toast.LENGTH_LONG).show()
                    }
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
    }
}