package com.example.infinitestock.ui.stock

import androidx.lifecycle.ViewModel
import com.example.infinitestock.entity.StockItem

class EntryStockViewModel : ViewModel() {
    private var items = arrayListOf<StockItem>()
    var tempString = String()

    fun getAllItems(): ArrayList<StockItem> {
        return items
    }

    fun getItem(string: String): StockItem {
        return items[searchId(string)]
    }

    fun addItem(item: StockItem) {
        if (searchId(item.name) == -1) {
            items.add(item)
        } else {
            editItem(item.name, item)
        }
    }

    fun editItem(string: String, item: StockItem) {
        val id = searchId(string)
        if (id == -1){
            addItem(item)
        } else {
            items.set(id, item)
        }
    }

    fun removeItem(item: StockItem) {
        val id = searchId(item.name)
        if (id != -1) {
            items.removeAt(id)
        }
    }

    private fun searchId(string: String): Int {
        var indeks = 0
        for (item in items){
            if (item.name == string) {
                return indeks
            }
            indeks += 1
        }
        return -1
    }
}