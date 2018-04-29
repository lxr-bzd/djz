package com.park.api.service;


import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpSession;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.lxr.commons.exception.ApplicationException;
import com.lxr.framework.Subject;
import com.lxr.framework.WebContext;
import com.lxr.framework.web.utils.CookieUtil;
import com.lxr.framework.web.utils.RequestUtil;
import com.park.api.common.BaseService;


@Service
public class SecurityService extends BaseService{
	
	public static final String SUBJECT_KEY = "subject";
	
	
	public static final String TOKEN = "stoken";
	
	@Autowired
	UserService userService;
	
	
	Map<String, Subject> loginMap = new HashMap<String, Subject>(200);
	
	
	public Subject checkUser(Subject subject) {
		
		//这里从数据库查询
		Map<String, Object> user = userService.getByAccount(subject.getAccount());
		
		if(user==null)throw new ApplicationException("账号不存在");
		
		if(user.get("pwd")!=null&&!user.get("pwd").toString().equals(subject.getPwd()))
			throw new ApplicationException("密码错误");
		
		
		subject.setId(user.get("id"));
		
		return subject;

	}
	
	
	
	public Subject login(Subject subject) {
		String token = subject.getToken();
		
		if(token==null||token.length()<10)
			token = createToken(subject);
		
		subject.setToken(token);
		
		subject.setSessionId(RequestUtil.getSession().getId());
		
		
		getLoginMap().put(subject.getAccount(), subject);
		
		RequestUtil.getSession().setAttribute(SUBJECT_KEY, subject);
		
		CookieUtil.setCookie(RequestUtil.getResponse(), TOKEN, token,null);
		
		RequestUtil.getRequest().setAttribute("token", token);
		
		
		return subject;

	}
	
	
	
	public void logout(){
		
		HttpSession session = RequestUtil.getSession();
		
		session.invalidate();
		
	}
	
	
	
	
	
	public boolean isLogin() {
		Subject s = getSubject();
		if(s==null)return false;
		
		Subject ls = getMapSubject(s.getAccount());
		
		if(ls==null) return false;
		
		
		String token = CookieUtil.getCookie(RequestUtil.getRequest(), TOKEN,null);
		
		
		if(ls.getToken().equals(token)&&ls.getAccount().equals(s.getAccount()))
			return true;
		
		 return false;

	}
	
	
	
	
	
	private Subject getSubject() {
		Subject s = getSessionSubject();
		
		return s;

	}
	
	
	
	
	public Subject getSessionSubject() {
		
		HttpSession session = RequestUtil.getRequest().getSession(false);
		
		if(session==null)return null;
		
		Object o = session.getAttribute(SUBJECT_KEY);
		if(o==null)return null;
		return (Subject)o;

	}
	
	
	public Object getSubjectId(){
		
		return getSessionSubject().getId();
	}
	
	
	
	private String createToken(Subject subject) {
		return DigestUtils.md5Hex(subject.getAccount()+System.currentTimeMillis());
	}
	
	
	public String getVcode() {
		
		Object v = RequestUtil.getSession().getAttribute("vcode");
		
		return v==null?null:v.toString();
	
	}


	public Map<String, Subject> getLoginMap() {
		Object o = WebContext.servletContext.getAttribute("loginMap");
		if(o==null||!(o instanceof Map))WebContext.servletContext.setAttribute("loginMap",loginMap);
		return loginMap;
	}
	
	public Subject getMapSubject(String account) {
		Object o = getLoginMap().get(account);
		
		if(o==null||!(o instanceof Subject))return null;
		
		
		return (Subject)o;
	}
	
	
}
