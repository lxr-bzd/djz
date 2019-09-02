

//L  X  D  S
// 老 少 男 女
var conf1 = {'L':'L','X':'X','D':'D','S':'S'}
	
	function initViewData(data){
		var mod = data.turn.mod;
		var turn = initTurn(data.turn);
		var counts = data.counts;
		var bgData = {hb:mod==1?bgA(turn.info):bgB(turn.info)}; bgData['hb'].push(turn.lj);
		var jgData = {hb:{list:[],jg:0,qh:0}};
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
				if(turn.user_lock[count.uid-1]=='1'){
					if(isNaN(jgData['hb'].list[j]))jgData['hb'].list[j] = 0;
					jgData['hb'].list[j]+=v;
					
				}
				
			}
		}
		
		for (var i = 0; i < jgData['hb'].list.length; i++) {
			var v = jgData['hb'].list[i];
			jgData['hb'].jg+=v;
			jgData['hb'].qh+=(v>0?1:(v<0?-1:0));
			
		}
		//處理求和結果
		
		turn.qh = eval(turn.qh );
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
		}
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
		turn.hb = eval(turn.hb );
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
		}
		
		
		/* 结束处理合并报告  */
		/* 开始处理合并求和  */
		turn.hbqh = eval(turn.hbqh );
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
		}
		
		
		/* 结束处理合并合并求和  */
		
		/* 开始处理選擇報告  */
		turn.xz = eval(turn.xz );
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
		}
		
		
		/* 结束处理選擇報告  */
		
		
		
		return {turn:turn,bgData:bgData,jgData:jgData};
		
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
	
	function getDefVdata(turn){
		
		return {"turn":initTurn(turn)
				,"bgData":{ "hb":[{"name":'',"val":0},{"name":"","val":0},'','']
							,"hbBg":[{"name":'',"val":0},{"name":"","val":0},'','']
							,"yzBg":[{"name":'',"val":0},{"name":"","val":0},'','']
							,"qhBg":[{"name":'',"val":0},{"name":"","val":0},'','']
							,"hbqh":[{"name":'',"val":0},{"name":"","val":0},'','']
				,"xzBg":[{"name":'',"val":0},{"name":"","val":0},'','']}
				,"jgData":{ "hb":{"list":[],"jg":0,"qh":0}
							,"hbBg":{"list":[],"jg":0,"qh":0}
							,"yzBg":{"list":[],"jg":0,"qh":0}
							,"qhBg":{"list":[],"jg":0,"qh":0}
							,"hbqh":{"list":[],"jg":0,"qh":0}
							,"xzBg":{"list":[],"jg":0,"qh":0}
							}
				
				}
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
			
			
		}else{
			
			var bigvData = {"bigTurn":bigTurn
					,"bgData":{ "hb":[{"name":'',"val":0},{"name":"","val":0},'','']
								,"hbBg":[{"name":'',"val":0},{"name":"","val":0},'','']
								,"yzBg":[{"name":'',"val":0},{"name":"","val":0},'','']
								,"qhBg":[{"name":'',"val":0},{"name":"","val":0},'','']
								,"hbqh":[{"name":'',"val":0},{"name":"","val":0},'','']
								,"xzBg":[{"name":'',"val":0},{"name":"","val":0},'','']}
					,"jgData":{ "hb":{"list":[],"jg":0,"qh":0}
								,"hbBg":{"list":[],"jg":0,"qh":0}
								,"yzBg":{"list":[],"jg":0,"qh":0}
								,"qhBg":{"list":[],"jg":0,"qh":0}
								,"hbqh":{"list":[],"jg":0,"qh":0}
								,"xzBg":{"list":[],"jg":0,"qh":0}
								}
					}

			
		}
		
		return bigvData;
		
		function handelBg(bigvData,bigTurn){
			var bgData = bigvData.bgData;
			var bg = eval(bigTurn.bg);
			var gj = eval(bigTurn.gj);
			var col = [1,2,3,4,5,6,7,8,9,10,'hb','hbBg','xzBg'];
			for (var i = 0; i < col.length; i++) {
				
				var nm = col[i];
				bgData[nm] = getBgA(bg[i]);
				bgData[nm].push(gj[i]);
			}
			
		}
		
		function add2BigVdata(bigVdata,vData){
			
			var jgData = bigVdata.jgData;
			var col = [1,2,3,4,5,6,7,8,9,10,'hb','hbBg','xzBg'];
			
			
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
	
	
	