package com.acrutchfield.natashashairstudio.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;

import com.acrutchfield.natashashairstudio.R;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

// https://medium.com/@kitek/recyclerview-swipe-to-delete-easier-than-you-thought-cff67ff5e5f6
public class DeleteItemCallback extends ItemTouchHelper.SimpleCallback {

    private static final String COLOR_RED = "#f44336";
    private final Context context;
    private final DeletePromptInterface promptInterface;

    public interface DeletePromptInterface {
        void promptForDelete(String uid, String id, int position);
    }

    public DeleteItemCallback(Context context, DeletePromptInterface promptInterface, int dragDirs, int swipeDirs){
        super(dragDirs, swipeDirs);
        this.context = context;
        this.promptInterface = promptInterface;
    }
    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView,
                            @NonNull RecyclerView.ViewHolder viewHolder,
                            float dX, float dY, int actionState, boolean isCurrentlyActive) {

        Drawable deleteIcon = ContextCompat.getDrawable(Objects.requireNonNull(context),
                R.drawable.ic_delete_black_24dp);

        assert deleteIcon != null;
        int intrinsicWidth = deleteIcon.getIntrinsicWidth();
        int intrinsicHeight = deleteIcon.getIntrinsicHeight();

        View itemView = viewHolder.itemView;
        int bottom = itemView.getBottom();
        int top = itemView.getTop();
        int right = itemView.getRight();
        float left = right + dX;
        int itemHeight = bottom - top;
        boolean isCanceled = dX == 0f && !isCurrentlyActive;
        ColorDrawable background = new ColorDrawable();
        int backgroundColor = Color.parseColor(COLOR_RED);

        int deleteIconTop = top + (itemHeight - intrinsicHeight) / 2;
        int deleteIconMargin = (itemHeight - intrinsicHeight) / 2;
        int deleteIconLeft = right - deleteIconMargin - intrinsicWidth;
        int deleteIconRight = right - deleteIconMargin;
        int deleteIconBottom = deleteIconTop + intrinsicHeight;


        if(isCanceled) {
            clearCanvas(c, left, top,
                    right, bottom);
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }

        background.setColor(backgroundColor);
        background.setBounds(right + Float.floatToIntBits(dX),
                top, right, bottom);
        background.draw(c);

        deleteIcon.setBounds(deleteIconLeft, deleteIconTop, deleteIconRight, deleteIconBottom);
        deleteIcon.draw(c);

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        String uid = (String)viewHolder.itemView.getTag(R.string.uid);
        String id = (String)viewHolder.itemView.getTag(R.string.id);
        promptInterface.promptForDelete(uid, id, viewHolder.getAdapterPosition());
        Log.d("ReviewFragment", "onSwiped.id=" + id);
        Log.d("ReviewFragment", "onSwiped.uid=" + uid);
    }

    private void clearCanvas(Canvas c, float left, float top, float right, float bottom) {
        Paint clearPaint = new Paint();
        clearPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        c.drawRect(left, top, right, bottom, clearPaint);
    }
}
