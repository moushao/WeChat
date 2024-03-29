package com.tw.wechat.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.tw.wechat.entity.User;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "USER".
*/
public class UserDao extends AbstractDao<User, Long> {

    public static final String TABLENAME = "USER";

    /**
     * Properties of entity User.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "Id", true, "_id");
        public final static Property Username = new Property(1, String.class, "username", false, "USERNAME");
        public final static Property Nick = new Property(2, String.class, "nick", false, "NICK");
        public final static Property Avatar = new Property(3, String.class, "avatar", false, "AVATAR");
        public final static Property ProfileImage = new Property(4, String.class, "profileImage", false, "PROFILE_IMAGE");
        public final static Property Type = new Property(5, int.class, "type", false, "TYPE");
    }


    public UserDao(DaoConfig config) {
        super(config);
    }
    
    public UserDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"USER\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: Id
                "\"USERNAME\" TEXT," + // 1: username
                "\"NICK\" TEXT," + // 2: nick
                "\"AVATAR\" TEXT," + // 3: avatar
                "\"PROFILE_IMAGE\" TEXT," + // 4: profileImage
                "\"TYPE\" INTEGER NOT NULL );"); // 5: type
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"USER\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, User entity) {
        stmt.clearBindings();
 
        Long Id = entity.getId();
        if (Id != null) {
            stmt.bindLong(1, Id);
        }
 
        String username = entity.getUsername();
        if (username != null) {
            stmt.bindString(2, username);
        }
 
        String nick = entity.getNick();
        if (nick != null) {
            stmt.bindString(3, nick);
        }
 
        String avatar = entity.getAvatar();
        if (avatar != null) {
            stmt.bindString(4, avatar);
        }
 
        String profileImage = entity.getProfileImage();
        if (profileImage != null) {
            stmt.bindString(5, profileImage);
        }
        stmt.bindLong(6, entity.getType());
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, User entity) {
        stmt.clearBindings();
 
        Long Id = entity.getId();
        if (Id != null) {
            stmt.bindLong(1, Id);
        }
 
        String username = entity.getUsername();
        if (username != null) {
            stmt.bindString(2, username);
        }
 
        String nick = entity.getNick();
        if (nick != null) {
            stmt.bindString(3, nick);
        }
 
        String avatar = entity.getAvatar();
        if (avatar != null) {
            stmt.bindString(4, avatar);
        }
 
        String profileImage = entity.getProfileImage();
        if (profileImage != null) {
            stmt.bindString(5, profileImage);
        }
        stmt.bindLong(6, entity.getType());
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public User readEntity(Cursor cursor, int offset) {
        User entity = new User( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // Id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // username
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // nick
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // avatar
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // profileImage
            cursor.getInt(offset + 5) // type
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, User entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setUsername(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setNick(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setAvatar(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setProfileImage(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setType(cursor.getInt(offset + 5));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(User entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(User entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(User entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
