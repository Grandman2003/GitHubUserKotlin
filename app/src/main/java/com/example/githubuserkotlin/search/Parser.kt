package com.example.githubuserkotlin.search

import android.os.AsyncTask
import android.view.View
import com.example.githubuserkotlin.entities.User
import com.example.githubuserkotlin.fragments.SearchFragments
import org.jsoup.Jsoup
import org.jsoup.internal.StringUtil
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import java.io.IOException
import java.io.PrintStream
import java.net.HttpURLConnection
import java.util.*
import java.util.function.Consumer

class Parser(private val fragment: SearchFragments) {
    var connection: HttpURLConnection? = null
    private var tasker: AsyncTask<*, *, *>? = null
    private var doc: Document? = null
    private var link: String? = null
    private var name: String? = null
    private var pages = 0
    private var current_page = 0
    private var users: ArrayList<User>? = null
    private val out = PrintStream(System.out)
    @Throws(IOException::class)
    fun searchNameChanged(name: String) {
        link = search_req + name + type
        this.name = name
        current_page = 1
        fragment.progressBar!!.visibility = View.VISIBLE
        if (tasker == null) {
            parse_it()
        } else {
            if (!tasker!!.isCancelled) {
                tasker!!.cancel(true)
            }
            parse_it()
        }
    }

    @Throws(IOException::class)
    fun searchPageChangedNext() {
        current_page += 1
        link = search_page + current_page + "&q=" + name + type
        fragment.progressBar!!.visibility = View.VISIBLE
        if (tasker == null) {
            parse_it()
        } else {
            if (!tasker!!.isCancelled) {
                tasker!!.cancel(true)
            }
            parse_it()
        }
    }

    @Throws(IOException::class)
    fun searchPageChangedPrevious() {
        current_page -= 1
        link = search_page + current_page + "&q=" + name + type
        fragment.progressBar!!.visibility = View.VISIBLE
        if (tasker == null) {
            parse_it()
        } else {
            if (!tasker!!.isCancelled) {
                tasker!!.cancel(true)
            }
            parse_it()
        }
    }

    @Throws(IOException::class)
    fun parse_it() {
        tasker = object : AsyncTask<Any?, Any?, Any?>() {
            override fun onPostExecute(o: Any?) {
                super.onPostExecute(o)
                usersResult
            }

            override fun doInBackground(objects: Array<Any?>): Any? {
                try {
                    users = ArrayList()
                    doc = Jsoup.connect(link).get()
                    val table = doc?.getElementsByClass("d-flex hx_hit-user px-0 Box-row")
                    table?.stream()?.forEach { s: Element ->
                        var fullname: String? = null
                        var nickname: String? = null
                        if (!s.getElementsByClass("mr-1").isEmpty()) {
                            fullname = s.getElementsByClass("mr-1")[0].text()
                        }
                        if (!s.getElementsByClass("color-text-secondary").isEmpty()) {
                            nickname = s.getElementsByClass("color-text-secondary")[0].text()
                        }
                        users!!.add(User(nickname, fullname))
                    }
                    pages =
                        if (!doc?.getElementsByClass("paginate-container codesearch-pagination-container")
                                ?.isEmpty()!!
                        ) {
                            val table_1 =
                                doc?.getElementsByClass("paginate-container codesearch-pagination-container")
                                    ?.get(0)
                                    ?.getElementsByTag(
                                        "a"
                                    )
                            val numbers = ArrayList<String>()
                            table_1?.stream()?.forEach { p: Element -> numbers.add(p.text()) }
                            //out.println(numbers.get(numbers.size()-2));}else{
                            getDigits(numbers)
                        } else {
                            0
                        }
                    out.println(pages)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                return null
            }
        }
        (tasker as AsyncTask<Any?, Any?, Any?>).execute()
    }

    private val usersResult: Unit
        private get() {
            if (users != null) {
                users!!.forEach(Consumer { s: User -> out.println(s.nickname) })
                check_position()
                fragment.progressBar!!.visibility = View.GONE
                fragment.number!!.text = current_page.toString()
                fragment.adapter!!.setUsers(users!!)
            }
        }

    private fun getDigits(strings: ArrayList<String>): Int {
        val numbers = ArrayList<Int>()
        for (k in strings) {
            if (StringUtil.isNumeric(k)) {
                numbers.add(k.toInt())
            }
        }
        numbers.sortWith(Comparator.naturalOrder())
        return if (numbers.size != 0) {
            numbers[numbers.size - 1]
        } else 0
    }

    private fun check_position() {
        if (pages != 0) {
            if (current_page == 1) {
                fragment.onFirstPage()
            } else if (pages - current_page <= 0) {
                fragment.onLastPage()
            } else {
                fragment.activate()
            }
        } else {
            fragment.disactivate()
        }
    }

    companion object {
        private const val search_req = "https://github.com/search?p=1&q="
        private const val search_page = "https://github.com/search?p="
        private const val type = "&type=users"
    }
}