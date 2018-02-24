package com.xiaomai.geek.db;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.xiaomai.geek.db.ArticleRecord;
import com.xiaomai.geek.db.Task;

import com.xiaomai.geek.db.ArticleRecordDao;
import com.xiaomai.geek.db.TaskDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig articleRecordDaoConfig;
    private final DaoConfig taskDaoConfig;

    private final ArticleRecordDao articleRecordDao;
    private final TaskDao taskDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        articleRecordDaoConfig = daoConfigMap.get(ArticleRecordDao.class).clone();
        articleRecordDaoConfig.initIdentityScope(type);

        taskDaoConfig = daoConfigMap.get(TaskDao.class).clone();
        taskDaoConfig.initIdentityScope(type);

        articleRecordDao = new ArticleRecordDao(articleRecordDaoConfig, this);
        taskDao = new TaskDao(taskDaoConfig, this);

        registerDao(ArticleRecord.class, articleRecordDao);
        registerDao(Task.class, taskDao);
    }
    
    public void clear() {
        articleRecordDaoConfig.clearIdentityScope();
        taskDaoConfig.clearIdentityScope();
    }

    public ArticleRecordDao getArticleRecordDao() {
        return articleRecordDao;
    }

    public TaskDao getTaskDao() {
        return taskDao;
    }

}
