
var groupUtils = {}

groupUtils.createVdata = function (data) {
    var mainData =  data.gameGroup;
    if (mainData.frow >= 4) {
        var groupVdata = {mainData: mainData, jgData: {}, bgData: {}};

        for (var i = 0; i < 15; i++) {
            if(mainData.frow>=4)
                this.handelBkBg(groupVdata,data[i+1],{viewKey:'bkbg'+(i+1),bg:'zdqh',bgSum:'zdqh_sum',bgJg:'zdqh_jg',bgJgSum:'zdqh_jg_sum'})
            else
                getDefVdata(data[i].turn);
        }
        this.handelBkBg(groupVdata,mainData,{viewKey:'hzbg',bg:'hz',bgSum:'hz_sum',bgJg:'hz_jg',bgJgSum:'hz_jg_sum'})
        this.handelBkBg(groupVdata,mainData,{viewKey:'zdbg',bg:'zd',bgSum:'zd_sum',bgJg:'zd_jg',bgJgSum:'zd_jg_sum'})
        this.handelNewBg(groupVdata,mainData,{viewKey:'hzqh',bg:'hzqh',bgJg:'hzqh_jg'})
        this.handelNewBg(groupVdata,mainData,{viewKey:'fzbg',bg:'fz',bgJg:'fz_jg'})
        this.handelSbBg(groupVdata,mainData,{viewKey:'sbbg',bg:'sb',bgJg:'sb_jg'})

        var trend = mainData.trend.split(",");
        var maxTrend = [0,[0]]
        for (let i = 0; i < trend.length; i++) {
            let m = Math.abs(new Number(trend[i]));
            if(m<maxTrend[0]) {
                continue;
            }
            if(m>maxTrend[0]) {
                maxTrend[0] = m;
                maxTrend[1] = [i];
            }
            if(m==maxTrend[0]) {
                maxTrend[1].push(i);
            }
        }

        for (let i = 0; i < maxTrend[1].length; i++) {
            let index = maxTrend[1][i];
            if(index<15){
                var jg = groupVdata.jgData['bkbg'+(index+1)]
                jg.trend = maxTrend[0];
            }
            if(index==15){
                var jg = groupVdata.jgData['hzbg']
                jg.trend = maxTrend[0];
            }
            if(index==16){
                var jg = groupVdata.jgData['zdbg']
                jg.trend = maxTrend[0];
            }
        }


        var sjSum = 0;
        for (let i = 0; i < 17; i++) {
            let index = null;
            if(i<15){
                index = 'bkbg'+(i+1);
            }i
            if(i==15){
                index = 'hzbg';
            }
            if(i==16){
                index = 'zdbg';
            }
            var jg = groupVdata.jgData[index];
            sjSum+=jg.sj;
        }

        var fz_count = eval(mainData.fz_count);
        groupVdata.jgData['hzqh'].jg = fz_count[0]
        groupVdata.jgData['hzqh'].qh = fz_count[1]
        groupVdata.bgData['hzqh'][3] = fz_count[2]
        groupVdata.jgData['hzqh'].sj = sjSum

        groupVdata.jgData['fzbg'].jg = fz_count[3]
        groupVdata.jgData['fzbg'].qh = fz_count[4]
        groupVdata.bgData['fzbg'][3] = fz_count[5]


        var num = 0;
        for (let i = 0; i < fz_count.length; i++) {
            num+=fz_count[i];
        }
        groupVdata.jgData['fzbg'].sj = num;

        //分值报告
        /*groupVdata.bgData['fzbg'] = [{"name": '', "val": 0}, {"name": "", "val": 0}, mainData.fz, mainData.fz_sum];
        groupVdata.jgData['fzbg'] = {"list": [],type:[], "jg": 0, "qh": 0};
        for (let i = 0; i < groupVdata.jgData['hzbg'].list.length; i++) {
            groupVdata.jgData['fzbg'].list.push(null)
            groupVdata.jgData['fzbg'].type.push(null)
        }*/
        return groupVdata;
    } else {

        var cols = [ 'bkbg1', 'bkbg2', 'bkbg3', 'bkbg4', 'bkbg5', 'bkbg6', 'bkbg7', 'bkbg8', 'bkbg9', 'bkbg10', 'bkbg11', 'bkbg12', 'bkbg13', 'bkbg14', 'bkbg15', 'hzbg','sbbg', 'hzqh', 'zdbg','fzbg'];
        var bigvData = {mainData: mainData, "bgData": {}, "jgData": {}};

        for (var i = 0; i < cols.length; i++) {

            bigvData.bgData[cols[i]] = [{"name": '', "val": 0}, {"name": "", "val": 0}, '', ''];
            bigvData.jgData[cols[i]] = {"list": [], "jg": 0, "qh": 0};
        }
        return bigvData;
    }
}

groupUtils.initViewData = function (groupVdata,mainData) {
    //终端报告
    this.handelBkBg(groupVdata,bigTurn,{viewKey:'zdqh',bg:'zdqh',bgSum:'zdqh_sum',bgJg:'zdqh_jg',bgJgSum:'zdqh_jg_sum'})

}

groupUtils.handelBkBg = function (bigvData,bigTurn,opt){
    var viewKey = opt.viewKey;
    bigvData.bgData[viewKey] = createBg(eval(bigTurn[opt.bg]));
    bigvData.bgData[viewKey].push(bigTurn[opt.bgSum]);
    var jg = {list:[null],jg: bigTurn[opt.bgJgSum],qh:0,type:[null],sj:0};
    bigvData.jgData[viewKey] = jg;

    var jgs = bigTurn[opt.bgJg]?bigTurn[opt.bgJg].split(","):[];
    for (var i = 0; i < jgs.length; i++) {
        if(!jgs[i])continue;
        var item = jgs[i].split('_');
        var v = new Number(item[1]);
        jg.list.push(v);
        jg.type.push(item[0]);
        jg.qh+=(v>0?1:(v<0?-1:0));
        if(item[0]=='1'||item[0]=='4'){
            jg.sj+=1
        }else if(item[0]=='5'||item[0]=='2'||item[0]=='3'){
            jg.sj-=1
        }

    }

}

groupUtils.handelNewBg = function (bigvData,bigTurn,opt) {
    var viewKey = opt.viewKey;
    var data = eval(bigTurn[opt.bg])

    bigvData.bgData[viewKey] = [{"name": '', "val": 0}, {"name": "", "val": 0}, data[0], data[1]];
    var jg = {list: [null], jg: data[2], qh: 0};
    bigvData.jgData[viewKey] = jg;

    var jgs = bigTurn[opt.bgJg] ? bigTurn[opt.bgJg].split(",") : [];
    for (var i = 0; i < jgs.length; i++) {
        if (!jgs[i]) continue;
        var v = new Number(jgs[i]);
        jg.list.push(v);
        jg.qh += (v > 0 ? 1 : (v < 0 ? -1 : 0));
    }
}


groupUtils.handelSbBg = function (bigvData,bigTurn,opt) {
    var viewKey = opt.viewKey;
    var data = eval(bigTurn[opt.bg])


    bigvData.bgData[viewKey] = [{"name": data[0]>0?'L':(data[0]<0?'X':''), "val": Math.abs(data[0])}, {"name": "", "val": 0}, data[0], null];
    var jg = {list: [null], jg: data[3], qh: 0};
    bigvData.jgData[viewKey] = jg;

    var jgs = bigTurn[opt.bgJg] ? bigTurn[opt.bgJg].split(",") : [];
    for (var i = 0; i < jgs.length; i++) {
        if (!jgs[i]) continue;
        var v = new Number(jgs[i]);
        jg.list.push(v);
        jg.qh += (v > 0 ? 1 : (v < 0 ? -1 : 0));
    }
}