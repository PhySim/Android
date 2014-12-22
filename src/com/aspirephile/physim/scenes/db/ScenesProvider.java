package com.aspirephile.physim.scenes.db;

import java.sql.SQLException;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import com.aspirephile.physim.core.Scene;
import com.aspirephile.shared.debug.Logger;
import com.aspirephile.shared.debug.NullPointerAsserter;
import com.aspirephile.shared.utils.StringManipulator;

public class ScenesProvider extends ContentProvider {
	private Logger l = new Logger(ScenesProvider.class);
	private NullPointerAsserter asserter = new NullPointerAsserter(
			ScenesProvider.class);
	private StringManipulator stringManip = new StringManipulator(asserter);

	public static final String AUTHORITY = "com.aspirephile.physim.scenes";

	public static final class paths {
		public static final String scenes = "scenes";
	}

	/**
	 * A uri to do operations on contacts table. A content provider is
	 * identified by its uri
	 */
	public static final Uri CONTENT_URI = Uri
			.parse(ContentResolver.SCHEME_CONTENT + "://" + AUTHORITY + "/"
					+ paths.scenes);

	/** Constants to identify the requested operation */
	private static final int SCENE_ID = 1;
	private static final int SCENES = 2;
	private static final int SCENE_NAMES = 3;

	private static final UriMatcher uriMatcher;

	static {
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(AUTHORITY, paths.scenes, SCENES);
		uriMatcher.addURI(AUTHORITY, paths.scenes + "/"
				+ ScenesDBProps.v2.tables.scenes.column.NAME, SCENE_NAMES);
		uriMatcher.addURI(AUTHORITY, paths.scenes + "/#", SCENE_ID);
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
				+ stringManip.getFormatedStringArrayQuietly(selectionArgs));
		int cnt = 0;
		if (uriMatcher.match(uri) == SCENE_ID) {
			String contactID = uri.getPathSegments().get(1);
			cnt = scenesDBHandler.delete(contactID);
			l.d(cnt + " rows affected by deletion");
			l.d("Notifying content resolver of change with URI: " + CONTENT_URI);
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
		Uri _uri = null;
		if (uri.equals(CONTENT_URI)) {
			Scene scene = new Scene(values);
			long rowID = scenesDBHandler.insert(scene);
			l.d("Row inserted with rowID: " + rowID);
			if (rowID > 0) {
				_uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
			} else {
				try {
					throw new SQLException("Failed to insert : " + uri);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			l.d("Notifying content resolver of change with URI: " + CONTENT_URI);
			getContext().getContentResolver().notifyChange(CONTENT_URI, null);
		}
		return _uri;

	}

	/** A callback method which is by the default content uri */
	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {

		l.d("ScenesProvider query being made with uri: " + uri
				+ ", projection: "
				+ stringManip.getFormatedStringArrayQuietly(projection)
				+ ", selection: " + selection + ", selectionArgs: "
				+ stringManip.getFormatedStringArrayQuietly(selectionArgs)
				+ ", sortOrder: " + sortOrder);
		Cursor result = null;
		if (uriMatcher.match(uri) == SCENES) {
			result = scenesDBHandler.fetchAllScenes();
			l.d("Fetching all scenes from ScenesDBProps returned cursor: "
					+ result);
		} else if (uriMatcher.match(uri) == SCENE_NAMES) {
			result = scenesDBHandler.fetchAllSceneNames();
			l.d("Fetching all scene names from ScenesDBProps returned cursor: "
					+ result);
		} else if (uriMatcher.match(uri) == SCENE_ID) {
			try {
				String sceneID = uri.getPathSegments().get(1);
				result = scenesDBHandler.getSceneByID(sceneID);
				l.d("Fetching scene with sceneID: " + sceneID
						+ " from ScenesDBProps returned cursor: " + result);
			} catch (IndexOutOfBoundsException e) {
				e.printStackTrace();
			}
		}
		if (asserter.assertPointer(result)) {
			l.d("Setting notification URI: " + CONTENT_URI);
			result.setNotificationUri(getContext().getContentResolver(), uri);
		}
		return result;
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
			l.d(cnt + " rows affected by updation");
			l.d("Setting notification URI: " + CONTENT_URI);
			getContext().getContentResolver().notifyChange(CONTENT_URI, null);
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
