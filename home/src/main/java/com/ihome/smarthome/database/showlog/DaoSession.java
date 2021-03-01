package com.ihome.smarthome.database.showlog;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.ihome.smarthome.database.showlog.ShowLogEntity;

import com.ihome.smarthome.database.showlog.ShowLogEntityDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig showLogEntityDaoConfig;

    private final ShowLogEntityDao showLogEntityDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        showLogEntityDaoConfig = daoConfigMap.get(ShowLogEntityDao.class).clone();
        showLogEntityDaoConfig.initIdentityScope(type);

        showLogEntityDao = new ShowLogEntityDao(showLogEntityDaoConfig, this);

        registerDao(ShowLogEntity.class, showLogEntityDao);
    }
    
    public void clear() {
        showLogEntityDaoConfig.clearIdentityScope();
    }

    public ShowLogEntityDao getShowLogEntityDao() {
        return showLogEntityDao;
    }

}