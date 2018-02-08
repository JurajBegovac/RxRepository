package com.releaseit.rxrepository.app

import android.content.SharedPreferences
import android.os.Bundle
import com.releaseit.rxrepository.repository.local.todos.Todo
import com.releaseit.rxrepository.repository.local.todos.TodoRepository
import com.releaseit.rxrepository.utils.LocalDb
import com.releaseit.rxrepository.utils.saveLocalDb
import dagger.android.support.DaggerAppCompatActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.button
import org.jetbrains.anko.centerInParent
import org.jetbrains.anko.dip
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.padding
import org.jetbrains.anko.progressBar
import org.jetbrains.anko.relativeLayout
import org.jetbrains.anko.scrollView
import org.jetbrains.anko.textView
import org.jetbrains.anko.verticalLayout
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity() {

  @Inject
  lateinit var prefs: SharedPreferences

  @Inject
  lateinit var todoRepository: TodoRepository

  private var disposable = CompositeDisposable()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    showInitial()
  }

  override fun onResume() {
    super.onResume()
    disposable.add(todoRepository.all().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
      {
        showTodos(it)
      }))
  }

  override fun onPause() {
    disposable.clear()
    super.onPause()
  }

  private fun insertDummyTodos() {
    doAsync {
      var todos: List<Todo> = emptyList()

      for (i in 1..10) {
        todos = todos.plus(Todo(title = "Title $i", message = "Message $i"))
      }
      todoRepository.insert(todos)
    }
  }

  private fun showTodos(todo: List<Todo>) {
    setContentView(scrollView {
      verticalLayout {
        button("Use realm").setOnClickListener { useRealm() }
        button("Use sqlite").setOnClickListener { useSqlite() }
        button("Insert dummy items").setOnClickListener { insertDummyTodos() }
        textView(todo.toString()).lparams { padding = dip(10) }
      }
    })
  }

  private fun useSqlite() {
    prefs.saveLocalDb(LocalDb.SQLITE)
    (application as App).restart()
  }

  private fun useRealm() {
    prefs.saveLocalDb(LocalDb.REALM)
    (application as App).restart()
  }

  private fun showInitial() {
    setContentView(relativeLayout {
      progressBar().lparams { centerInParent() }
    })
  }

}
