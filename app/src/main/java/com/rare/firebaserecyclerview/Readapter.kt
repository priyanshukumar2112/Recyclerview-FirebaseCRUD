package com.rare.firebaserecyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class Readapter (var detail : ArrayList<SDetail>, var recyclerInterface :RecyclerInterface) : RecyclerView.Adapter<Readapter.ViewHolder>(){

   class ViewHolder(var view: View) : RecyclerView.ViewHolder(view) {
        var etName = view.findViewById<TextView>(R.id.tvName)
        var etRollNo = view.findViewById<TextView>(R.id.tvRollNo)
       var btnedit = view.findViewById<ImageButton>(R.id.btnUpdate)
       var btnDelete = view.findViewById<ImageButton>(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        var view = LayoutInflater.from(parent.context).inflate(R.layout.list_show, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.etName.setText(detail[position].name)
        holder.etRollNo.setText(detail[position].rollNo.toString())

        holder.btnDelete.setOnClickListener{
            recyclerInterface.delete(position)
        }
        holder.btnedit.setOnClickListener {
            recyclerInterface.edit(position)
        }
    }

    override fun getItemCount(): Int {
        return  detail.size
    }
}