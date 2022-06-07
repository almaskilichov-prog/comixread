package kz.almas.comixread.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface UserInfoDao {

    @Query("SELECT * FROM userinfo")
    List<UserInfo> getAll();

    @Query("SELECT * FROM userinfo WHERE id = :id")
    UserInfo getById(long id);

    @Query("UPDATE userinfo SET avatarBase64 = :newAvatarBase64 WHERE id IN (:idList)")
    int updateAvatarBase64ByIdList(List<Long> idList, byte[] newAvatarBase64);

    @Query("UPDATE userinfo SET username = :newUsername WHERE id IN (:idList)")
    int updateUsernameByIdList(List<Long> idList, String newUsername);

    @Insert
    void insert(UserInfo userInfo);

    @Update
    void update(UserInfo userInfo);

    @Delete
    void delete(UserInfo userInfo);
}
