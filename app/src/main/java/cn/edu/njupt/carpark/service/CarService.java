package cn.edu.njupt.carpark.service;


import cn.edu.njupt.carpark.entity.CarDO;
import cn.edu.njupt.carpark.dao.CarDao;

public class CarService {
    private volatile static CarService instance;

    private CarService() {
    }

    public static CarService getInstance() {
        // check 1
        if (null == instance) {
            synchronized (CarDao.class) {
                // check 2
                if (null == instance) {
                    instance = new CarService();
                }
            }
        }
        return instance;
    }

    private static CarDao carDao = CarDao.getInstance();

    /**
     * @param number plate number
     */
    public CarDO getByNumber(String number) {
        return carDao.queryByNumber(number);
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
        return carDao.saveOrUpdate(number, carDO);
    }

    public int monthRentExpired(String number, String username) {
        CarDO carDO = new CarDO();
        carDO.setNumber(number);
        carDO.setUsername(username);
        carDO.setMonthRent(false);
        return carDao.updateMonthRentByNumber(number, carDO);
    }

    public boolean deleteByNumber(String number) {
        return carDao.deleteByNumber(number);
    }
}
