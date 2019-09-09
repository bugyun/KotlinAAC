package vip.ruoyun.baselib;

import android.content.Context;

import androidx.room.Room;

public class RoomTest {


    public void test(Context context) {
        AppDatabase db = Room.databaseBuilder(context.getApplicationContext(),
                AppDatabase.class, "database-name")
                .enableMultiInstanceInvalidation()//启用多实例失效
                .build();
        db.userDao().insertAll();

    }

}
