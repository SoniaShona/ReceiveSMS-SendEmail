package com.example.exo2

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.recyclerview.widget.RecyclerView
//import androidx.recyclerview.widget.RecyclerView.ViewHolder
import kotlinx.android.synthetic.main.contact_item.view.*

class ListAdapter (listcont : ArrayList<Contact>, cont : Context  ) : RecyclerView.Adapter<ListAdapter.ExmplVH>()   {


    class ExmplVH(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }
    var contatc_list = listcont
    var context = cont


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExmplVH {
        val layoutInflater = LayoutInflater.from(parent?.context)
        val ite = layoutInflater.inflate(R.layout.contact_item, parent, false)
        return ExmplVH(ite)
    }

    override fun getItemCount(): Int {
        return  contatc_list.size
    }

    override fun onBindViewHolder(holder: ExmplVH, position: Int) {
        holder.itemView.nom.text = contatc_list[position].name
        holder.itemView.numero.text = contatc_list[position].phoneNumber
        holder.itemView.mail.text = contatc_list[position].email

        holder.itemView.setOnClickListener{

            if(position!=0){  contatc_list.removeAt(position)
                notifyItemRemoved(position)
                FileContacts.writeToFile(context , contatc_list)
            }
            else{
                contatc_list.clear()
                notifyDataSetChanged()
                FileContacts.writeToFile(context , contatc_list)
            }

        }

    }
}