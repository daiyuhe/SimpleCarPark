package cn.edu.njupt.carpark.service;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import cn.edu.njupt.carpark.dao.CarDao;

//分发车库号
public class ParkNumberService {
    private volatile static ParkNumberService instance;
    private static CarParkService carParkService = CarParkService.getInstance();
    //默认为50个车库号，从1 ~ 50
    private static final int garageIdSize = 50;
    //已经使用过的GarageId
    private static Set<Integer> usedParkNumber = null;
    //还未使用过的GarageId
    private static Set<Integer> unUsedParkNumber = null;

    private ParkNumberService() {
    }

    public static ParkNumberService getInstance() {
        // check 1
        if (null == instance) {
            synchronized (CarDao.class) {
                // check 2
                if (null == instance) {
                    instance = new ParkNumberService();
                }
            }
        }
        return instance;
    }


    /**
     * 初始化Set集合
     */
    private static void initSet() {
        usedParkNumber = carParkService.listAllParkNumber();
        Set<Integer> set2 = new HashSet<>();
        for (int i = 1; i <= garageIdSize; i++) {
            if (!usedParkNumber.contains(i)) {
                set2.add(i);
            }
        }
        unUsedParkNumber = set2;
    }

    /**
     * 入库，获取车牌号
     * @return
     */
    public int getParkNumber() {
        if (usedParkNumber == null || unUsedParkNumber == null) {
            initSet();
        }
        if (unUsedParkNumber.isEmpty()) {
            return -1;
        }
        Object[] objs = unUsedParkNumber.toArray();
        int ran = new Random().nextInt(objs.length);
        unUsedParkNumber.remove(objs[ran]);
        usedParkNumber.add((Integer) objs[ran]);
        return (Integer) objs[ran];
    }

    /**
     * 出库
     * @param parkNumber
     */
    public void outParkNumber(int parkNumber) {
        if (usedParkNumber == null || unUsedParkNumber == null) {
            initSet();
        }
        usedParkNumber.remove(parkNumber);
        unUsedParkNumber.add(parkNumber);
    }

    /**
     * 剩余车库个数
     * @return
     */
    public int leaveParkNumbers() {
        if (usedParkNumber == null || unUsedParkNumber == null) {
            initSet();
        }
        return unUsedParkNumber.size();
    }


}
