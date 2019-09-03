package cn.edu.njupt.carpark.service;

import java.util.HashSet;
import java.util.Set;

import cn.edu.njupt.carpark.bean.CarParkDO;
import cn.edu.njupt.carpark.dao.GarageRelationDao;


// CarParkDO service
public class GarageRelationService {

    private static GarageRelationDao garageRelationDao = new GarageRelationDao();

    /**
     * 获取所有在使用的车库号
     * @return
     */
    public Set<Integer> listGarageId(){
        return new HashSet<Integer>(garageRelationDao.listGarageId());
    }

    /**
     * 根据number、isRent、garageNumber 添加GarageRelation对象
     * @param number
     * @param isRent
     * @param garageNumber
     * @return
     */
    public boolean saveGarageRelation(String number , boolean isRent , int garageNumber){
        CarParkDO carParkDO = new CarParkDO();
        carParkDO.setNumber(number);
        carParkDO.setMonthRent(isRent);
        carParkDO.setGarageNumber(garageNumber);
        carParkDO.setEnterTime(System.currentTimeMillis() / 1000); //秒级别的
        return garageRelationDao.saveGarageRelation(carParkDO);
    }


    /**
     * 根据number 删除GarageRelation对象
     * @param number
     * @return
     */
    public int deleteGarageRelation(String number){
        return garageRelationDao.deleteGarageRelation(number);
    }


    /**
     * 根据number 获取GarageRelation对象
     * @param number
     * @return
     */
    public CarParkDO getGarageRelation(String number){
        return garageRelationDao.getGarageRelation(number);
    }
}
