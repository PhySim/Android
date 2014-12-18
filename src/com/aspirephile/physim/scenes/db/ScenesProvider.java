package com.aspirephile.physim.scenes.db;

import java.sql.SQLException;

import com.aspirephile.shared.debug.Logger;
import com.aspirephile.shared.debug.NullPointerAsserter;
import com.aspirephile.shared.utils.StringManipulator;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

public class ScenesProvider extends ContentProvider {
	private Logger l = new Logger(ScenesProvider.class);
	private NullPointerAsserter asserter = new NullPointerAsserter(
			ScenesProvider.class);
	private StringManipulator stringManip = new StringManipulator(asserter);

	public static final String PROVIDER_NAME = "com.aspirephile.physim.scenes";

	/**
	 * A uri to do operations on contacts table. A content provider is
	 * identified by its uri
	 */
	public static final Uri CONTENT_URI = Uri.parse("content://"
			+ PROVIDER_NAME + "/scenes");

	/** Constants to identify the requested operation */
	private static final int SCENE_ID = 1;
	private static final int SCENES = 2;
	private static final int SCENE_NAMES = 3;

	private static final UriMatcher uriMatcher;

	static {
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(PROVIDER_NAME, "scenes", SCENES);
		uriMatcher.addURI(PROVIDER_NAME, "scenes/#", SCENE_ID);
	}

	/** This content provider does the database operations by this object */
	ScenesDBHandler scenesDBHandler;

	/**
	 * A callback method which is invoked when delete operation is requested on
	 * this content provider
	 */
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO allow deletion of scenes

		l.d("ScenesProvider recruited to delete with URI: " + uri
				+ ", selection: " + selection + ", selectionArgs: "
				+ stringManip.getFormatedStringArray(selectionArgs));
		int cnt = 0;
		if (uriMatcher.match(uri) == SCENE_ID) {
			String contactID = uri.getPathSegments().get(1);
			cnt = scenesDBHandler.delete(contactID);
			getContext().getContentResolver().notifyChange(CONTENT_URI, null);
		}
		return cnt;
	}

	@Override
	public String getType(Uri uri) {
		return null;
	}

	/**
	 * A callback method which is invoked when insert operation is requested on
	 * this content provider
	 */
	@Override
	public Uri insert(Uri uri, ContentValues values) {
		long rowID = scenesDBHandler.insert(values);
		Uri _uri = null;
		if (rowID > 0) {
			_uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
		} else {
			try {
				throw new SQLException("Failed to insert : " + uri);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return _uri;

	}

	/** A callback method which is by the default content uri */
	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {

		l.d("ScenesProvider query made with uri: " + uri + ", projection");
		if (uriMatcher.match(uri) == SCENES) {
			return scenesDBHandler.fetchAllSceneNames();
		} else if (uriMatcher.match(uri) == SCENE_NAMES) {
			return scenesDBHandler.fetchAllSceneNames();
		} else if (uriMatcher.match(uri) == SCENE_ID) {
			try {
				String sceneID = uri.getPathSegments().get(1);
				Cursor result = scenesDBHandler.getSceneByID(sceneID);
				result.setNotificationUri(getContext().getContentResolver(),
						CONTENT_URI);
			} catch (IndexOutOfBoundsException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * A callback method which is invoked when update operation is requested on
	 * this content provider
	 */
	@Override
	public int update(Uri uri, ContentValues contentValues, String selection,
			String[] selectionArgs) {
		int cnt = 0;
		if (uriMatcher.match(uri) == SCENE_ID) {
			String contactID = uri.getPathSegments().get(1);
			cnt = scenesDBHandler.update(contentValues, contactID);

		}
		return cnt;
	}

	@Override
	public boolean onCreate() {
		scenesDBHandler = new ScenesDBHandler(getContext());
		scenesDBHandler.open();
		return true;
	}

}
