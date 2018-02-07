package com.releaseit.rxrepository.repository.local.realm

import com.releaseit.rxrepository.repository.local.*
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.realm.Realm
import io.realm.RealmChangeListener
import io.realm.RealmObject
import io.realm.RealmResults
import org.funktionale.option.Option
import javax.inject.Qualifier

/**
 * Created by jurajbegovac on 26/01/2018.
 * */

/**
 * Dagger2 qualifier
 */
//@Qualifier
//@MustBeDocumented
//@Retention(AnnotationRetention.RUNTIME)
//annotation class RealmRepo

/**
 * RealmRepository
 */
abstract class RealmRepository<Item, RealmDbModel : RealmObject>(private val mapper: (Item) -> RealmDbModel) : DbProvider<Item, Realm> {

    override fun insert(item: Item) =
            Realm.getDefaultInstance().executeTransaction {
                it.insert(mapper(item))
            }

    override fun insert(items: List<Item>) =
            Realm.getDefaultInstance().executeTransaction {
                it.insert(items.map { mapper(it) })
            }

    override fun update(item: Item) =
            Realm.getDefaultInstance().executeTransaction {
                it.insertOrUpdate(mapper(item))
            }

    override fun update(updateSpecification: UpdateSpecification<Realm>) =
            Realm.getDefaultInstance().executeTransaction {
                updateSpecification.accept(it)
            }

    override fun upsert(item: Item) =
            Realm.getDefaultInstance().executeTransaction {
                it.insertOrUpdate(mapper(item))
            }

    override fun insertOrUpdate(insertOrUpdateSpecification: InsertOrUpdateSpecification<Realm>) =
            Realm.getDefaultInstance().executeTransaction {
                insertOrUpdateSpecification.accept(it)
            }

    override fun delete(item: Item) =
            Realm.getDefaultInstance().executeTransaction {
                mapper(item).deleteFromRealm()
            }

    override fun delete(deleteSpecification: DeleteSpecification<Realm>) =
            Realm.getDefaultInstance().executeTransaction {
                deleteSpecification.accept(it)
            }

    override fun get(querySpecification: QuerySpecification<Option<Item>, Realm>): Flowable<Option<Item>> =
            Flowable.using(
                    { Realm.getDefaultInstance() },
                    { querySpecification.apply(it) },
                    { realm -> realm.close() })
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .unsubscribeOn(AndroidSchedulers.mainThread())

    override fun query(querySpecification: QuerySpecification<List<Item>, Realm>): Flowable<List<Item>> =
            Flowable.using(
                    { Realm.getDefaultInstance() },
                    { querySpecification.apply(it) },
                    { realm ->
                        realm.close()
                    })
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .unsubscribeOn(AndroidSchedulers.mainThread())

    fun <T> createQuerySpecification(realmResultsMapper: (Realm) -> RealmResults<RealmDbModel>, mapper: (RealmResults<RealmDbModel>) -> T): QuerySpecification<T, Realm> =
            RealmQuerySpecification(realmResultsMapper = realmResultsMapper, returnMapper = mapper)
}


class RealmQuerySpecification<T, RealmDbModel : RealmObject>(private val realmResultsMapper: (Realm) -> RealmResults<RealmDbModel>,
                                                             private val returnMapper: (RealmResults<RealmDbModel>) -> T) : QuerySpecification<T, Realm> {

    override fun apply(realm: Realm): Flowable<T> {
        return Flowable.create ({ e ->
            val realmResults = realmResultsMapper(realm)
            e.onNext(returnMapper(realmResults))

            val realmChangeListener: RealmChangeListener<RealmResults<RealmDbModel>> = RealmChangeListener {
                e.onNext(returnMapper(it))
            }
            realmResults.addChangeListener(realmChangeListener)

            e.setCancellable {
                if (realmResults.isValid)
                    realmResults.removeChangeListener(realmChangeListener)
            }
        }, BackpressureStrategy.BUFFER)
    }

}
