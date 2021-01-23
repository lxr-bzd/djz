package com.park.api.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.park.api.ServiceManage;
import com.park.api.entity.*;
import com.park.api.utils.ArrayUtils;
import com.park.api.utils.JsonUtils;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;
import sun.dc.pr.PRError;

import javax.xml.ws.ServiceMode;
import java.math.BigDecimal;
import java.util.*;

@Service
public class GameGroupService {

    @Autowired
    BigTurnService bigTurnService;

    @Autowired
    GameRefService gameRefService;

    @Autowired
    SysService sysService;


    public static final int GROUP_NUM = 15;


    public Map<String, Object> getMainModel() {
        GameGroup gameGroup = getCurrentTurn();
        if(gameGroup==null)return null;
        Map<String, Object> map = new HashMap<String, Object>();
        for (int i = 0; i < GROUP_NUM; i++) {
            map.put((i+1)+"",bigTurnService.getGroupData(i+1));
        }
        Map gameGroupMap = ServiceManage.jdbcTemplate.queryForMap("select * from game_group where id = ?",gameGroup.getId());

        map.put("config", getConfig());
        map.put("gameGroup", gameGroupMap);
        return map;
    }


    private Map<String,Object> getConfig(){
        Map<String,Object> map = new HashMap<>();
        map.put("tip_open", sysService.getSysConfig("tip_open", Integer.class));
        return map;
    }

    public void doInput(String pei) {
        GameGroup gameGroup = getCurrentTurn();
        if(gameGroup==null)return;

        List<BigTurnResult> results = new ArrayList<>();
        for (int i = 0; i < GROUP_NUM; i++) {
            results.add(bigTurnService.doInputBigTurn(pei,i+1));
        }


        if(gameGroup.getFrow()<3){

            ServiceManage.jdbcTemplate.update("update game_group set frow = ? where id = ?",
                    gameGroup.getFrow()+1,gameGroup.getId());
        }else{


            BigDecimal upFz = StringUtils.isEmpty(gameGroup.getFz())?null:new BigDecimal(JSONArray.parseArray(gameGroup.getFz()).get(0).toString());
            List<BigDecimal> fzArr = StringUtils.isEmpty(gameGroup.getFz())? Arrays.asList(new BigDecimal[]{BigDecimal.ZERO,BigDecimal.ZERO,BigDecimal.ZERO})
            :JSONArray.parseArray(gameGroup.getFz(),BigDecimal.class);
            List<Long> hzqhArr = StringUtils.isEmpty(gameGroup.getFz())? Arrays.asList(new Long[]{0l,0l,0l})
            :JSONArray.parseArray(gameGroup.getHzqh(),Long.class);

            BgItem upSbBg = null;
            if(StringUtils.isNotEmpty(gameGroup.getSb()))
                upSbBg = BgItem.parseItem(gameGroup.getSb());

            List<Integer> fzCounts = JSONArray.parseArray(gameGroup.getFz_count(),Integer.class);

            BgItem hzbg = buildHzbg(results,pei,JsonUtils.toLongArray(gameGroup.getHz()));
            BgItem zdbg = buildZdbg(results,pei,JsonUtils.toLongArray(gameGroup.getZd()));



            Long hzqh = buildHzqh(results);
            long hzqh_sum = hzqh+hzqhArr.get(1);

            BigDecimal fzbg = BigDecimal.ZERO;
            BigDecimal fzbg_sum = fzArr.get(1).add(fzbg);

            if(zdbg.getBg()[0]!=0&&zdbg.getBg()[1]!=0){
                fzbg = hzbg.getBgSum()==0?BigDecimal.ZERO:new BigDecimal(zdbg.getBgSum()).divide(new BigDecimal(hzqh), 3, BigDecimal.ROUND_HALF_UP);
                fzbg_sum = fzArr.get(1).add(fzbg);
            }



            BgItem sb = buildSb(results,zdbg,pei,upSbBg);

            if(upFz!=null)
                countFz(upFz,fzCounts,zdbg);

            String newTrend = buildTrend(gameGroup.getTrend(),results,hzbg,zdbg);
            ServiceManage.jdbcTemplate.update("update game_group set frow = ?," +
                            " zd=?, zd_sum=zd_sum+?, zd_jg=CONCAT(zd_jg,?), zd_jg_sum = zd_jg_sum+?, " +
                            " hz=?, hz_sum=hz_sum+?, hz_jg=CONCAT(hz_jg,?), hz_jg_sum = hz_jg_sum+?, " +
                                "fz=? ,fz_jg=CONCAT(fz_jg,?), "+
                                "hzqh=?,hzqh_jg=CONCAT(hzqh_jg,?),"+
                                "sb=?,sb_jg=CONCAT(sb_jg,?),"+
                                "fz_count=?,trend=?"+
                            " where id = ?",
                    ArrayUtils.concatAll(
                            new Object[]{gameGroup.getFrow()+1}
                            ,buildBgItemArray(zdbg)
                            ,buildBgItemArray(hzbg)
                            ,new Object[]{
                                    JSONArray.toJSONString(new Object[]{fzbg,fzbg_sum,fzbg_sum}),fzbg+","
                                    ,JSONArray.toJSONString(new Object[]{hzqh,hzqh_sum,hzqh_sum}),hzqh+","
                                    ,sb.toItemStr(),sb.getJg()==null?"":((sb.getJg()[0]+sb.getJg()[1])+","),
                                    JSONArray.toJSONString(fzCounts),newTrend
                                    ,gameGroup.getId()})
                    );
        }

    }

    private String buildTrend(String oldTrend,List<BigTurnResult> results ,BgItem hzbg,BgItem zdbg){
        List<Integer> jgs = new ArrayList<>();
        for (int i = 0; i < results.size(); i++) {
            BgItem item = results.get(i).getZdqh();
            if(item.getJg()==null){
                jgs.add(0);
                continue;
            }
            Long jg = item.getJg()[0]+item.getJg()[1];
            jgs.add(jg>0?1:(jg<0?-1:0));
        }

        //取趋势
        if(hzbg.getJg()==null){
            jgs.add(0);
        }else{
            Long jg = hzbg.getJg()[0]+hzbg.getJg()[1];
            jgs.add(jg>0?1:(jg<0?-1:0));
        }
        if(zdbg.getJg()==null){
            jgs.add(0);
        }else {
            Long jg = zdbg.getJg()[0] + zdbg.getJg()[1];
            jgs.add(jg > 0 ? 1 : (jg < 0 ? -1 : 0));
        }


        int[] olds = ArrayUtils.str2int(oldTrend.split(","));
        Integer[] news = new Integer[olds.length];
        for (int i = 0; i < olds.length; i++) {
            if(jgs.get(i)==0){
                news[i] = olds[i];
                continue;
            }

            if(olds[i]==0||(olds[i]*jgs.get(i)<0)){
                news[i] = jgs.get(i)>0?1:-1;
            }else{
                news[i] = olds[i]+(jgs.get(i)>0?1:-1);
            }
        }

        return StringUtils.join(news,",");
    }


    private void countFz(BigDecimal fz,List<Integer> fzCounts,BgItem zdbg){

        int i = -1;
        if(fz.compareTo(new BigDecimal("0.4"))==-1){

        }else if(fz.compareTo(new BigDecimal("0.5"))==-1){
            i = 0;
        }else if(fz.compareTo(new BigDecimal("0.6"))==-1){
            i = 1;
        }else if(fz.compareTo(new BigDecimal("0.7"))==-1){
            i = 2;
        }else if(fz.compareTo(new BigDecimal("0.8"))==-1){
            i = 3;
        }else if(fz.compareTo(new BigDecimal("0.9"))==-1){
            i = 4;
        }else if(fz.compareTo(new BigDecimal("1.0000001"))==-1){
            i = 5;
        }


        if(i>=0){
            int f = 0;
            if(zdbg.getJgType()==1||zdbg.getJgType()==4){
                f = 1;
            }else if(zdbg.getJgType()==5||zdbg.getJgType()==2||zdbg.getJgType()==3){
                f = -1;
            }
            Long zdJg = zdbg.getJg()[0]+zdbg.getJg()[1];
            fzCounts.set(i,fzCounts.get(i)+f);
        }
    }


    /**
     * 双边报告
     * X-S 或者 L-D 记作 一个 L
     * X-D 或者 L-S 记作 一个 X
     * @param results
     * @return L=1 X=-1
     * @param pei L 对应23 ，x对应14
     */
    private BgItem buildSb(List<BigTurnResult> results,BgItem zdbg,String pei,BgItem upBg){
        Integer result = 0;

        for (int i = 0; i < results.size(); i++) {
            BgItem item = results.get(i).getZdqh();
            Long[] bg = item.getBg();
            if((bg[0]<0&&bg[1]<0)||(bg[0]>0&&bg[1]>0)){
                result+=1;
            }
            if((bg[0]<0&&bg[1]>0)||(bg[0]>0&&bg[1]<0)){
                result-=1;
            }
        }
        if(zdbg!=null){
            Long[] bg = zdbg.getBg();
            if((bg[0]<0&&bg[1]<0)||(bg[0]>0&&bg[1]>0)){
                result+=1;
            }
            if((bg[0]<0&&bg[1]>0)||(bg[0]>0&&bg[1]<0)){
                result-=1;
            }
        }

        BgItem ret = new BgItem();
        Long[] bgLong = new Long[]{(long)result,0l};
        ret.setBg(bgLong);
        ret.setBgSum((upBg==null?0:upBg.getBgSum())+bgLong[0]+bgLong[1]);

        Long[] jg = null;
        if(upBg!=null){
            int f = 0;
            if(pei.substring(0, 1).equals("2")||pei.substring(0, 1).equals("3")){
                f = 1;
            }else{
                f = -1;
            }
            jg = new Long[]{upBg.getBg()[0]*f,0l};
            ret.setJgSum(upBg.getJgSum()+jg[0]+jg[1]);
        }
        ret.setJg(jg);
        return ret;
    }

    private Object[] buildBgItemArray(BgItem item){
        Object[] objs = new Object[]{
                JSONArray.toJSONString(item.getBg())
                ,item.getBgSum()
                ,item.getJg()==null?"":(item.getJgType()+"_"+(item.getJg()[0]+item.getJg()[1])+",")
                ,item.getJgSum()};

        return objs;
    }


    private Long buildHzqh(List<BigTurnResult> results){
        Long hzqh = 0l;
        for (int i = 0; i < results.size(); i++) {
            BgItem item = results.get(i).getZdqh();
            Long[] bg = item.getBg();
            if(bg[0]!=0||bg[1]!=0){
                hzqh +=1;
                if(Math.abs(bg[0].longValue())==Math.abs(bg[1].longValue()))hzqh+=1;
            }
        }
        return hzqh;
    }


    private BgItem buildHzbg(List<BigTurnResult> results,String pei,Long[] upBg){

        BgItem ret = new BgItem();
        long[] bg = new long[]{0l,0l};
        for (int i = 0; i < results.size(); i++) {
            BgItem item = results.get(i).getZdbg();
            CountCoreAlgorithm.bgSum(ArrayUtils.toBasic(item.getBg()),bg,false);

        }
        Long[] bgLong = ArrayUtils.toObject(bg);

        ret.setBg(bgLong);
        ret.setJg(upBg==null?null:CountCoreAlgorithm.computeJg(pei.substring(0, 1),upBg));
        ret.setBgSum(Math.abs(ret.getBg()[0])+Math.abs(ret.getBg()[1]));
        ret.setJgSum(ret.getJg()==null?0:ret.getJg()[0]+ret.getJg()[1]);
        ret.setJgType(upBg==null?null:BigCoreService.countJgDetail(ret.getJg()));
        return ret;

    }

    private BgItem buildZdbg(List<BigTurnResult> results,String pei,Long[] upBg){
        BgItem ret = new BgItem();
        BigDecimal[] bg = new BigDecimal[]{BigDecimal.ZERO,BigDecimal.ZERO};
        for (int i = 0; i < results.size(); i++) {
            BgItem item = results.get(i).getZdqh();
            CountCoreAlgorithm.bgCount2(ArrayUtils.toBasic(item.getBg()),bg,1);
        }

        Long[] bgLong = new Long[]{bg[0].setScale(0,BigDecimal.ROUND_HALF_UP).longValue(),bg[1].setScale(0,BigDecimal.ROUND_HALF_UP).longValue()};

        ret.setBg(bgLong);
        ret.setJg(upBg==null?null:CountCoreAlgorithm.computeJg(pei.substring(0, 1),upBg));
        ret.setBgSum(Math.abs(ret.getBg()[0])+Math.abs(ret.getBg()[1]));
        ret.setJgSum(ret.getJg()==null?0:ret.getJg()[0]+ret.getJg()[1]);
        ret.setJgType(upBg==null?null:BigCoreService.countJgDetail(ret.getJg()));

        return ret;
    }

    public GameGroup getCurrentTurn() {
        GameGroup bigTurn = null;
        try {
                bigTurn = ServiceManage.jdbcTemplate.queryForObject("select * from game_group where state=1 limit 1",
                        new BeanPropertyRowMapper<GameGroup>(GameGroup.class));
                return bigTurn;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public void doFinish(){
        GameGroup gameGroup = getCurrentTurn();
        for (int i = 0; i < GROUP_NUM; i++) {
            bigTurnService.doFinishTurn(i+1);
        }
        ServiceManage.jdbcTemplate.update("UPDATE game_group SET state=2 WHERE id =?",gameGroup.getId());
    }

    public void doRenew(){
        GameGroup bigTurn = createGameGroup();
        //TurnGroupCountService.initData(bigTurn);
        for (int i = 0; i < GROUP_NUM; i++) {
            bigTurnService.doRenewTurn(i+1);
        }
    }


    private GameGroup createGameGroup(){
        GameGroup gameGroup = new GameGroup();
        gameGroup.setFrow(1);
        gameGroup.setState(1);

        Map<String, Object> param = null;
        try {
            param = BeanUtils.describe(gameGroup);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "INSERT INTO `game_group` ( `frow`, `state`" +
                ", `hz`, `hz_jg`, `hz_sum`, `hz_jg_sum`," +
                " `zd`, `zd_jg`, `zd_sum`, `zd_jg_sum`," +
                " `fz`, `fz_jg`, " +
                " `sb`, `sb_jg`,fz_count, " +
                " `hzqh`, `hzqh_jg`,trend) " +
                "VALUES ( :frow, :state," +
                " '', '', 0, 0" +
                ", '', '', 0, 0" +
                ", '', ''" +
                ", '', '','[0,0,0,0,0,0]'" +
                ", '', '','0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0')";
        ServiceManage.namedJdbcTemplate.update(sql,new MapSqlParameterSource(param),keyHolder);

        gameGroup.setId(keyHolder.getKey().intValue());

        return gameGroup;
    }

    public static void main(String[] args) {

        String i = StringUtils.join(new Integer[]{1,2,1},",");
        System.out.println(i);
    }

}
