package cn.edu.njupt.carpark.dao;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import cn.edu.njupt.carpark.bean.GarageRelation;


public class GarageRelationDao {

    /**
     * 获取所有的车库号
     * @return
     */
    public List<Integer> listGarageId(){
        List<Integer> res = new ArrayList<>();
        List<GarageRelation> list = LitePal.select("garageNumber").find(GarageRelation.class);
        for(int i = 0 ; i < list.size() ; i++){
            res.add(list.get(i).getGarageNumber());
        }
        return res;
    }


    /**
     * 添加garageRelation 对象
     * @param garageRelation
     * @return
     */
    public boolean saveGarageRelation(GarageRelation garageRelation){
        return garageRelation.save();
    }

    /**
     * 根据车牌号出库
     * @param number
     * @return
     */
    public int deleteGarageRelation(String number){
        return LitePal.deleteAll(GarageRelation.class , "number = ?" , number);
    }


    /**
     * 根据cardId 获取garageRelation 对象
     * @param number
     * @return
     */
    public GarageRelation getGarageRelation(String number){
        List<GarageRelation> res = LitePal.where("number = ?" , number).find(GarageRelation.class);
        return res.size() == 0 ? null : res.get(0);
    }

}
