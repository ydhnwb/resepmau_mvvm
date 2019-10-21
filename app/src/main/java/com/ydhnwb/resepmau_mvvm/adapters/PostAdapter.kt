package com.ydhnwb.resepmau_mvvm.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.ydhnwb.resepmau_mvvm.R
import com.ydhnwb.resepmau_mvvm.models.Post
import kotlinx.android.synthetic.main.list_item_post.view.*

class PostAdapter(private var posts : MutableList<Post>, private var context: Context) : RecyclerView.Adapter<PostAdapter.ViewHolder>(){

    fun changeList(ps : List<Post>){
        posts.clear()
        posts.addAll(ps)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item_post, parent, false))
    }

    override fun getItemCount() = posts.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(posts[position], context)

    class ViewHolder (itemView : View) : RecyclerView.ViewHolder(itemView){
        fun bind(p : Post, context: Context){
            itemView.post_title.text = p.title
            itemView.post_content.text = p.content
            itemView.setOnClickListener {
                Toast.makeText(context, p.id.toString(), Toast.LENGTH_LONG).show()
            }
        }
    }
}