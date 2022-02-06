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
import javax.inject.Inject

class ListAdapter @Inject constructor(): RecyclerView.Adapter<ListAdapter.ShoppingItemViewHolder>(){

    class ShoppingItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val tvTodoTitle: TextView = itemView.findViewById(R.id.tvTodoTitle)
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
        holder.tvTodoTitle.text = todoItems[position].title
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