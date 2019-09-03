package cn.edu.njupt.carpark.service;


import cn.edu.njupt.carpark.bean.CarDO;
import cn.edu.njupt.carpark.dao.UserDaoImpl;

public class UserService {

    private static UserDaoImpl userDao = UserDaoImpl.getInstance();

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
