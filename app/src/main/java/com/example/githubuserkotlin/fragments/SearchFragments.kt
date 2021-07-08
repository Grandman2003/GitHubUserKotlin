package com.example.githubuserkotlin.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.SearchView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.githubuserkotlin.R
import com.example.githubuserkotlin.adapters.SearchUsersAdapter
import com.example.githubuserkotlin.search.Parser
import java.io.IOException

class SearchFragments : Fragment() {
    private var searchView: SearchView? = null
    var recyclerView: RecyclerView? = null;

    @JvmField
    var progressBar: ProgressBar? = null;
    private var previous_button: Button? = null
    private var next_button: Button? = null

    @JvmField
    var number: TextView? = null
    private var parser: Parser? = null

    @JvmField
    var adapter: SearchUsersAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)
        searchView = view.findViewById(R.id.searchview)
        recyclerView = view.findViewById(R.id.recycle_search)
        next_button = view.findViewById(R.id.next_id)
        previous_button = view.findViewById(R.id.previous_id)
        number = view.findViewById(R.id.page_number)
        //recyclerView.layoutManager(LinearLayoutManager(context))
        recyclerView?.layoutManager=LinearLayoutManager(context);
        progressBar = view.findViewById(R.id.progressBar)
        progressBar?.visibility=View.GONE;
        requireActivity().setTitle(R.string.search)
        adapter = SearchUsersAdapter()
        recyclerView?.adapter=adapter
        parser = Parser(this)
        disactivate()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        next_button!!.setOnClickListener {
            try {
                onNextClick()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        previous_button!!.setOnClickListener {
            try {
                onPreviousClick()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        searchView!!.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                try {
                    parser!!.searchNameChanged(query)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                /* try {
                   parser.searchNameChanged(newText);
                 } catch (IOException e) {
                   e.printStackTrace();
                 } */
                return true
            }
        })
    }

    @Throws(IOException::class)
    fun onNextClick() {
        parser!!.searchPageChangedNext()
    }

    @Throws(IOException::class)
    fun onPreviousClick() {
        parser!!.searchPageChangedPrevious()
    }

    fun onFirstPage() {
        next_button!!.visibility = View.VISIBLE
        next_button!!.isClickable = true
        previous_button!!.isClickable = false
        previous_button!!.visibility = View.INVISIBLE
        number!!.visibility = View.VISIBLE
    }

    fun onLastPage() {
        next_button!!.visibility = View.INVISIBLE
        next_button!!.isClickable = false
        previous_button!!.isClickable = true
        previous_button!!.visibility = View.VISIBLE
        number!!.visibility = View.VISIBLE
    }

    fun activate() {
        next_button!!.visibility = View.VISIBLE
        next_button!!.isClickable = true
        previous_button!!.isClickable = true
        previous_button!!.visibility = View.VISIBLE
        number!!.visibility = View.VISIBLE
    }

    fun disactivate() {
        next_button!!.visibility = View.INVISIBLE
        next_button!!.isClickable = false
        previous_button!!.isClickable = false
        previous_button!!.visibility = View.INVISIBLE
        number!!.visibility = View.INVISIBLE
    }
}


