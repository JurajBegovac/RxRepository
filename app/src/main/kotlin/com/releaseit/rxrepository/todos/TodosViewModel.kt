package com.releaseit.rxrepository.todos

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.util.Log
import com.releaseit.rxrepository.repository.local.todos.Todo
import com.releaseit.rxrepository.repository.local.todos.TodoRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject

/**
 * Created by jurajbegovac on 09/02/2018.
 */

internal data class State(val showLoading: Boolean, val items: List<Todo>, val showError: Boolean)

class TodosViewModel(private val todoRepository: TodoRepository) : ViewModel() {

  private val disposable = CompositeDisposable()
  private val stateSubject: BehaviorSubject<State> = BehaviorSubject.createDefault(State(true, emptyList(), false))

  var showLoading = MutableLiveData<Boolean>()
  var showError = MutableLiveData<Boolean>()
  var items = MutableLiveData<List<TodoViewItem>>()

  init {
    disposable.addAll(todosDisposable(), stateDisposable())
  }

  private fun stateDisposable() =
    stateSubject.subscribe {
      showLoading.value = it.showLoading
      showError.value = it.showError
      items.value = it.items.map { TodoViewItem(it.title, it.message) }
    }

  override fun onCleared() {
    disposable.clear()
    super.onCleared()
  }

  private fun todosDisposable() =
    todoRepository.all()
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe({ stateSubject.onNext(State(false, it, false)) })

  fun addTodo() {
    // todo navigate to add item activity
    Log.d("Juraj", "add todo")
  }

  fun itemPressed(position: Int) {
    val item = stateSubject.value?.items?.get(position)
    Log.d("Juraj", "$item")
    // todo navigate to edit item activity
  }

}

data class TodoViewItem(val title: String, val message: String)

class TodoViewModelFactory(private val todoRepository: TodoRepository) : ViewModelProvider.Factory {
  override fun <T : ViewModel?> create(modelClass: Class<T>): T {
    if (modelClass.isAssignableFrom(TodosViewModel::class.java)) {
      return TodosViewModel(todoRepository) as T
    }
    throw IllegalArgumentException("Unknown ViewModel class")
  }
}
