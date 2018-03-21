package com.mvp.rui.android_greendao_demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.mvp.rui.android_greendao_demo.database.DataBaseManager;
import com.mvp.rui.android_greendao_demo.table_model.DaoSession;
import com.mvp.rui.android_greendao_demo.table_model.UserModel;
import com.mvp.rui.android_greendao_demo.table_model.UserModelDao;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * 一个结合RxJava1.x与greendao的框架项目
 */
public class MainActivity extends AppCompatActivity {

    private DaoSession daoSession;
    private UserModelDao userModelDao;
    private TextView tvQueryContent;
    /**
     * 第二次数据不再添加
     */
    private boolean isAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        daoSession = DataBaseManager.getDaoSession();
        userModelDao = daoSession.getUserModelDao();
        tvQueryContent = findViewById(R.id.tv_query_content);

        findViewById(R.id.tv_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isAdd) {
//                addSingle();
                    addList();
                }

            }
        });

        findViewById(R.id.tv_query).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                queryAll();
//                queryByCondition(30, "female");
            }
        });

        findViewById(R.id.tv_update).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update(30, "female");
            }
        });

        findViewById(R.id.tv_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delete(30, "female");
            }
        });
    }

    /**
     * 添加单个数据
     */
    private void addSingle() {
        UserModel model = new UserModel();
        model.setName("Tom");
        model.setAge(15);
        model.setGender("male");
        userModelDao.rx().insert(model)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<UserModel>() {
                    @Override
                    public void call(UserModel userModel) {
                        Toast.makeText(MainActivity.this, "数据插入成功", Toast.LENGTH_SHORT).show();
                    }
                })
        ;
    }

    /**
     * 添加多个数据
     */
    private void addList() {
        List<UserModel> list = new ArrayList<>();

        UserModel model = new UserModel();
        model.setName("Jay");
        model.setAge(20);
        model.setGender("female");
        list.add(model);

        UserModel model1 = new UserModel();
        model1.setName("Bruno");
        model1.setAge(22);
        model1.setGender("male");
        list.add(model1);

        UserModel model2 = new UserModel();
        model2.setName("Jack");
        model2.setAge(25);
        model2.setGender("male");
        list.add(model2);


        UserModel model3 = new UserModel();
        model3.setName("Apple");
        model3.setAge(31);
        model3.setGender("female");
        list.add(model3);

        userModelDao.rx()//让数据插入在子线程中执行，防止阻塞主线程
                .insertInTx(list)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(userModels -> {
                    isAdd = true;
                    Toast.makeText(MainActivity.this, "数据插入成功", Toast.LENGTH_SHORT).show();
                });

    }

    /**
     * 查询全部
     */
    private void queryAll() {
        tvQueryContent.setText("");
        userModelDao.detachAll();//多次查询，需要清除缓存，这个是清除userModelDao缓存
        Toast.makeText(MainActivity.this, "数据查询进入", Toast.LENGTH_SHORT).show();
        QueryBuilder<UserModel> qb = userModelDao.queryBuilder();
        qb.rx().list().flatMap(userModels -> Observable.from(userModels))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(model -> {
                    tvQueryContent.append(model.toString());
                    tvQueryContent.append("\n");
                });
    }

    /**
     * 按条件查询
     *
     * @param age
     * @param gender
     */
    private void queryByCondition(int age, String gender) {
        tvQueryContent.setText("");

        Toast.makeText(MainActivity.this, "数据查询进入", Toast.LENGTH_SHORT).show();
        getqueryOB(age, gender)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(model -> {
                    tvQueryContent.append(model.toString());
                    tvQueryContent.append("\n");
                });
    }

    /**
     * 这里的更新,是先查询到原来的对象然后才去更新的
     */
    private void update(int age, String gender) {

        getqueryOB(age, gender)
                .map(model -> {
                    model.setAge(40);//将符合条件的数据年龄更新设置为40
                    return model;
                })
                .toList()
                .flatMap(userModels -> {
                    //这种方式，会将数据库没有的数据插入，而已有数据将本更新，这个是通过id主键进行判断的
                    return userModelDao.rx().insertOrReplaceInTx(userModels);
                })
//                .flatMap(new Func1<List<UserModel>, Observable<Iterable<UserModel>>>() {
//                    @Override
//                    public Observable<Iterable<UserModel>> call(List<UserModel> userModels) {
                //这种方式，只能更新数据库存在的对象
//                        return userModelDao.rx().updateInTx(userModels);
//                    }
//                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(userModels ->
                        Toast.makeText(MainActivity.this
                                , "数据跟新成功", Toast.LENGTH_SHORT).show())
        ;

    }

    /**
     * 删除，先查询然后删除，
     * 或则知道主键id直接删除 userModelDao.deleteByKeyInTx(key);
     *
     * @param age
     * @param gender
     */
    private void delete(int age, String gender) {
        getqueryOB(age, gender)
                .toList()//这个没有必要
                .flatMap(userModels -> {
                    //
                    return userModelDao.rx().deleteInTx(userModels);
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aVoid ->
                        Toast.makeText(MainActivity.this
                                , "删除成功", Toast.LENGTH_SHORT).show());
    }

    /**
     * 查询公共的代码
     *
     * @param age
     * @param gender
     * @return
     */
    private Observable<UserModel> getqueryOB(int age, String gender) {
        daoSession.clear();//多次查询，需要清除缓存，这个是清除全部缓存
        //userModelDao.detachAll();//多次查询，需要清除缓存，这个是清除userModelDao缓存
        QueryBuilder<UserModel> qb = userModelDao.queryBuilder();

//        qb.where(UserModelDao.Properties.Id.eq(id));
        qb.where(qb.and(UserModelDao.Properties.Age.gt(age)
                , UserModelDao.Properties.Gender.eq(gender))
        );

        return qb.rx().list().flatMap(userModels -> Observable.from(userModels));
    }
}
