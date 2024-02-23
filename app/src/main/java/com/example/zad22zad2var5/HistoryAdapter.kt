package com.example.zad22zad2var5

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class HistoryAdapter : ListAdapter<History, HistoryAdapter.HistoryViewHolder>(DiffCallback()) {

    private var onDeleteClickListener: ((History) -> Unit)? = null
    private var onEditClickListener: ((History) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_history, parent, false)
        return HistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val fact = getItem(position)
        holder.bind(fact, onDeleteClickListener, onEditClickListener)
    }

    fun setOnDeleteClickListener(listener: (History) -> Unit) {
        onDeleteClickListener = listener
    }

    fun setOnEditClickListener(listener: (History) -> Unit) {
        onEditClickListener = listener
    }

    class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val factTextView: TextView = itemView.findViewById(R.id.factText)
        private val deleteButton: Button = itemView.findViewById(R.id.deleteButton)
        private val editButton: Button = itemView.findViewById(R.id.editButton)

        fun bind(history: History, onDeleteClickListener: ((History) -> Unit)?, onEditClickListener: ((History) -> Unit)?) {
            factTextView.text = "${history.text}"

            deleteButton.setOnClickListener {
                onDeleteClickListener?.invoke(history)
            }
            editButton.setOnClickListener {
                onEditClickListener?.invoke(history)
            }
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<History>() {
        override fun areItemsTheSame(oldItem: History, newItem: History): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: History, newItem: History): Boolean {
            return oldItem == newItem
        }
    }
}