

//L  X  D  S
// 老 少 男 女
var conf1 = {'L':'L','X':'X','D':'D','S':'S'}

function initViewData(data){
    var mod = data.turn.mod;
    var turn = initTurn(data.turn);
    var counts = data.counts;
    var bgData = {hb:mod==1?bgA(turn.info):bgB(turn.info)}; bgData['hb'].push(turn.lj);
    var jgData = {/*hb:{list:[],jg:0,qh:0}*/};
    for (var i = 0; i < counts.length; i++) {
        var count = counts[i];
        count.g = eval(count.g);
        bgData[count.uid] = mod==1?bgA(count.g):bgB(count.g);
        bgData[count.uid].push(count.g_sum);
        jgData[count.uid] = {list:[],jg:count.tg_sum,qh:0};
        var ts = count.tg?count.tg.split(","):[];
        for (var j = 0; j < ts.length; j++) {
            if(!ts[j])continue;
            var jg = jgData[count.uid];
            var v = new Number(ts[j].split("_")[0])+new Number(ts[j].split("_")[1]);
            jg.list[j] = v;
            jg.qh+=(v>0?1:(v<0?-1:0));
            /*if(turn.user_lock[count.uid-1]=='1'){
                if(isNaN(jgData['hb'].list[j]))jgData['hb'].list[j] = 0;
                jgData['hb'].list[j]+=v;

            }*/

        }
    }

    //处理汇总报告
    var hzJg = {list:[],jg:0,qh:0};
    jgData['hb'] =hzJg;

    var hzjgs = turn.hz_jg?turn.hz_jg.split(","):[];
    for (var i = 0; i < hzjgs.length; i++) {
        if(!hzjgs[i])continue;
        var v = new Number(hzjgs[i]);
        hzJg.list.push(v);
        hzJg.jg+=v;
        hzJg.qh+=(v>0?1:(v<0?-1:0));
    }


    //處理 汇总求和結果
    /*turn.qh = eval(turn.qh );
    var qhBg = createBg([turn.qh[1][0],turn.qh[1][1]]);qhBg.push(turn.qh_sum);
    bgData['qhBg'] =qhBg;
    var qhJg = {list:[null],jg:0,qh:0};
    jgData['qhBg'] =qhJg;

    var qhjgs = turn.qh_jg?turn.qh_jg.split(","):[];
    for (var i = 0; i < qhjgs.length; i++) {
        if(!qhjgs[i])continue;
        var v = new Number(qhjgs[i]);
        qhJg.list.push(v);
        qhJg.jg+=v;
        qhJg.qh+=(v>0?1:(v<0?-1:0));
    }*/
    /* 开始处理原值数据 */
    turn.yz = eval(turn.yz );
    var yzBg = createBg([turn.yz[1][0],turn.yz[1][1]]);
    yzBg.push(turn.yz_sum);
    bgData['yzBg'] =yzBg;
    var yzJg = {list:[null],jg: turn.yz_jg_sum,qh:0}
    jgData['yzBg'] =yzJg;
    var yzjgs = turn.yz_jg?turn.yz_jg.split(","):[];
    for (var i = 0; i < yzjgs.length; i++) {
        if(!yzjgs[i])continue;
        var v = new Number(yzjgs[i]);
        yzJg.list.push(v);
        yzJg.qh+=(v>0?1:(v<0?-1:0));
    }

    /* 结束处理原值数据 */


    /* 开始处理合并报告  */
    /*turn.hb = eval(turn.hb );
    var hbBgData = createBg(turn.hb);
    hbBgData.push(turn.hb_sum);
    bgData["hbBg"] = hbBgData;


    jgData['hbBg'] = {list:[null],jg:turn.hb_jg_sum,qh:0};
    var hbjgs = turn.hb_jg?turn.hb_jg.split(","):[];
    for (var i = 0; i < hbjgs.length; i++) {
        if(!hbjgs[i])continue;
        var v = new Number(hbjgs[i]);
        jgData['hbBg'].list.push(v);
        jgData['hbBg'].qh+=(v>0?1:(v<0?-1:0));
    }*/
    /* 结束处理合并报告  */

    /* 开始处理合并求和  */
    /*turn.hbqh = eval(turn.hbqh );
    var hbqhBgData = createBg(turn.hbqh);
    hbqhBgData.push(turn.hbqh_sum);
    bgData["hbqh"] = hbqhBgData;

    jgData['hbqh'] = {list:[null],jg:0,qh:0};
    var hbqhjgs = turn.hbqh_jg?turn.hbqh_jg.split(","):[];
    for (var i = 0; i < hbqhjgs.length; i++) {
        if(!hbqhjgs[i])continue;
        var v = new Number(hbqhjgs[i]);
        jgData['hbqh'].list.push(v);
        jgData['hbqh'].qh+=(v>0?1:(v<0?-1:0));
        jgData['hbqh'].jg+=v;
    }*/
    /* 结束处理合并合并求和  */

    /* 开始处理選擇報告  */
    /*turn.xz = eval(turn.xz );
    var xzBgData = createBg(turn.xz);
    xzBgData.push(turn.xz_sum);
    bgData["xzBg"] = xzBgData;


    jgData['xzBg'] = {list:[null],jg:turn.xz_jg_sum,qh:0};
    var xzjgs = turn.xz_jg?turn.xz_jg.split(","):[];
    for (var i = 0; i < xzjgs.length; i++) {
        if(!xzjgs[i])continue;
        var v = new Number(xzjgs[i]);
        jgData['xzBg'].list.push(v);
        jgData['xzBg'].qh+=(v>0?1:(v<0?-1:0));
    }*/
    /* 结束处理選擇報告  */

    /* 开始选择求和  */
    /*turn.xzqh = eval(turn.xzqh );
    var xzqhBgData = createBg(turn.xzqh);
    xzqhBgData.push(turn.xzqh_sum);
    bgData["xzqh"] = xzqhBgData;


    jgData['xzqh'] = {list:[null],jg:0,qh:0};
    var xzqhjgs = turn.xzqh_jg?turn.xzqh_jg.split(","):[];
    for (var i = 0; i < xzqhjgs.length; i++) {
        if(!xzqhjgs[i])continue;
        var v = new Number(xzqhjgs[i]);
        jgData['xzqh'].list.push(v);
        jgData['xzqh'].qh+=(v>0?1:(v<0?-1:0));
        jgData['xzqh'].jg+=v;
    }*/

    /* 结束选择求和  */
    /* 开始结果报告  */
    /*turn.jgbg = eval(turn.jgbg );
    var jgbg = getBgJg(turn.jgbg,turn.jgbg_sum,turn.jgbg_jg?turn.jgbg_jg.split(","):[]);
    bgData["jgbg"] = jgbg[0];
    jgData['jgbg'] = jgbg[1];*/
    /* 结束结果报告  */


    /* 开始结果A报告  */
    /*turn.jgbgA = eval(turn.jgbgA);
    var jgbgA = getBgJg(turn.jgbgA,turn.jgbgA_sum,turn.jgbgA_jg?turn.jgbgA_jg.split(","):[]);
    bgData["jgAbg"] = jgbgA[0];
    jgData['jgAbg'] = jgbgA[1];*/
    /* 结束结果A报告  */
    /* 开始结果B报告  */
    /*turn.jgbgB = eval(turn.jgbgB);
    var jgbgB = getBgJg(turn.jgbgB,turn.jgbgA_sum,turn.jgbgB_jg?turn.jgbgB_jg.split(","):[]);
    bgData["jgBbg"] = jgbgB[0];
    jgData['jgBbg'] = jgbgB[1];*/
    /* 结束结果B报告  */
    //处理小板报告
    turn.xbbg = eval("("+turn.xbbg+")");
    bgData['xbbg'] = createBg(turn.xbbg);
    //处理小板报告

    //处理小板报告
    turn.bkhz = eval("("+turn.bkhz+")");
    bgData['bkhz'] = createBg(turn.bkhz);
    //处理小板报告

    //处理板块报告
    turn.bkbg = eval("("+turn.bkbg+")");
    bgData['bkbg'] = createBg(turn.bkbg);
    //处理板块报告

    //处理板块终端
    turn.bkzd = eval("("+turn.bkzd+")");
    bgData['bkzd'] = createBg(turn.bkzd);
    //处理板块终端


    return {turn:turn,bgData:bgData,jgData:jgData};

    /**
     *
     * @param cBg 报告数组
     * @param cBgSum
     * @param jgArray
     * @returns {*[]}
     */
    function getBgJg(cBg,cBgSum,jgArray){

        let bgRet = createBg(cBg);
        bgRet.push(cBgSum);

        let jgRet = {list:[null],jg:0,qh:0};
        for (var i = 0; i < jgArray.length; i++) {
            if(!jgArray[i])continue;
            var v = new Number(jgArray[i]);
            jgRet.list.push(v);
            jgRet.qh+=(v>0?1:(v<0?-1:0));
            jgRet.jg+=v;
        }
        return [bgRet,jgRet];

    }


    function bgA(g){
        var ret = [{name:'',val:''},{name:'',val:''},0];
        ret[0].name = g[4]>0?conf1.L:(g[4]<0?conf1.X:'');
        ret[0].val = ret[0].name?Math.abs(g[4]):0;
        ret[1].name = g[5]>0?conf1.D:(g[5]<0?conf1.S:'');
        ret[1].val = ret[1].name?Math.abs(g[5]):0;
        ret[2] = ret[0].val+ret[1].val
        return ret;

    }
    function bgB(g){


    }

    function createBg(arr){
        var ret = [{name:'',val:''},{name:'',val:''},0];
        ret[0].name = arr[0]>0?conf1.L:(arr[0]<0?conf1.X:'');
        ret[0].val = ret[0].name?Math.abs(arr[0]):0;
        ret[1].name = arr[1]>0?conf1.D:(arr[1]<0?conf1.S:'');
        ret[1].val = ret[1].name?Math.abs(arr[1]):0;
        ret[2] = ret[0].val+ret[1].val
        return ret;

    }


}


function getBgA(g){
    var ret = [{name:'',val:''},{name:'',val:''},0];
    ret[0].name = g[0]>0?conf1.L:(g[0]<0?conf1.X:'');
    ret[0].val = ret[0].name?Math.abs(g[0]):0;
    ret[1].name = g[1]>0?conf1.D:(g[1]<0?conf1.S:'');
    ret[1].val = ret[1].name?Math.abs(g[1]):0;
    ret[2] = ret[0].val+ret[1].val
    return ret;

}

function initTurn(oldTurn){
    var turn = oldTurn;
    turn.info = eval(turn.info);
    turn.user_lock = turn.user_lock.split(',');
    turn.xzbg_lock = turn.xzbg_lock.split(',');
    turn.hbbg_lock = turn.hbbg_lock.split(',');
    turn.xzbg_config = turn.xzbg_config.split(',');
    turn.hbbg_config = turn.hbbg_config.split(',');
    return turn;
}

function createBg(arr){
    var ret = [{name:'',val:''},{name:'',val:''},0];
    ret[0].name = arr[0]>0?conf1.L:(arr[0]<0?conf1.X:'');
    ret[0].val = ret[0].name?Math.abs(arr[0]):0;
    ret[1].name = arr[1]>0?conf1.D:(arr[1]<0?conf1.S:'');
    ret[1].val = ret[1].name?Math.abs(arr[1]):0;
    ret[2] = ret[0].val+ret[1].val
    return ret;

}

function getDefVdata(turn){

    var ret = {"turn":initTurn(turn)
        ,"bgData":{ }
        ,"jgData":{ }

    }

    var cols = ["hb","qhBg","hbBg","hbqh","xzBg","xzqh","jgbg","jgbgA","jgbgB"];

    for (var i = 0; i < cols.length; i++) {

        ret.bgData[cols[i]] = [{"name":'',"val":0},{"name":"","val":0},'',''];
        ret.jgData[cols[i]] = {"list":[],"jg":0,"qh":0};
    }
    return ret;
}

function createBigVdata(bigTurn,vDatas){
    if(bigTurn.frow>=4){
        var bigvData = {bigTurn:bigTurn,jgData:{},bgData:{}};

        handelBg(bigvData,bigTurn);
        for (var i = 0; i < bigTurn.bigTurnConfig.turnNum; i++) {
            add2BigVdata(bigvData,vDatas[i]);
        }

        for (var i in bigvData.jgData) {
            var item = bigvData.jgData[i];
            for(var j in item.list){

                item.qh+=item.list[j]>0?1:(item.list[j]<0?-1:0);

            }

        }

        //终端报告
        //handelDefBg(bigvData,bigTurn,{viewKey:'zdbg',bg:'zd',bgSum:'zd_sum',bgJg:'zd_jg',bgJgSum:'zd_jg_sum'})
        //handelZdbg(bigTurn,bigvData);

        //报告AB
        /*handelDefBg(bigvData,bigTurn,
            {viewKey:'bgA',bg:'bga',bgSum:'bga_sum',bgJg:'bga_jg',bgJgSum:'bga_jg_sum'})
        handelDefBg(bigvData,bigTurn,
            {viewKey:'bgB',bg:'bgb',bgSum:'bgb_sum',bgJg:'bgb_jg',bgJgSum:'bgb_jg_sum'})*/

        //handelJgABbg(bigTurn,bigvData);

        //结果终端
        //handelDefBg(bigvData,bigTurn,{viewKey:'jgzd',bg:'jgzd',bgSum:'jgzd_sum',bgJg:'jgzd_jg',bgJgSum:'jgzd_jg_sum'})
        //handelJgzdbg(bigTurn,bigvData);
        //板块报告
        handelBkBg(bigvData,bigTurn,{viewKey:'bkbg',bg:'bkbg',bgSum:'bkbg_sum',bgJg:'bkbg_jg',bgJgSum:'bkbg_jg_sum'})
        handelBkBg(bigvData,bigTurn,{viewKey:'bkzd',bg:'bkzd',bgSum:'bkzd_sum',bgJg:'bkzd_jg',bgJgSum:'bkzd_jg_sum'})

		//板块汇总
        handelBkBg(bigvData,bigTurn,{viewKey:'bkhz',bg:'bkhz',bgSum:'bkhz_sum',bgJg:'bkhz_jg',bgJgSum:'bkhz_jg_sum'})
		//板块求和
        handelBkBg(bigvData,bigTurn,{viewKey:'bkqh',bg:'bkqh',bgSum:'bkqh_sum',bgJg:'bkqh_jg',bgJgSum:'bkqh_jg_sum'})
        //终端报告
        handelBkBg(bigvData,bigTurn,{viewKey:'zdbg',bg:'zdbg',bgSum:'zdbg_sum',bgJg:'zdbg_jg',bgJgSum:'zdbg_jg_sum'})


    }else{

        var cols = ["hb",/*"qhBg","hbBg","hbqh","xzBg","xzqh","jgbg",*//*"zdbg"
            ,"jgAbg","jgBbg","bgA","bgB","jgzd",*/'bkbg',"bkzd","bkhz","bkqh",'zdbg'];
        var bigvData = {"bigTurn":bigTurn,"bgData":{},"jgData":{}};

        for (var i = 0; i < cols.length; i++) {

            bigvData.bgData[cols[i]] = [{"name":'',"val":0},{"name":"","val":0},'',''];
            bigvData.jgData[cols[i]] = {"list":[],"jg":0,"qh":0};
        }


    }

    return bigvData;

    function handelBg(bigvData,bigTurn){
        var bgData = bigvData.bgData;
        var bg = eval(bigTurn.bg);
        var gj = eval(bigTurn.gj);
        var col = [1,2,3,4,5,6,7,8,9,10,'hb','qhBg','hbBg','hbqh','xzBg','xzqh','jgbg','jgAbg','jgBbg'];
        for (var i = 0; i < col.length; i++) {

            var nm = col[i];
            bgData[nm] = getBgA(bg[i]);
            bgData[nm].push(gj[i]);
        }

    }


    function handelBkBg(bigvData,bigTurn,opt){
        var viewKey = opt.viewKey;
        bigvData.bgData[viewKey] = createBg(eval(bigTurn[opt.bg]));
        bigvData.bgData[viewKey].push(bigTurn[opt.bgSum]);
        var jg = {list:[null],jg: bigTurn[opt.bgJgSum],qh:0,type:[null]};
        bigvData.jgData[viewKey] = jg;

        var jgs = bigTurn[opt.bgJg]?bigTurn[opt.bgJg].split(","):[];
        for (var i = 0; i < jgs.length; i++) {
            if(!jgs[i])continue;
            var item = jgs[i].split('_');
            var v = new Number(item[1]);
            jg.list.push(v);
            jg.type.push(item[0]);
            jg.qh+=(v>0?1:(v<0?-1:0));
        }
    }

    function handelDefBg(bigvData,bigTurn,opt){
        var viewKey = opt.viewKey;
        bigvData.bgData[viewKey] = createBg(eval(bigTurn[opt.bg]));
        bigvData.bgData[viewKey].push(bigTurn[opt.bgSum]);
        var jg = {list:[null],jg: bigTurn[opt.bgJgSum],qh:0};
        bigvData.jgData[viewKey] = jg;

        var jgs = bigTurn[opt.bgJg]?bigTurn[opt.bgJg].split(","):[];
        for (var i = 0; i < jgs.length; i++) {
            if(!jgs[i])continue;
            var v = new Number(jgs[i]);
            jg.list.push(v);
            jg.qh+=(v>0?1:(v<0?-1:0));
        }
    }




    function createBg(arr){
        var ret = [{name:'',val:''},{name:'',val:''},0];
        ret[0].name = arr[0]>0?conf1.L:(arr[0]<0?conf1.X:'');
        ret[0].val = ret[0].name?Math.abs(arr[0]):0;
        ret[1].name = arr[1]>0?conf1.D:(arr[1]<0?conf1.S:'');
        ret[1].val = ret[1].name?Math.abs(arr[1]):0;
        ret[2] = ret[0].val+ret[1].val
        return ret;

    }

    /*function handelJgABbg(bigTurn,bigvData){

        bigvData.bgData['bgA'] = createBg(eval(bigTurn.bga));
        bigvData.bgData['bgA'].push(bigTurn.bga_sum);
        var jgArrA = {list:[null],jg: bigTurn.bga_jg_sum,qh:0};
        bigvData.jgData['bgA'] = jgArrA;

        var arrA = bigTurn.bga_jg?bigTurn.bga_jg.split(","):[];
        for (var i = 0; i < arrA.length; i++) {
            if(!arrA[i])continue;
            var v = new Number(arrA[i]);
            jgArrA.list.push(v);
            jgArrA.qh+=(v>0?1:(v<0?-1:0));
        }


        bigvData.bgData['bgB'] = createBg(eval(bigTurn.bgb));
        bigvData.bgData['bgB'].push(bigTurn.bgb_sum);
        var jgArrB = {list:[null],jg: bigTurn.bgb_jg_sum,qh:0};
        bigvData.jgData['bgB'] = jgArrB;

        var arrB = bigTurn.bgb_jg?bigTurn.bgb_jg.split(","):[];
        for (var i = 0; i < arrB.length; i++) {
            if(!arrB[i])continue;
            var v = new Number(arrB[i]);
            jgArrB.list.push(v);
            jgArrB.qh+=(v>0?1:(v<0?-1:0));
        }



    }*/


    function add2BigVdata(bigVdata,vData){

        var jgData = bigVdata.jgData;
        var col = [1,2,3,4,5,6,7,8,9,10,'hb'/*,'qhBg','hbBg','hbqh','xzBg','xzqh','jgbg'*//*,'jgAbg','jgBbg'*/];


        for (var j = 0; j < col.length; j++) {
            var nm = col[j];
            if(jgData[nm]==null)jgData[nm]={jg:0,list:[],qh:0};

            jgData[nm].jg+=vData.jgData[nm].jg;
        }

        for (var i = 0; i < vData.jgData['hb'].list.length; i++) {

            for (var j = 0; j < col.length; j++) {
                var nm = col[j];
                if(jgData[nm].list[i]==null)jgData[nm].list[i]=0;
                jgData[nm].list[i]+=vData.jgData[nm].list[i];
            }

        }
    }

}
	
	
	