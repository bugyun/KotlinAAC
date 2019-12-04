package vip.ruoyun.baselib;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

public class RoomTest {


    private Migration migration_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("");
        }
    };

    public void test(Context context) {
        AppDatabase db = Room.databaseBuilder(context.getApplicationContext(),
                AppDatabase.class, "database-name")
//                .setTransactionExecutor()
                .enableMultiInstanceInvalidation()//启用多实例失效
                .addMigrations(migration_1_2)//升级数据库
                .addCallback(new RoomDatabase.Callback() {
                    //第一次创建数据库时调用。在创建所有表之后调用这个函数。
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                    }

                    //在数据库打开时调用。
                    @Override
                    public void onOpen(@NonNull SupportSQLiteDatabase db) {
                    }

                    //在数据库被破坏性迁移后调用
                    @Override
                    public void onDestructiveMigration(@NonNull SupportSQLiteDatabase db) {
                    }
                })
                .build();
        db.userDao().insertAll();


        //
        AppDatabase.getUserDao(context);


    }

}
