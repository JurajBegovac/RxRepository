package com.releaseit.rxrepository.todos

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.releaseit.rxrepository.R
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_todo.*
import kotlinx.android.synthetic.main.item_todo.view.*
import org.jetbrains.anko.intentFor
import javax.inject.Inject

/**
 * Created by jurajbegovac on 09/02/2018.
 */
class TodoActivity : DaggerAppCompatActivity() {

  @Inject
  lateinit var todoViewModelFactory: TodoViewModelFactory

  private lateinit var todosViewModel: TodosViewModel

  companion object {
    fun startIntent(context: Context) = context.intentFor<TodoActivity>()
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_todo)

    activityTodoRecyclerView.apply {
      layoutManager = LinearLayoutManager(context)
    }

    todosViewModel =
      ViewModelProviders.of(this, todoViewModelFactory)
        .get(TodosViewModel::class.java)

    todosViewModel.items.observe(this, Observer<List<TodoViewItem>> { showItems(it) })
    todosViewModel.showLoading.observe(this, Observer<Boolean> { showLoading(it) })
    todosViewModel.showError.observe(this, Observer<Boolean> { showError(it) })

    activityTodoFab.setOnClickListener { todosViewModel.addTodo() }
  }

  private fun showItems(todoItems: List<TodoViewItem>?) {
    Log.d("Juraj", "showItems: $todoItems")
    if (todoItems == null) return

    activityTodoRecyclerView.adapter =
      TodoAdapter({ todoItems[it] }, { todoItems.size }, { todosViewModel.itemPressed(it) })
  }

  private fun showLoading(show: Boolean?) {
    Log.d("Juraj", "showLoading: $show")
  }

  private fun showError(show: Boolean?) {
    Log.d("Juraj", "showError: $show")
  }

}

internal class TodoAdapter(private val itemProvider: (Int) -> TodoViewItem, private val itemCount: () -> Int,
                           private val clickListener: (Int) -> (Unit)) : RecyclerView.Adapter<TodoViewHolder>() {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = TodoViewHolder(parent.inflate(R.layout.item_todo))

  override fun getItemCount() = itemCount()

  override fun onBindViewHolder(holder: TodoViewHolder?, position: Int) {
    holder?.bind(itemProvider(position), { clickListener(position) })
  }
}

internal class TodoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
  fun bind(item: TodoViewItem, clickListener: () -> Unit) {
    itemView.itemTodoRoot.setOnClickListener { clickListener() }
    itemView.itemTodoTitle.text = item.title
    itemView.itemTodoMessage.text = item.message
  }
}

fun ViewGroup.inflate(@LayoutRes resource: Int): View = LayoutInflater.from(context).inflate(resource, this, false)
