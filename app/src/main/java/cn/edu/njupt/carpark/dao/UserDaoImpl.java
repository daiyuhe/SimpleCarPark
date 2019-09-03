package cn.edu.njupt.carpark.dao;


import org.litepal.LitePal;

import cn.edu.njupt.carpark.bean.CarDO;


public class UserDaoImpl {
    private volatile static UserDaoImpl instance;

    private UserDaoImpl() {
    }

    public static UserDaoImpl getInstance() {
        // check 1
        if (null == instance) {
            synchronized (UserDaoImpl.class) {
                // check 2
                if (null == instance) {
                    instance = new UserDaoImpl();
                }
            }
        }
        return instance;
    }

    public boolean save(CarDO carDO) {
        return carDO.save();
    }

    public CarDO queryByNumber(String number) {
        return LitePal.where("number = ?", number).findFirst(CarDO.class);

    }

    public boolean saveOrUpdate(String number, CarDO carDO) {
        return carDO.saveOrUpdate("number = ?", number);
    }

    public int updateMonthRentByNumber(String number, CarDO carDO) {
        return carDO.updateAll("number = ?", number);
    }

    public boolean deleteByNumber(String number) {
        int i = LitePal.deleteAll(CarDO.class, "number = ?", number);
        return i > 0;
    }
}