package com.releaseit.rxrepository.repository.local

import io.reactivex.Flowable
import io.reactivex.functions.Consumer
import io.reactivex.functions.Function
import org.funktionale.option.Option

/**
 * Created by jurajbegovac on 07/02/2018.
 */

interface DbProvider<Item, DbProvider> : Repository<Item> {

  fun update(updateSpecification: UpdateSpecification<DbProvider>)

  fun insertOrUpdate(insertOrUpdateSpecification: InsertOrUpdateSpecification<DbProvider>)

  fun delete(deleteSpecification: DeleteSpecification<DbProvider>)

  fun get(querySpecification: QuerySpecification<Option<Item>, DbProvider>): Flowable<Option<Item>>

  fun query(querySpecification: QuerySpecification<List<Item>, DbProvider>): Flowable<List<Item>>
}

interface QuerySpecification<T, DbProvider> : Function<DbProvider, Flowable<T>>

interface UpdateSpecification<DbProvider> : Consumer<DbProvider>

interface InsertOrUpdateSpecification<DbProvider> : Consumer<DbProvider>

interface DeleteSpecification<DbProvider> : Consumer<DbProvider>
