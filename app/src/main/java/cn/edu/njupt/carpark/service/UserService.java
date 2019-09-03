package cn.edu.njupt.carpark.service;


import cn.edu.njupt.carpark.entity.CarDO;
import cn.edu.njupt.carpark.dao.UserDao;

public class UserService {
    private volatile static UserService instance;

    private UserService() {
    }

    public static UserService getInstance() {
        // check 1
        if (null == instance) {
            synchronized (UserDao.class) {
                // check 2
                if (null == instance) {
                    instance = new UserService();
                }
            }
        }
        return instance;
    }

    private static UserDao userDao = UserDao.getInstance();

    /**
     * @param number plate number
     */
    public CarDO getByNumber(String number) {
        return userDao.queryByNumber(number);
    }

    /**
     * 注册或更新用户信息。
     *
     * @param number      plate number
     * @param username    the specified username
     * @param isMonthRent 是否月租(1,0)
     * @return true or false
     */
    public boolean saveOrUpdate(String number, String username, boolean isMonthRent) {
        CarDO carDO = new CarDO();
        carDO.setNumber(number);
        carDO.setUsername(username);
        carDO.setMonthRent(isMonthRent);
        if (isMonthRent) {
            carDO.setMonthRentStartTime(System.currentTimeMillis());
        }
        return userDao.saveOrUpdate(number, carDO);
    }

    public int monthRentExpired(String number, String username) {
        CarDO carDO = new CarDO();
        carDO.setNumber(number);
        carDO.setUsername(username);
        carDO.setMonthRent(false);
        return userDao.updateMonthRentByNumber(number, carDO);
    }

    public boolean deleteByNumber(String number) {
        return userDao.deleteByNumber(number);
    }
}
