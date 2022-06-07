package kz.almas.comixread.database;

import androidx.room.ColumnInfo;
import androidx.room.Dao;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class UserInfo {

    @PrimaryKey
    public long id;

    @ColumnInfo
    public String key;

    @ColumnInfo
    public String avatarBase64;

    @ColumnInfo
    public String username;

    @ColumnInfo
    public String password;

}
