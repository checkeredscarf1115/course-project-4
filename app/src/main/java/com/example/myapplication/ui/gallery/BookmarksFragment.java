package com.example.myapplication.ui.gallery;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.databinding.FragmentBookmarksBinding;
import com.example.myapplication.domain.bookmark.databases.BookmarkDB;
import com.example.myapplication.domain.bookmark.entities.WordEntity;

import java.util.ArrayList;
import java.util.List;

public class BookmarksFragment extends Fragment {

    private FragmentBookmarksBinding binding;
    private RecyclerView rv;
    private BookmarkAdapter adapter;
//    private List<WordEntity> wordEntityList;
//    private List<List<SynsEntity>> synsEntityList;
//    private List<List<AntsEntity>> antsEntitiesList;
//    private List<List<ContextsEntity>> contextsEntitiesList;
    private BookmarksRepository bookmarksRepository;
    private boolean isScrolling = false;
    private int currentItems = 0, totalItems = 0, scrolledOutItems = 0;
    private ProgressBar progressBar;
    final int itemsToLoad = 10;
    private boolean isScrollEndReached = false;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentBookmarksBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        progressBar = binding.progressBar;

        bookmarksRepository = new BookmarksRepository();

        rv = binding.bookmarkRecyclerView;
        adapter = new BookmarkAdapter(getActivity(), bookmarksRepository);
        SelectAsyncTask selectAsyncTask = new SelectAsyncTask();
        selectAsyncTask.execute();
        rv.setAdapter(adapter);

        rv.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (isScrollEndReached) {
                    return;
                }

                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) rv.getLayoutManager();

                currentItems = linearLayoutManager.getChildCount();
                totalItems = linearLayoutManager.getItemCount();
                scrolledOutItems = linearLayoutManager.findFirstVisibleItemPosition();

                if (isScrolling && (currentItems + scrolledOutItems == totalItems)) {
                    isScrolling = false;
                    //fetch data
                    //fetchData();
                    SelectAsyncTask selectAsyncTask = new SelectAsyncTask();
                    selectAsyncTask.execute();
                }
            }
        });

        return root;
    }

//    private void fetchData() {
//        progressBar.setVisibility(View.VISIBLE);
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                for (int i = 0; i < 3; i++) {
//
//                    List<WordEntity> list = BookmarkDB.getInstance(getActivity())
//                            .wordDao()
//                            .selectLimitOffset(1, currentItems);
//
//                    if (list.size() <= 0) {
//                        break;
//                    }
//
//                    wordEntityList.add(list.get(0));
//
//                    adapter.notifyDataSetChanged();
//                    Log.i("bookmark_fragment", "notified adapter " + adapter.getItemCount() + " " + list.toString());
//                }
//            }
//        }, 5000);
//    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    class SelectAsyncTask extends AsyncTask<Void, Void, Void> {
        private int itemsCount;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            itemsCount = totalItems;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            for (int i = 0; i < itemsToLoad; i++) {

//                Log.d("bookmark_fragment", "currentItems" + Integer.toString(currentItems));
//                Log.d("bookmark_fragment", "scrolledOutItems" +Integer.toString(scrolledOutItems));
//                Log.d("bookmark_fragment", "totalItems" +Integer.toString(totalItems));
                List<WordEntity> list = BookmarkDB.getInstance(getActivity())
                        .wordDao()
                        .selectLimitOffset(1, totalItems + i);

                if (list.size() <= 0) {
                    isScrollEndReached = true;
                    break;
                }

                //Log.d("bookmark_fragment", list.toString());
                bookmarksRepository.getWordEntityList().add(list.get(0));

//                AddToTextView(list.get(0).getWordID(), );


                for (int j = 0; j < itemsToLoad * 2; j++) {
                    //while (synsEntityList.size() <= totalItems + i - 1)
                    bookmarksRepository.getSynsEntityList().add(new ArrayList<>());
                    //while (antsEntitiesList.size() <= totalItems + i - 1)
                    bookmarksRepository.getAntsEntitiesList().add(new ArrayList<>());
                    //while (contextsEntitiesList.size() <= totalItems + i - 1)
                    bookmarksRepository.getContextsEntitiesList().add(new ArrayList<>());
                }
//
//                synsEntityList.get(totalItems + i).clear();
//                antsEntitiesList.get(totalItems + i).clear();
//                contextsEntitiesList.get(totalItems + i).clear();

                //Log.d("bookmark", "" + totalItems + i);

                //if (synsEntityList.size() - 1 < totalItems + i)
                bookmarksRepository.getSynsEntityList().get(totalItems + i).addAll(BookmarkDB.getInstance(getContext()).synsDao().getByWordID(list.get(0).getWordID()));
                bookmarksRepository.getAntsEntitiesList().get(totalItems + i).addAll(BookmarkDB.getInstance(getContext()).antsDao().getByWordID(list.get(0).getWordID()));
                bookmarksRepository.getContextsEntitiesList().get(totalItems + i).addAll(BookmarkDB.getInstance(getContext()).contextsDao().getByWordID(list.get(0).getWordID()));
            }
            return null;
        }

//        private void AddToTextView() {
//            for (int i = 0; i < )
//        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
//            if (wordEntityList.size() > 0) {
//                adapter.notifyDataSetChanged();
//                //Log.i("bookmark_fragment", "notified adapter " + adapter.getItemCount() + " " + wordEntityList.toString());
//            }

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    for (int i = 0; i < itemsToLoad + 1; i++) {
                        //Log.d("bookmark", "" + itemsCount + i);
                        adapter.notifyItemChanged(i + itemsCount);
                    }
                    progressBar.setVisibility(View.GONE);
                }
            }, 2000);
        }
    }



    class FinishingAsyncTask extends AsyncTask<Void, Void, Void> {
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            progressBar.setVisibility(View.VISIBLE);
//        }

        @Override
        protected Void doInBackground(Void... voids) {
            for (int i = 0; i < itemsToLoad; i++) {

//                Log.d("bookmark_fragment", "currentItems" + Integer.toString(currentItems));
//                Log.d("bookmark_fragment", "scrolledOutItems" +Integer.toString(scrolledOutItems));
//                Log.d("bookmark_fragment", "totalItems" +Integer.toString(totalItems));
                List<WordEntity> list = BookmarkDB.getInstance(getActivity())
                        .wordDao()
                        .selectLimitOffset(1, totalItems + i);

                if (list.size() <= 0) {
                    break;
                }

                //Log.d("bookmark_fragment", list.toString());
                bookmarksRepository.getWordEntityList().add(list.get(0));
            }
//            wordEntityList = BookmarkDB.getInstance(getActivity())
//                    .wordDao()
//                    .selectLimitOffset(10, 0);
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            if (bookmarksRepository.getWordEntityList().size() > 0) {
                adapter.notifyDataSetChanged();
                //Log.i("bookmark_fragment", "notified adapter " + adapter.getItemCount() + " " + wordEntityList.toString());
            }
            progressBar.setVisibility(View.GONE);
        }
    }
}