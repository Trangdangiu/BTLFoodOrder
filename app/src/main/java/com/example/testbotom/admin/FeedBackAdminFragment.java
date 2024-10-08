package com.example.testbotom.admin;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.testbotom.Adapter.FeedbackAdapter;
import com.example.testbotom.Database.Create_database;
import com.example.testbotom.Database.Feedback;
import com.example.testbotom.R;

import java.util.List;


public class FeedBackAdminFragment extends Fragment {
    private RecyclerView recyclerView;
    private FeedbackAdapter feedbackAdapter;
    private List<Feedback> feedbackList;
    private Create_database database;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_feed_back_admin, container, false);




        database = new Create_database(getContext());

        // Lấy danh sách phản hồi từ database
        feedbackList = database.getAllFeedback();

        // Khởi tạo RecyclerView và Adapter
        recyclerView = view.findViewById(R.id.recycleview_feedback);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        feedbackAdapter = new FeedbackAdapter(feedbackList);
        recyclerView.setAdapter(feedbackAdapter);


        return view;

    }
}