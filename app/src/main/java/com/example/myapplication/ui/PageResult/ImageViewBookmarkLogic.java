package com.example.myapplication.ui.PageResult;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.myapplication.domain.bookmark.databases.BookmarkDB;
import com.example.myapplication.domain.bookmark.entities.AntsEntity;
import com.example.myapplication.domain.bookmark.entities.ContextsEntity;
import com.example.myapplication.domain.bookmark.entities.SynsEntity;
import com.example.myapplication.domain.bookmark.entities.WordEntity;

public class ImageViewBookmarkLogic {
    private ImageView imgViewBookmark;
    private ResultRepository repository;
    private String tvOrigWord, langFrom, langTo;
    private boolean checked;

    public ImageViewBookmarkLogic(ImageView imgViewBookmark, ResultRepository repository, String tvOrigWord, String langFrom, String langTo) {
        this.imgViewBookmark = imgViewBookmark;
        this.repository = repository;
        this.tvOrigWord = tvOrigWord;
        this.langFrom = langFrom;
        this.langTo = langTo;
    }

    public View.OnClickListener getListener() {
        return new View.OnClickListener() {
            private View view;

            @Override
            public void onClick(View view) {
                this.view = view;
                toggle();
                setDrawable();
                InteractWithDatabase();
            }

            private void toggle() {
                checked = !checked;
            }

            private void setDrawable() {
                ImageView iv = (ImageView) view;
                if (checked)
                    iv.setImageDrawable(view.getResources().getDrawable(android.R.drawable.star_big_on));
                else {
                    iv.setImageDrawable(view.getResources().getDrawable(android.R.drawable.star_big_off));
                }
            }

            private void InteractWithDatabase() {
                Log.d("bookmark", Boolean.toString(checked));
                if (checked) {
                    insertWord();
                } else {
                    deleteWord();
                }
            }

            private void deleteWord() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //for (int i = 0; i < repository.getDefsFrom().size(); i++) {
                            BookmarkDB.getInstance(imgViewBookmark.getContext())
                                    .wordDao()
                                    .deleteByWordComp(repository.getCompFrom());
                            Log.i("bookmark", "deleted " + repository.getCompFrom());
                        //}
                    }
                }).start();

            }

            private void insertWord() {
                Log.d("bookmark", repository.getDefsTo().toString());
                for (int i = 0; i < repository.getDefsTo().size(); i++) {
                    WordEntity wordEntity = new WordEntity(
                            tvOrigWord,
                            repository.getDefsTo().get(i),
                            //repository.getDefsFrom().get(i), //if size > 0
                            repository.getCompsTo().get(i),
                            langFrom,
                            langTo
                    );
                    InsertAsyncTask insertAsyncTask = new InsertAsyncTask();
                    insertAsyncTask.execute(wordEntity);
                }
            }
        };
    }

    public void Execute() {
        CheckAsyncTask asyncTask = new CheckAsyncTask();
        asyncTask.execute();


    }

    class InsertAsyncTask extends AsyncTask<WordEntity, Void, Void> {
        int id = 0;
        int serialNumber = 0;

        @Override
        protected Void doInBackground(WordEntity... wordEntities) {
            BookmarkDB.getInstance(imgViewBookmark.getContext())
                    .wordDao()
                    .insert(wordEntities[0]);
            //Log.i("bookmark", "inserted" + tvOrigWord);

            id = BookmarkDB.getInstance(imgViewBookmark.getContext())
                    .wordDao()
                    .getIDsByWord(wordEntities[0].getWordDef())
                    .get(0);

            for (int i = 0; i < repository.getCompsTo().size(); i++) {
                if (wordEntities[0].getWordDef().equals(repository.getDefsTo().get(i))) {
                    serialNumber = i;
                    break;
                }
            }

            //Log.i("bookmark", "id:" + id + " sn:" + serialNumber);
            for (int i = 0; i < repository.getSynsList().get(serialNumber).size(); i++) {
                SynsEntity synsEntity = new SynsEntity(id, repository.getSynsList().get(serialNumber).get(i));
                //SynsInsertAsyncTask synsInsertAsyncTask = new SynsInsertAsyncTask();
                //synsInsertAsyncTask.execute(synsEntity);
                BookmarkDB.getInstance(imgViewBookmark.getContext())
                        .synsDao()
                        .insert(synsEntity);
            }

            for (int i = 0; i < repository.getAntsList().get(serialNumber).size(); i++) {
                AntsEntity antsEntity = new AntsEntity(id, repository.getAntsList().get(serialNumber).get(i));
//                AntsInsertAsyncTask antsInsertAsyncTask = new AntsInsertAsyncTask();
//                antsInsertAsyncTask.execute(antsEntity);
                //Log.d("bookmark", "id" + id);
                BookmarkDB.getInstance(imgViewBookmark.getContext())
                        .antsDao()
                        .insert(antsEntity);
            }

            for (int i = 0; i < repository.getContextList().get(serialNumber).size(); i++) {
                ContextsEntity contextsEntity = new ContextsEntity(id, repository.getContextList().get(serialNumber).get(i));
//                ContextsInsertAsyncTask contextsInsertAsyncTask = new ContextsInsertAsyncTask();
//                contextsInsertAsyncTask.execute(contextsEntity);
                BookmarkDB.getInstance(imgViewBookmark.getContext())
                        .contextsDao()
                        .insert(contextsEntity);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);


        }
    }

    class SynsInsertAsyncTask extends AsyncTask<SynsEntity, Void, Void> {
        @Override
        protected Void doInBackground(SynsEntity... synsEntities) {
            BookmarkDB.getInstance(imgViewBookmark.getContext())
                    .synsDao()
                    .insert(synsEntities[0]);
            return null;
        }
    }

    class AntsInsertAsyncTask extends AsyncTask<AntsEntity, Void, Void> {
        @Override
        protected Void doInBackground(AntsEntity... antsEntities) {
            BookmarkDB.getInstance(imgViewBookmark.getContext())
                    .antsDao()
                    .insert(antsEntities[0]);
            return null;
        }
    }

    class ContextsInsertAsyncTask extends AsyncTask<ContextsEntity, Void, Void> {
        @Override
        protected Void doInBackground(ContextsEntity... contextsEntities) {
            BookmarkDB.getInstance(imgViewBookmark.getContext())
                    .contextsDao()
                    .insert(contextsEntities[0]);
            return null;
        }
    }

    class CheckAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            if (BookmarkDB.getInstance(imgViewBookmark.getContext()).wordDao().checkIfLangExists(tvOrigWord, langTo).size() > 0) {

                    imgViewBookmark.setImageDrawable(imgViewBookmark.getResources().getDrawable(android.R.drawable.star_big_on));
                    checked = true;

            } else {
                imgViewBookmark.setImageDrawable(imgViewBookmark.getResources().getDrawable(android.R.drawable.star_big_off));
                checked = false;
            }
            //Log.i("bookmark", "checked" + tvOrigWord + checked);
            return null;
        }
    }
}
