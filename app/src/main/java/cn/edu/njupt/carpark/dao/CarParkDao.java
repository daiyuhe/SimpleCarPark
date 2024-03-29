package cn.edu.njupt.carpark.dao;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import cn.edu.njupt.carpark.entity.CarParkDO;


public class CarParkDao {
    private volatile static CarParkDao instance;

    private CarParkDao() {
    }

    public static CarParkDao getInstance() {
        // check 1
        if (null == instance) {
            synchronized (CarDao.class) {
                // check 2
                if (null == instance) {
                    instance = new CarParkDao();
                }
            }
        }
        return instance;
    }

    /**
     * 获取所有的车库号
     * @return
     */
    public List<Integer> listAllParkNumber(){
        List<Integer> res = new ArrayList<>();
        List<CarParkDO> list = LitePal.select("garageNumber").find(CarParkDO.class);
        for(int i = 0 ; i < list.size() ; i++){
            res.add(list.get(i).getGarageNumber());
        }
        return res;
    }


    /**
     * 添加garageRelation 对象
     * @param carParkDO
     * @return
     */
    public boolean saveCarParkDO(CarParkDO carParkDO){
        return carParkDO.save();
    }

    /**
     * 根据车牌号出库
     * @param number
     * @return
     */
    public int deleteCarParkDOByNumber(String number){
        return LitePal.deleteAll(CarParkDO.class , "number = ?" , number);
    }


    /**
     * 根据cardId 获取garageRelation 对象
     * @param number
     * @return
     */
    public CarParkDO getCarParkDOByNumber(String number){
        List<CarParkDO> res = LitePal.where("number = ?" , number).find(CarParkDO.class);
        return res.size() == 0 ? null : res.get(0);
    }

}
