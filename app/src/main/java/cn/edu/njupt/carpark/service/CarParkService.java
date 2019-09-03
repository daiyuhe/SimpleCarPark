package cn.edu.njupt.carpark.service;

import java.util.HashSet;
import java.util.Set;

import cn.edu.njupt.carpark.entity.CarParkDO;
import cn.edu.njupt.carpark.dao.CarParkDao;
import cn.edu.njupt.carpark.dao.CarDao;


// CarParkDO service
public class CarParkService {
    private volatile static CarParkService instance;

    private CarParkService() {
    }

    public static CarParkService getInstance() {
        // check 1
        if (null == instance) {
            synchronized (CarDao.class) {
                // check 2
                if (null == instance) {
                    instance = new CarParkService();
                }
            }
        }
        return instance;
    }

    private CarParkDao carParkDao = CarParkDao.getInstance();

    /**
     * 获取所有在使用的车库号
     * @return
     */
    public Set<Integer> listAllParkNumber(){
        return new HashSet<Integer>(carParkDao.listAllParkNumber());
    }

    /**
     * 根据number、isRent、garageNumber 添加CarParkDO对象
     * @param number
     * @param isRent
     * @param garageNumber
     * @return
     */
    public boolean saveCarParkDO(String number , boolean isRent , int garageNumber){
        CarParkDO carParkDO = new CarParkDO();
        carParkDO.setNumber(number);
        carParkDO.setMonthRent(isRent);
        carParkDO.setGarageNumber(garageNumber);
        carParkDO.setEnterTime(System.currentTimeMillis() / 1000); //秒级别的
        return carParkDao.saveCarParkDO(carParkDO);
    }


    /**
     * 根据number 删除GarParkDO对象
     * @param number
     * @return
     */
    public int deleteCarParkDOByNumber(String number){
        return carParkDao.deleteCarParkDOByNumber(number);
    }


    /**
     * 根据number 获取GarParkDO对象
     * @param number
     * @return
     */
    public CarParkDO getGarParkDOByNumber(String number){
        return carParkDao.getCarParkDOByNumber(number);
    }
}
