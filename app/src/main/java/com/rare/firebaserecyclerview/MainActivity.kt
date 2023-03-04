package com.rare.firebaserecyclerview

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.rare.firebaserecyclerview.databinding.ActivityMainBinding
import com.rare.firebaserecyclerview.databinding.ItemListBinding

class MainActivity : AppCompatActivity(), RecyclerInterface {
    val database = Firebase.database
    lateinit var layoutManager: LinearLayoutManager
    lateinit var binding: ActivityMainBinding
    lateinit var adapter: Readapter
    var detail = ArrayList<SDetail>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        layoutManager = LinearLayoutManager(this)

        adapter = Readapter(detail, this)
        binding.recylerView.layoutManager = layoutManager
        binding.recylerView.adapter = adapter
        binding.btnFab.setOnClickListener {
            var dbinding = ItemListBinding.inflate(layoutInflater)
            var dialog = Dialog(this)
            dialog.setContentView(dbinding.root)
            dialog.window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
            )
            dbinding.btnDelete.visibility = View.INVISIBLE
            dbinding.btnUpdate.visibility = View.INVISIBLE
            dbinding.btnSave.setOnClickListener {

                if (dbinding.etName.text.isEmpty()) {
                    dbinding.etName.error = "Enter Name"
                } else if (dbinding.etRollNo.text.isEmpty()) {
                    dbinding.etRollNo.error = "Enter Roll no"
                } else {
                    var name = dbinding.etName.text.toString()
                    var roll = dbinding.etRollNo.text.toString().toInt()
                   // detail.add(SDetail(name, roll))
                    database.reference.push().setValue(SDetail(name, roll))
                    adapter.notifyDataSetChanged()
                    dialog.dismiss()
                }
            }
            dialog.show()
        }

        database.reference.addChildEventListener(object: ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                var userList = snapshot.getValue(SDetail::class.java)
                userList?.reference = snapshot.key
                if (userList != null) {
                    detail.add(userList)
                }
                adapter.notifyDataSetChanged()
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                var userlist = snapshot.getValue(SDetail::class.java)
                userlist?.reference=snapshot.key

                if(userlist != null){
                    var index = -1
                    index = detail.indexOfFirst { element-> element.reference.equals(userlist.reference) }

                    if(index>-1)
                    detail.set( index, userlist)
                }
                adapter.notifyDataSetChanged()
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                var userlist = snapshot.getValue(SDetail::class.java)
                userlist?.reference=snapshot.key

                if(userlist != null){
                    var index = -1
                    index = detail.indexOfFirst { element-> element.reference.equals(userlist.reference)  }
                    if(index>-1)
                        detail.removeAt(index)
                }
                adapter.notifyDataSetChanged()
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    override fun edit(position: Int) {

        var dbinding = ItemListBinding.inflate(layoutInflater)
        var dialog = Dialog(this)
        dialog.setContentView(dbinding.root)
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
        )
        dbinding.btnDelete.visibility = View.INVISIBLE
        dbinding.btnUpdate.visibility = View.INVISIBLE

        dbinding.etName.setText(detail[position].name.toString())
        dbinding.etRollNo.setText(detail[position].rollNo.toString())
        dbinding.btnSave.setOnClickListener {

            if (dbinding.etName.text.isEmpty()) {
                dbinding.etName.error = "Enter Name"
            } else if (dbinding.etRollNo.text.isEmpty()) {
                dbinding.etRollNo.error = "Enter Roll no"
            } else {
                var name = dbinding.etName.text.toString()
                var roll = dbinding.etRollNo.text.toString().toInt()
               // detail.set(position, SDetail(name, roll))
                database.reference.child((detail[position].reference?:"")).setValue(SDetail(name, roll))
                adapter.notifyDataSetChanged()
                dialog.dismiss()
            }
        }
        dialog.show()
    }

    override fun delete(position: Int) {
        var dialog = AlertDialog.Builder(this)
        dialog.setTitle("Delete Alert!!")
        dialog.setMessage("Are you sure ")
        dialog.setPositiveButton("yes") { _, _ ->
          //  detail.removeAt(position)
            database.reference.child((detail[position].reference?:"")).removeValue()
            adapter.notifyDataSetChanged()
            Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show()

        }

        dialog.setNegativeButton("no") { _, _ ->
            Toast.makeText(this, "operation cancel", Toast.LENGTH_SHORT).show()

        }
        dialog.show()

        System.out.println("click")


    }
}
