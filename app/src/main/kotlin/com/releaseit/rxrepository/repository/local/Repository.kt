package com.releaseit.rxrepository.repository.local

/**
 * Created by jurajbegovac on 26/01/2018.
 */

interface Repository<in Item> {

  fun insert(item: Item) // maybe we should return id

  fun insert(items: List<Item>) // maybe we should return number of inserted items

  fun update(item: Item) // maybe we should return number of updated items

  fun upsert(item: Item) // maybe we should return id of inserted/updated item

  fun delete(item: Item) // maybe we should return id of deleted item

}
