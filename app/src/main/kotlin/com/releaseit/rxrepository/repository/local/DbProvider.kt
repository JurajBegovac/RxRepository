package com.releaseit.rxrepository.repository.local

import io.reactivex.Flowable
import io.reactivex.functions.Consumer
import io.reactivex.functions.Function
import org.funktionale.option.Option

/**
 * Created by jurajbegovac on 07/02/2018.
 */

interface DbProvider<Item, DbProvider> : Repository<Item> {

  fun update(updateSpecification: UpdateSpecification<DbProvider>) // maybe we should return number of updated items

  fun insertOrUpdate(insertOrUpdateSpecification: InsertOrUpdateSpecification<DbProvider>)  //  maybe we should return number of inserted/updated items

  fun delete(deleteSpecification: DeleteSpecification<DbProvider>) // maybe we should return number of deleted item

  fun get(querySpecification: QuerySpecification<Option<Item>, DbProvider>): Flowable<Option<Item>>

  fun query(querySpecification: QuerySpecification<List<Item>, DbProvider>): Flowable<List<Item>>
}

interface QuerySpecification<T, DbProvider> : Function<DbProvider, Flowable<T>>

interface UpdateSpecification<DbProvider> : Consumer<DbProvider>

interface InsertOrUpdateSpecification<DbProvider> : Consumer<DbProvider>

interface DeleteSpecification<DbProvider> : Consumer<DbProvider>
