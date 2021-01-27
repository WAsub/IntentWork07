package sk2a_2190073.intentwork07;

import android.content.Context;

public class DragListener extends SortableListView.SimpleDragListener {
    private static  MainActivity.MEMOListdata[] PREFS;
    private static int mDraggingPosition;
    private static SortableListView mListView;
    private static Context con;
    private static SortableListView _lvMain;

    public DragListener(int mDraggingPosition, SortableListView mListView, MainActivity.MEMOListdata[] PREFS, Context con, SortableListView lv){
        this.PREFS = PREFS;
        this.mDraggingPosition = mDraggingPosition;
        this.mListView = mListView;
        this.con = con;
        this._lvMain = lv;
    }
    @Override
    public int onStartDrag(int position) {
        mDraggingPosition = position;
        mListView.invalidateViews();
        return position;
    }

    @Override
    public int onDuringDrag(int positionFrom, int positionTo) {
//        Toast.makeText( con , positionFrom+"トーストメッセージ"+positionTo, Toast.LENGTH_SHORT).show();
        if (positionFrom < 0 || positionTo < 0
                || positionFrom == positionTo) {
            return positionFrom;
        }
        int i;
        if (positionFrom < positionTo) {
            final int min = positionFrom;
            final int max = positionTo;
            final MainActivity.MEMOListdata data = PREFS[min];
            i = min;
            while (i < max) {
                PREFS[i] = PREFS[++i];
            }
            PREFS[max] = data;
        } else if (positionFrom > positionTo) {
            final int min = positionTo;
            final int max = positionFrom;
            final MainActivity.MEMOListdata data = PREFS[max];
            i = max;
            while (i > min) {
                PREFS[i] = PREFS[--i];
            }
            PREFS[min] = data;
        }
        mDraggingPosition = positionTo;
        mListView.invalidateViews();
        return positionTo;
    }


    @Override
    public boolean onStopDrag(int positionFrom, int positionTo) {
        mDraggingPosition = -1;
        mListView.invalidateViews();
        SQLiteProcess sqlite = new SQLiteProcess(con);
        sqlite.sortMemoOrder(PREFS, _lvMain, mDraggingPosition, con);
        return super.onStopDrag(positionFrom, positionTo);
    }
}