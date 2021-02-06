package com.ihome.smarthome.database.showlog;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;

public class DbController {
    /**
     * Helper
     */
    private DaoMaster.DevOpenHelper mHelper;//获取Helper对象
    /**
     * 数据库
     */
    private SQLiteDatabase db;
    /**
     * DaoMaster
     */
    private DaoMaster mDaoMaster;
    /**
     * DaoSession
     */
    private DaoSession mDaoSession;
    /**
     * 上下文
     */
    private Context context;
    /**
     * dao
     */
    private ShowLogEntityDao showLogEntityDao;

    private static DbController mDbController;

    /**
     * 获取单例
     */
    public static DbController getInstance(Context context){
        if(mDbController == null){
            synchronized (DbController.class){
                if(mDbController == null){
                    mDbController = new DbController(context);
                }
            }
        }
        return mDbController;
    }
    /**
     * 初始化
     * @param context
     */
    public DbController(Context context) {
        this.context = context;
        mHelper = new DaoMaster.DevOpenHelper(context,"show_log.db", null);
        mDaoMaster =new DaoMaster(getWritableDatabase());
        mDaoSession = mDaoMaster.newSession();
        showLogEntityDao = mDaoSession.getShowLogEntityDao();

    }
    /**
     * 获取可读数据库
     */
    private SQLiteDatabase getReadableDatabase(){
        if(mHelper == null){
            mHelper = new DaoMaster.DevOpenHelper(context,"show_log.db",null);
        }
        SQLiteDatabase db =mHelper.getReadableDatabase();
        return db;
    }

    /**
     * 获取可写数据库
     * @return
     */
    private SQLiteDatabase getWritableDatabase(){
        if(mHelper == null){
            mHelper =new DaoMaster.DevOpenHelper(context,"show_log.db",null);
        }
        SQLiteDatabase db = mHelper.getWritableDatabase();
        return db;
    }

    /**
     * 会自动判定是插入还是替换
     * @param personInfor
     */
    public void insertOrReplace(ShowLogEntity personInfor){
        showLogEntityDao.insertOrReplace(personInfor);
    }
    /**插入一条记录，表里面要没有与之相同的记录
     *
     * @param personInfor
     */
    public long insert(ShowLogEntity personInfor){
        return  showLogEntityDao.insert(personInfor);
    }

    /**
     * 更新数据
     * @param personInfor
     */
    public void update(ShowLogEntity personInfor){

    }
    /**
     * 按条件查询数据
     */
    public List<ShowLogEntity> searchByWhere(Integer wherecluse){

        List<ShowLogEntity>personInfors =  showLogEntityDao.queryBuilder().where(ShowLogEntityDao.Properties.Type.eq(wherecluse)).build().list();


        return personInfors;
    }
    /**
     * 查询所有数据
     */
    public List<ShowLogEntity> searchAll(){
        List<ShowLogEntity>personInfors= showLogEntityDao.queryBuilder().list();
        return personInfors;
    }
    /**
     * 删除数据
     */
    public void delete(String wherecluse){
        showLogEntityDao.queryBuilder().where(ShowLogEntityDao.Properties.Type.eq(wherecluse)).buildDelete().executeDeleteWithoutDetachingEntities();
    }

    public void deleteAll(){
        showLogEntityDao.queryBuilder().buildDelete().executeDeleteWithoutDetachingEntities();
    }
}