package com.example.myapplication.ui.PageResult;

import android.content.Context;
import android.content.Intent;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.google.android.flexbox.FlexboxLayout;

public class PageResultAdapter extends RecyclerView.Adapter<PageResultAdapter.ViewHolder> {
//    private final List<List<String>> meaningList;
//    private final List<List<String>> synsList;
//    private final List<List<String>> antsList;
//    private final List<List<String>> contextList;
    private final LayoutInflater inflater;
    private final Context context;
    private final int langTo;
    private final ResultRepository repository;

//    public PageResultAdapter(List<List<String>> meaningList, List<List<String>> synsList,
//                             List<List<String>> antsList, List<List<String>> contextList,
//                             Context context, int langTo) {
//        this.meaningList = meaningList;
//        this.synsList = synsList;
//        this.antsList = antsList;
//        this.contextList = contextList;
//        this.inflater = LayoutInflater.from(context);
//        this.context = context;
//        this.langTo = langTo;
//    }


    public PageResultAdapter(Context context, int langTo, ResultRepository repository) {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.langTo = langTo;
        this.repository = repository;
    }

    @Override
    public PageResultAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_pageresult, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PageResultAdapter.ViewHolder holder, int position) {
        //Log.i("adapter", "started updating view");

        if (repository.getCompsTo().size() > 0) {
            setVisibilityVisible(holder.tvWordCompTo);
            holder.tvWordCompTo.setText(repository.getCompsTo().get(position));
        } else {
            setVisibilityGone(holder.tvWordCompTo);
        }

        if (repository.getDefsTo().size() > 0) {
            setVisibilityVisible(holder.tvDefTo);
            holder.tvDefTo.setText(repository.getDefsTo().get(position));
        } else {
            setVisibilityGone(holder.tvDefTo);
        }

        if (repository.getDefsFrom().size() > 0) {
            setVisibilityVisible(holder.tvDefFrom);
            holder.tvDefFrom.setText(repository.getDefsFrom().get(position));
        } else {
            setVisibilityGone(holder.tvDefFrom);
        }

        ContextThemeWrapper contextTextDF = new ContextThemeWrapper(context, R.style.text_df_margin);
        Button btn;
        int i;

        if (repository.getSynsList().get(position).size() == 0) {
            setVisibilityGone(holder.layoutSyns);
        } else
        if (repository.getSynsList().size() > 0 && position < repository.getSynsList().size()) {
            for (i = 0; i < repository.getSynsList().get(position).size(); i++) {
                setVisibilityVisible(holder.layoutSyns);
                btn = new Button(contextTextDF);
                holder.layoutSyns.addView(StyleButton(btn, repository.getSynsList().get(position).get(i)));
            }
        } else {
            setVisibilityGone(holder.layoutSyns);
        }

        if (repository.getAntsList().get(position).size() == 0) {
            setVisibilityGone(holder.layoutAnts);
        } else
        if (repository.getAntsList().size() > 0 && position < repository.getAntsList().size()) {
            for (i = 0; i < repository.getAntsList().get(position).size(); i++) {
                setVisibilityVisible(holder.layoutAnts);
                btn = new Button(contextTextDF);
                holder.layoutAnts.addView(StyleButton(btn, repository.getAntsList().get(position).get(i)));
            }
        } else {
            setVisibilityGone(holder.layoutAnts);
        }

        if (repository.getContextList().get(position).size() == 0) {
            setVisibilityGone(holder.tvContext);
        } else
        if (repository.getContextList().size() > 0 && position < repository.getContextList().size()) {

            setVisibilityVisible(holder.tvContext);
            holder.tvContext.setText("");
            for (i = 0; i < repository.getContextList().get(position).size(); i++) {

                holder.tvContext.append(repository.getContextList().get(position).get(i));
            }
        }



//        Log.d("adapter", repository.getDefsFrom().get(position) +
//                repository.getDefsTo().get(position) +
//                repository.getDefsTo().size());
    }

    private void setVisibilityGone(View view) {
        view.setVisibility(View.GONE);
    }

    private void setVisibilityVisible(View view) {
        view.setVisibility(View.VISIBLE);
    }

    private Button StyleButton(Button btn, String text) {
        btn.setText(text);
        btn.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        btn.setTransformationMethod(null);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PageResultActivity.class);
                intent.putExtra("word", btn.getText().toString());
                intent.putExtra("langFrom", langTo);
                intent.putExtra("langTo", langTo);
                context.startActivity(intent);
            }
        });

        return btn;
    }

    @Override
    public int getItemCount() {
        if (repository.getDefsTo() != null) {
            return repository.getDefsTo().size();
        }
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final FlexboxLayout layoutSyns, layoutAnts;
        private final TextView tvDefTo, tvContext, tvDefFrom, tvWordCompTo;
        ViewHolder(View view){
            super(view);
            layoutSyns = view.findViewById(R.id.syns_buttons_layout);
            layoutAnts = view.findViewById(R.id.ants_buttons_layout);
            tvDefFrom = view.findViewById(R.id.defFrom);
            tvDefTo = view.findViewById(R.id.defTo);
            tvContext = view.findViewById(R.id.context);
            tvWordCompTo = view.findViewById(R.id.word_comp_to);
        }
    }
}
