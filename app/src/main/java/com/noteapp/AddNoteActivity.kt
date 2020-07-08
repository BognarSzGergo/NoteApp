package com.noteapp

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_add_note.*
import java.lang.Exception

class AddNoteActivity : AppCompatActivity() {

    private var id=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_note)

        try {
            val bundle=intent.extras
            if (bundle!=null){
                id=bundle.getInt("ID",0)
                if(id!=0){
                    etTitle.setText(bundle.getString("Title").toString())
                    etDesc.setText(bundle.getString("Description").toString())
                }
            }

        }catch (ex:Exception){}
    }

    fun buAddEvent(view:View){
        val dbManager=DbManager(this)

        val values=ContentValues()
        values.put("Title",etTitle.text.toString())
        values.put("Description",etDesc.text.toString())

        if (id==0 ){
            val ID=dbManager.insert(values)
            if (ID>0){
                Toast.makeText(this, "note is added",Toast.LENGTH_LONG).show()
                finish()
            }else{
                Toast.makeText(this, "cannot add note",Toast.LENGTH_LONG).show()
            }
        }else{
            val selectionArgs= arrayOf(id.toString())
            val id=dbManager.update(values,"ID=?",selectionArgs)
            if (id>0){
                Toast.makeText(this, "note is updated",Toast.LENGTH_LONG).show()
                finish()
            }else{
                Toast.makeText(this, "cannot update note",Toast.LENGTH_LONG).show()
            }
        }


    }
}
