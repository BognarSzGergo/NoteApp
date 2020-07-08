package com.noteapp

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.BaseAdapter
import android.widget.SearchView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.ticket.view.*

class MainActivity : AppCompatActivity() {

    var listNotes=ArrayList<Note>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadQuery("%")
    }

    override fun onResume() {
        super.onResume()
        loadQuery("%")
    }

    fun loadQuery(title:String){
        val dbManager=DbManager(this)
        val projections=(arrayOf ("ID","Title","Description"))
        val selectionArgs= arrayOf(title)
        val cursor=dbManager.query(projections,"Title like ?",selectionArgs,"Title")
        listNotes.clear()
        if(cursor.moveToFirst()){
            do {
                val noteId=cursor.getInt(cursor.getColumnIndex("ID"))
                val noteTitle=cursor.getString(cursor.getColumnIndex("Title"))
                val noteDesc=cursor.getString(cursor.getColumnIndex("Description"))

                listNotes.add(Note(noteId,noteTitle,noteDesc))
            }while (cursor.moveToNext())
        }

        val myNotesAdapter=MyNotesAdapter(this,listNotes)
        lvNotes.adapter=myNotesAdapter
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)

        val sv:SearchView = menu.findItem(R.id.app_bar_search).actionView as SearchView

        val sm= getSystemService(Context.SEARCH_SERVICE) as SearchManager
        sv.setSearchableInfo(sm.getSearchableInfo(componentName))
        sv.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                loadQuery("%$query%")
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                if (newText.isNotEmpty()) {
                    loadQuery("%$newText%")
                } else {
                    loadQuery("%")
                }
                return false
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.addNote->{
                val intent=Intent(this,AddNoteActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    inner class MyNotesAdapter(val context: Context, val listNotes: ArrayList<Note>) : BaseAdapter() {

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val myView=layoutInflater.inflate(R.layout.ticket,null)
            val myNote=listNotes[position]

            myView.tvTitle.text=myNote.noteName
            myView.tvDes.text=myNote.noteDes
            myView.ivDelete.setOnClickListener {
                val dbManager=DbManager(context)
                val selectionArgs= arrayOf(myNote.noteID.toString())
                dbManager.delete("ID=?",selectionArgs)
                loadQuery("%")
            }

            myView.ivEdit.setOnClickListener {
                val intent=Intent(context,AddNoteActivity::class.java)
                intent.putExtra("ID",myNote.noteID)
                intent.putExtra("Title",myNote.noteName)
                intent.putExtra("Description",myNote.noteDes)
                startActivity(intent)
            }

            return myView
        }

        override fun getItem(position: Int): Any {
            return listNotes[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return  listNotes.count()
        }

    }
}
