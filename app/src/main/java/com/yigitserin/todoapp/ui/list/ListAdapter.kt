package com.yigitserin.todoapp.ui.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.yigitserin.todoapp.R
import com.yigitserin.todoapp.data.entity.db.Note
import com.yigitserin.todoapp.data.entity.db.NoteType
import com.yigitserin.todoapp.utils.prettyDate
import java.util.*
import javax.inject.Inject

class ListAdapter @Inject constructor(): RecyclerView.Adapter<ListAdapter.ShoppingItemViewHolder>(){

    class ShoppingItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        val tvDescription: TextView = itemView.findViewById(R.id.tvDescription)
        val tvDate: TextView = itemView.findViewById(R.id.tvDate)
        val tvType: TextView = itemView.findViewById(R.id.tvType)
    }

    private val diffCallback = object : DiffUtil.ItemCallback<Note>() {
        override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    var todoItems: List<Note>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    var listener: OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingItemViewHolder {
        return ShoppingItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false))
    }

    override fun onBindViewHolder(holder: ShoppingItemViewHolder, position: Int) {
        val note = todoItems[position]
        holder.tvTitle.text = todoItems[position].title
        holder.tvDescription.text = todoItems[position].description
        holder.tvDate.text = Date(todoItems[position].date).prettyDate()
        holder.tvType.text = when(todoItems[position].type){
            NoteType.DAILY -> holder.itemView.context.getString(R.string.ui_list_type_daily)
            NoteType.WEEKLY -> holder.itemView.context.getString(R.string.ui_list_type_weekly)
        }

        holder.itemView.setOnClickListener {
            listener?.onClick(note)
        }
    }

    override fun getItemCount(): Int {
        return todoItems.size
    }

    class OnClickListener(val clickListener: (note: Note) -> Unit) {
        fun onClick(note: Note) = clickListener(note)
    }
}