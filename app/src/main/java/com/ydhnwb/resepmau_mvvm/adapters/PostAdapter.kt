package com.ydhnwb.resepmau_mvvm.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ydhnwb.resepmau_mvvm.R
import com.ydhnwb.resepmau_mvvm.RecipeActivity
import com.ydhnwb.resepmau_mvvm.models.Recipe
import kotlinx.android.synthetic.main.list_item_post.view.*

class PostAdapter(private var posts : MutableList<Recipe>, private var context: Context) : RecyclerView.Adapter<PostAdapter.ViewHolder>(){

    fun changeList(ps : List<Recipe>){
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
        fun bind(p : Recipe, context: Context){
            itemView.post_title.text = p.title
            itemView.post_content.text = p.content
            itemView.setOnClickListener {
                context.startActivity(Intent(context, RecipeActivity::class.java).apply {
                    putExtra("is_update", true)
                    putExtra("id", p.id)
                })
            }
        }
    }
}