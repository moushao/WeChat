package com.tw.wechat.dao;

import java.util.List;
import java.util.ArrayList;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.SqlUtils;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;
import org.greenrobot.greendao.query.Query;
import org.greenrobot.greendao.query.QueryBuilder;

import com.tw.wechat.entity.User;

import com.tw.wechat.entity.Comment;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "COMMENT".
*/
public class CommentDao extends AbstractDao<Comment, Long> {

    public static final String TABLENAME = "COMMENT";

    /**
     * Properties of entity Comment.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property CommentID = new Property(0, Long.class, "commentID", true, "_id");
        public final static Property TweetId = new Property(1, Long.class, "tweetId", false, "TWEET_ID");
        public final static Property Content = new Property(2, String.class, "content", false, "CONTENT");
        public final static Property SenderID = new Property(3, Long.class, "senderID", false, "SENDER_ID");
    }

    private DaoSession daoSession;

    private Query<Comment> tweet_CommentsQuery;

    public CommentDao(DaoConfig config) {
        super(config);
    }
    
    public CommentDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"COMMENT\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: commentID
                "\"TWEET_ID\" INTEGER," + // 1: tweetId
                "\"CONTENT\" TEXT," + // 2: content
                "\"SENDER_ID\" INTEGER);"); // 3: senderID
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"COMMENT\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, Comment entity) {
        stmt.clearBindings();
 
        Long commentID = entity.getCommentID();
        if (commentID != null) {
            stmt.bindLong(1, commentID);
        }
 
        Long tweetId = entity.getTweetId();
        if (tweetId != null) {
            stmt.bindLong(2, tweetId);
        }
 
        String content = entity.getContent();
        if (content != null) {
            stmt.bindString(3, content);
        }
 
        Long senderID = entity.getSenderID();
        if (senderID != null) {
            stmt.bindLong(4, senderID);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, Comment entity) {
        stmt.clearBindings();
 
        Long commentID = entity.getCommentID();
        if (commentID != null) {
            stmt.bindLong(1, commentID);
        }
 
        Long tweetId = entity.getTweetId();
        if (tweetId != null) {
            stmt.bindLong(2, tweetId);
        }
 
        String content = entity.getContent();
        if (content != null) {
            stmt.bindString(3, content);
        }
 
        Long senderID = entity.getSenderID();
        if (senderID != null) {
            stmt.bindLong(4, senderID);
        }
    }

    @Override
    protected final void attachEntity(Comment entity) {
        super.attachEntity(entity);
        entity.__setDaoSession(daoSession);
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public Comment readEntity(Cursor cursor, int offset) {
        Comment entity = new Comment( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // commentID
            cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1), // tweetId
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // content
            cursor.isNull(offset + 3) ? null : cursor.getLong(offset + 3) // senderID
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, Comment entity, int offset) {
        entity.setCommentID(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setTweetId(cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1));
        entity.setContent(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setSenderID(cursor.isNull(offset + 3) ? null : cursor.getLong(offset + 3));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(Comment entity, long rowId) {
        entity.setCommentID(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(Comment entity) {
        if(entity != null) {
            return entity.getCommentID();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(Comment entity) {
        return entity.getCommentID() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
    /** Internal query to resolve the "comments" to-many relationship of Tweet. */
    public List<Comment> _queryTweet_Comments(Long tweetId) {
        synchronized (this) {
            if (tweet_CommentsQuery == null) {
                QueryBuilder<Comment> queryBuilder = queryBuilder();
                queryBuilder.where(Properties.TweetId.eq(null));
                tweet_CommentsQuery = queryBuilder.build();
            }
        }
        Query<Comment> query = tweet_CommentsQuery.forCurrentThread();
        query.setParameter(0, tweetId);
        return query.list();
    }

    private String selectDeep;

    protected String getSelectDeep() {
        if (selectDeep == null) {
            StringBuilder builder = new StringBuilder("SELECT ");
            SqlUtils.appendColumns(builder, "T", getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T0", daoSession.getUserDao().getAllColumns());
            builder.append(" FROM COMMENT T");
            builder.append(" LEFT JOIN USER T0 ON T.\"SENDER_ID\"=T0.\"_id\"");
            builder.append(' ');
            selectDeep = builder.toString();
        }
        return selectDeep;
    }
    
    protected Comment loadCurrentDeep(Cursor cursor, boolean lock) {
        Comment entity = loadCurrent(cursor, 0, lock);
        int offset = getAllColumns().length;

        User sender = loadCurrentOther(daoSession.getUserDao(), cursor, offset);
        entity.setSender(sender);

        return entity;    
    }

    public Comment loadDeep(Long key) {
        assertSinglePk();
        if (key == null) {
            return null;
        }

        StringBuilder builder = new StringBuilder(getSelectDeep());
        builder.append("WHERE ");
        SqlUtils.appendColumnsEqValue(builder, "T", getPkColumns());
        String sql = builder.toString();
        
        String[] keyArray = new String[] { key.toString() };
        Cursor cursor = db.rawQuery(sql, keyArray);
        
        try {
            boolean available = cursor.moveToFirst();
            if (!available) {
                return null;
            } else if (!cursor.isLast()) {
                throw new IllegalStateException("Expected unique result, but count was " + cursor.getCount());
            }
            return loadCurrentDeep(cursor, true);
        } finally {
            cursor.close();
        }
    }
    
    /** Reads all available rows from the given cursor and returns a list of new ImageTO objects. */
    public List<Comment> loadAllDeepFromCursor(Cursor cursor) {
        int count = cursor.getCount();
        List<Comment> list = new ArrayList<Comment>(count);
        
        if (cursor.moveToFirst()) {
            if (identityScope != null) {
                identityScope.lock();
                identityScope.reserveRoom(count);
            }
            try {
                do {
                    list.add(loadCurrentDeep(cursor, false));
                } while (cursor.moveToNext());
            } finally {
                if (identityScope != null) {
                    identityScope.unlock();
                }
            }
        }
        return list;
    }
    
    protected List<Comment> loadDeepAllAndCloseCursor(Cursor cursor) {
        try {
            return loadAllDeepFromCursor(cursor);
        } finally {
            cursor.close();
        }
    }
    

    /** A raw-style query where you can pass any WHERE clause and arguments. */
    public List<Comment> queryDeep(String where, String... selectionArg) {
        Cursor cursor = db.rawQuery(getSelectDeep() + where, selectionArg);
        return loadDeepAllAndCloseCursor(cursor);
    }
 
}
