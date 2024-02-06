package com.example.myapplication.ui.gallery;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.ui.PageResult.PageResultActivity;

public class BookmarkAdapter extends RecyclerView.Adapter<BookmarkAdapter.ViewHolder> {
//    private final List<WordEntity> wordEntityList;
//    private final List<List<SynsEntity>> synsEntities;
//    private final List<List<AntsEntity>> antsEntities;
//    private final List<List<ContextsEntity>> contextsEntities;
    private final LayoutInflater inflater;
    private final BookmarksRepository bookmarksRepository;
    //private int itemsLoaded = 0;

//    public BookmarkAdapter(Context context,
//                           List<WordEntity> wordEntityList,
//                           List<List<SynsEntity>> synsEntities,
//                           List<List<AntsEntity>> antsEntities,
//                           List<List<ContextsEntity>> contextsEntities) {
//        this.inflater = LayoutInflater.from(context);
//        this.wordEntityList = wordEntityList;
//        this.synsEntities = synsEntities;
//        this.antsEntities = antsEntities;
//        this.contextsEntities = contextsEntities;
//    }


    public BookmarkAdapter(Context context,BookmarksRepository bookmarksRepository) {
        this.inflater = LayoutInflater.from(context);
        this.bookmarksRepository = bookmarksRepository;

//        this.wordEntityList = bookmarksRepository.getWordEntityList();
//        this.synsEntities = bookmarksRepository.getSynsEntityList();
//        this.antsEntities = bookmarksRepository.getAntsEntitiesList();
//        this.contextsEntities = bookmarksRepository.getContextsEntitiesList();
    }

    @Override
    public BookmarkAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_bookmark, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BookmarkAdapter.ViewHolder holder, int position) {
        holder.tvWordFrom.setText(bookmarksRepository.getWordEntityList().get(position).getWordComp());
        holder.tvWordTo.setText(bookmarksRepository.getWordEntityList().get(position).getTranslation() + ", " + bookmarksRepository.getWordEntityList().get(position).getWordDef());

        if (bookmarksRepository.getSynsEntityList().get(position).size() > 0) {
            setVisibilityVisible(holder.tvSyns);
            holder.tvSyns.setText("s:" + bookmarksRepository.getSynsEntityList().get(position).toString());
        }
        else  {
            setVisibilityGone(holder.tvSyns);
        }

        if (bookmarksRepository.getAntsEntitiesList().get(position).size() > 0) {
            setVisibilityVisible(holder.tvAnts);
            holder.tvAnts.setText("a:" + bookmarksRepository.getAntsEntitiesList().get(position).toString());
        }
        else  {
            setVisibilityGone(holder.tvAnts);
        }

        if (bookmarksRepository.getContextsEntitiesList().get(position).size() > 0) {
            setVisibilityVisible(holder.tvContexts);
            holder.tvContexts.setText("c:" + bookmarksRepository.getContextsEntitiesList().get(position).toString());
        }
        else {
            setVisibilityGone(holder.tvContexts);
        }
        String langFrom = bookmarksRepository.getWordEntityList().get(position).getLanguageFrom();
        String langTo = bookmarksRepository.getWordEntityList().get(position).getLanguageTo();
        holder.imgvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(holder.imgvSearch.getContext(), PageResultActivity.class);
                intent.putExtra("word", holder.tvWordFrom.getText().toString());
                intent.putExtra("langFrom", GetLanguageIndex(langFrom));
                intent.putExtra("langTo", GetLanguageIndex(langTo));
                holder.imgvSearch.getContext().startActivity(intent);
            }
        });
        Log.i("bookmark", "viewholder updated");
    }

    private int GetLanguageIndex(String lang) {
        switch (lang) {
            case "English": {
                return 0;
            }
            case "Русский": {
                return 1;
            }
            default: {
                return 0;
            }
        }
    }
    private void setVisibilityGone(View view) {
        view.setVisibility(View.GONE);
    }

    private void setVisibilityVisible(View view) {
        view.setVisibility(View.VISIBLE);
    }

    @Override
    public int getItemCount() {
//        if (repository.getDefsTo() != null) {
//            return repository.getDefsTo().size();
//        }
        return bookmarksRepository.getWordEntityList().size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView tvWordFrom, tvWordTo, tvSyns, tvAnts, tvContexts;
        final ImageView imgvSearch;
        ViewHolder(View view){
            super(view);
            tvWordFrom = view.findViewById(R.id.word_comp);
            tvWordTo = view.findViewById(R.id.translation);
            tvSyns = view.findViewById(R.id.syns);
            tvAnts = view.findViewById(R.id.ants);
            tvContexts = view.findViewById(R.id.context);
            imgvSearch = view.findViewById(R.id.search_imgview);
        }
    }


}
