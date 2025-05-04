package com.example.FarmerAdminAgrimart;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import android.widget.TextView;
import android.app.AlertDialog;
import android.widget.EditText;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link QueriesPage#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QueriesPage extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView recyclerView;
    private QueryAdapter adapter;
    private List<QueryItem> queryList;

    public QueriesPage() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment QueriesPage.
     */
    // TODO: Rename and change types and number of parameters
    public static QueriesPage newInstance(String param1, String param2) {
        QueriesPage fragment = new QueriesPage();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_queries_page, container, false);

        recyclerView = view.findViewById(R.id.queries_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Static questions and answers
        queryList = new ArrayList<>();
        queryList.add(new QueryItem("How can I update the stock availability of my product?",
                "You can update stock by going to 'Products' in the app, selecting the product, and edit the stock quantity."));
        queryList.add(new QueryItem("How can I update the stock availability of my product?",
                "You can update stock by going to 'Products' in the app, selecting the product, and edit the stock quantity."));
        queryList.add(new QueryItem("How can I update the stock availability of my product?",
                "You can update stock by going to 'Products' in the app, selecting the product, and edit the stock quantity."));
        queryList.add(new QueryItem("How can I update the stock availability of my product?",
                "You can update stock by going to 'Products' in the app, selecting the product, and edit the stock quantity."));
        queryList.add(new QueryItem("How can I update the stock availability of my product?",
                "You can update stock by going to 'Products' in the app, selecting the product, and edit the stock quantity."));
        queryList.add(new QueryItem("How can I update the stock availability of my product?",
                "You can update stock by going to 'Products' in the app, selecting the product, and edit the stock quantity."));
        queryList.add(new QueryItem("How can I update the stock availability of my product?",
                "You can update stock by going to 'Products' in the app, selecting the product, and edit the stock quantity."));
        queryList.add(new QueryItem("How can I update the stock availability of my product?",
                "You can update stock by going to 'Products' in the app, selecting the product, and edit the stock quantity."));
        queryList.add(new QueryItem("How can I update the stock availability of my product?",
                "You can update stock by going to 'Products' in the app, selecting the product, and edit the stock quantity."));
        queryList.add(new QueryItem("How can I update the stock availability of my product?",
                "You can update stock by going to 'Products' in the app, selecting the product, and edit the stock quantity."));
        queryList.add(new QueryItem("How can I update the stock availability of my product?",
                "You can update stock by going to 'Products' in the app, selecting the product, and edit the stock quantity."));
        adapter = new QueryAdapter(queryList);
        recyclerView.setAdapter(adapter);

        // Back button functionality
        view.findViewById(R.id.back_button).setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());

        // Add Query button functionality
        Button addQueryBtn = view.findViewById(R.id.add_query_button);
        addQueryBtn.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Ask a Query");
            final EditText input = new EditText(getContext());
            input.setHint("Type your question here");
            builder.setView(input);
            builder.setPositiveButton("Submit", (dialog, which) -> {
                String question = input.getText().toString().trim();
                if (!question.isEmpty()) {
                    queryList.add(0, new QueryItem(question, "Thank you for your query! Our team will respond soon."));
                    adapter.notifyItemInserted(0);
                    recyclerView.scrollToPosition(0);
                }
            });
            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
            builder.show();
        });

        return view;
    }

    // Query item model
    public static class QueryItem {
        String question;
        String answer;
        public QueryItem(String question, String answer) {
            this.question = question;
            this.answer = answer;
        }
    }

    // RecyclerView Adapter
    public class QueryAdapter extends RecyclerView.Adapter<QueryAdapter.QueryViewHolder> {
        private List<QueryItem> queries;
        public QueryAdapter(List<QueryItem> queries) {
            this.queries = queries;
        }
        @Override
        public QueryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.query_item, parent, false);
            return new QueryViewHolder(v);
        }
        @Override
        public void onBindViewHolder(QueryViewHolder holder, int position) {
            QueryItem item = queries.get(position);
            holder.questionText.setText(item.question);
            holder.answerText.setText(item.answer);
        }
        @Override
        public int getItemCount() {
            return queries.size();
        }
        class QueryViewHolder extends RecyclerView.ViewHolder {
            TextView questionText, answerText;
            QueryViewHolder(View itemView) {
                super(itemView);
                questionText = itemView.findViewById(R.id.query_question);
                answerText = itemView.findViewById(R.id.query_answer);
            }
        }
    }
}