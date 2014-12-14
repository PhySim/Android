package com.aspirephile.physim.scenes.db;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.TextView;

import com.aspirephile.physim.R;

public class ScenesCursorAdapter extends CursorAdapter {

	private LayoutInflater mInflater;

	public ScenesCursorAdapter(Context context, Cursor c, int flags) {
		super(context, c, flags);
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		// TODO Add selective layouts
		/*
		 * if (cursor.getPosition() % 2 == 1) {
		 * view.setBackgroundColor(context.getResources().getColor(
		 * R.color.background_odd)); } else {
		 * view.setBackgroundColor(context.getResources().getColor(
		 * R.color.background_even)); }
		 */
		TextView name = (TextView) view.findViewById(R.id.tv_scene_list_name);
		CheckedTextView bounded = (CheckedTextView) view
				.findViewById(R.id.ctt_scene_list_bounded);
		TextView feild1 = (TextView) view.findViewById(R.id.tv_scene_list_name);
		TextView feild2 = (TextView) view.findViewById(R.id.tv_scene_list_name);

		name.setText(cursor.getString(cursor
				.getColumnIndex(ScenesDB.tables.scenes.column.NAME)));

	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		return mInflater.inflate(R.layout.scenes_list_item, parent, false);
	}

}
