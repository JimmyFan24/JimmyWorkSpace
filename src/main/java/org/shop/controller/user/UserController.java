package org.shop.controller.user;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.shop.pojo.Hw;
import org.shop.pojo.Sh;
import org.shop.pojo.User;

import org.shop.service.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/user/")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private HwService hwService;

	@Autowired
	private ShService shService;

	// login
	@RequestMapping("tzlogin")
	public String tzlogin() {
		return "login";
	}

	// 登录验证
	@ResponseBody
	@RequestMapping("login")
	public String login(User user, HttpSession session, String requestDate) {
		Map map = new HashMap();
		JSONObject requestJson = JSONObject.fromObject(requestDate);
		map.put("name", requestJson.getString("name"));
		map.put("password", requestJson.getString("password"));
		user.setName(requestJson.getString("name"));
		user.setPassword(requestJson.getString("password"));
		User user2 = userService.login(user);
		session.setAttribute("t2", user2.getT2());
		session.setAttribute("id", user2.getId());
		if (user2 == null) {
			Map reMap = new HashMap();
			reMap.put("succ", "false");
			JSONObject jsonObject = JSONObject.fromObject(reMap);
			return jsonObject.toString();
		} else {
			System.out.println("user2" + user2);
			session.setAttribute("name", user.getName());
			session.setAttribute("id", user2.getId());
			session.setAttribute("t1", user2.getT1());
			Map reMap = new HashMap();
			reMap.put("succ", "true");
			JSONObject jsonObject = JSONObject.fromObject(reMap);
			return jsonObject.toString();
		}

	}

	@RequestMapping("sy")
	public String denglu(HttpSession session, String t2, Model model, String name, User user) {
		int qx = (int) session.getAttribute("t1");
		// model.addAttribute("list", userService.findall());
		if (qx == 0) {
			model.addAttribute("list", userService.findall(user));
			return "gly/yh";
		} else if (qx == 1) {
			model.addAttribute("list", userService.findall(user));
			return "gly/yh";
		} else if (qx == 2) {
			return "redirect:tzckxs";
		} else if (qx == 3) {
			return "redirect:tzkccg";
		}
		return "login";
	}
	
	

	@RequestMapping("touser")
	public String touser(User user) throws ParseException {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH");
		String time = df.format(System.currentTimeMillis());
		user.setDate(df.parse(time));
		user.setT1(4);
		user.setT2(0);
		userService.touser(user);
		return "redirect:tzlogin";
	}

	@RequestMapping("delete")
	public String delete(int id) {
		userService.delete(id);
		return "redirect:sy";
	}

	@RequestMapping("tjyh")
	public String tzyh() {
		return "gly/upyh";
	}

	@RequestMapping("insert")
	public String insert(User user) throws ParseException {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH");
		String time = df.format(System.currentTimeMillis());
		user.setDate(df.parse(time));
		userService.touser(user);
		return "redirect:sy";
	}

	@RequestMapping("tzup")
	public String tzup(int id, Model mode) {
		User user = userService.findid(id);
		mode.addAttribute("user", user);
		return "gly/user";
	}
	
	@RequestMapping("tzzup")
	public String tzzup(Model model,HttpSession session) {
		int  id=(int) session.getAttribute("id");
		User user = userService.findid(id);
		model.addAttribute("user", user);
		return "gly/user";
	}

	@RequestMapping("upyh")
	public String upyh(User user) throws ParseException {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH");
		String time = df.format(System.currentTimeMillis());
		userService.userup(user);
		return "redirect:sy";
	}

	@RequestMapping("tzhw")
	public String tzhw(Model model, Hw hw) {
		hw.setSj(1);
		hw.setSh(1);
		model.addAttribute("list", hwService.sp(hw));
		return "gly/hw";
	}

	@RequestMapping("xj")
	public String spxj(int id, Hw hw) throws ParseException {
		hw.setSh(1);
		hw.setSj(0);
		hw.setId(id);
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH");
		String time = df.format(System.currentTimeMillis());
		hw.setDate(df.parse(time));
		hwService.spxj(hw);
		return "redirect:tzhw";
	}

	@RequestMapping("tzkc")
	public String tzkc(Model model, Hw hw) {
		hw.setSj(0);
		hw.setSh(1);
		model.addAttribute("list", hwService.sp(hw));
		return "gly/kc";
	}

	@RequestMapping("sj")
	public String spsj(int id, Hw hw) throws ParseException {
		hw.setSh(1);
		hw.setSj(1);
		hw.setId(id);
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH");
		String time = df.format(System.currentTimeMillis());
		hw.setDate(df.parse(time));
		hwService.spxj(hw);
		return "redirect:tzkc";
	}

	@RequestMapping("tzrk")
	public String tzrk() {
		return "gly/rkd";
	}

	@RequestMapping("rk")
	public String zjrk(Hw hw, HttpSession session) throws ParseException {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH");
		String time = df.format(System.currentTimeMillis());
		hw.setDate(df.parse(time));
		hw.setSj(0);
		hw.setSh(0);
		String zrr = (String) session.getAttribute("name");
		hw.setZrr(zrr);
		hwService.xjrk(hw);
		return "redirect:tzrk";
	}

	@RequestMapping("tzrksh")
	public String tzrush(Model model, Hw hw) {
		hw.setSh(0);
		hw.setSj(0);
		model.addAttribute("list", hwService.sp(hw));
		return "gly/rksh";
	}

	@RequestMapping("rks")
	public String rk(int id, Hw hw) throws ParseException {
		hw.setSj(0);
		hw.setSh(1);
		hw.setId(id);
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH");
		String time = df.format(System.currentTimeMillis());
		hw.setDate(df.parse(time));
		hwService.spxj(hw);
		return "redirect:tzrksh";
	}

	@RequestMapping("tzck")
	public String tzck(Model model, Hw hw) {
		hw.setSh(1);
		hw.setSj(1);
		List list = hwService.sp(hw);
		model.addAttribute("list", list);
		return "gly/ckd";
	}

	@RequestMapping("ckid")
	public String ckid(int id, Model model, HttpSession session) {
		Hw list = hwService.dy(id);
		int jj = list.getNumber();
		session.setAttribute("jj", jj);
		session.setAttribute("gg", list.getMoney());
		model.addAttribute("list", list);
		return "gly/ckl";
	}

	@RequestMapping("ckl")
	public String ckl(Sh sh, HttpSession session, Hw hw) throws ParseException {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH");
		String time = df.format(System.currentTimeMillis());
		sh.setDate(df.parse(time));
		sh.setSh(0);
		int g = sh.getJg() - sh.getMoney();
		System.out.println(g);
		sh.setLr(sh.getSl() * g);
		String name = (String) session.getAttribute("name");
		sh.setZrr(name);
		shService.ck(sh);
		return "redirect:tzck";
	}

	@RequestMapping("tzcksh")
	public String cksss(Model model) {
		List list = shService.cc(0);
		model.addAttribute("list", list);
		return "gly/cksh";

	}

	@RequestMapping("cksh")
	public String cksh(int id, Sh sh, Hw hws, HttpSession session) throws ParseException {
		sh.setSh(1);
		sh.setHw(shService.hwss(id).getHw());
		shService.cksh(sh);
		Sh ssSh = new Sh();
		String name = shService.hwss(id).getHw();
		List list = shService.hws(name);
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH");
		String time = df.format(System.currentTimeMillis());
		hws.setDate(df.parse(time));
		Hw hsHw = new Hw();
		hsHw = hwService.dys(name);
		int ye = hsHw.getNumber();
		int l = ye - shService.hwss(id).getSl();
		hws.setNumber(l);
		hws.setName(name);
		hwService.ckkk(hws);
		return "redirect:tzcksh";
	}

	@RequestMapping("sb")
	public String sb(int id, HttpSession session) {
		shService.jj(id);
		return "redirect:tzcksh";
	}

	@RequestMapping("tzbb")
	public String bb(Model model) {
	     Map map = new HashMap(); 
	     List list=new ArrayList<>();
		 //list = shService.bb();
	     list= shService.bbs();
	    // String str=String.join("=", list);
	     
	//	System.out.println(str);
		JSONArray json = JSONArray.fromObject(list);
		
		model.addAttribute("list", list);
		model.addAttribute("json", json);
		System.out.println(json);
		System.out.println(list);
		
		return "gly/bb";
	}
	//********************************采购******************************************
	@RequestMapping("tzkccg")
	public String tzkccg(Model model, Hw hw) {
		hw.setSj(0);
		hw.setSh(1);
		model.addAttribute("list", hwService.sp(hw));
		return "cg/kc";
	}
	@RequestMapping("tzrkcg")
	public String tzrkcg() {
		return "cg/rkd";
	}
	
	@RequestMapping("rkcg")
	public String zjrkcg(Hw hw, HttpSession session) throws ParseException {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH");
		String time = df.format(System.currentTimeMillis());
		hw.setDate(df.parse(time));
		hw.setSj(0);
		hw.setSh(0);
		String zrr = (String) session.getAttribute("name");
		hw.setZrr(zrr);
		hwService.xjrk(hw);
		return "redirect:tzrkcg";
	}
	
	@RequestMapping("tzzupss")
	public String tzzupss(Model model,HttpSession session) {
		int  id=(int) session.getAttribute("id");
		User user = userService.findid(id);
		model.addAttribute("user", user);
		return "cg/user";
	}
	
	//*************************************销售**************************************
	@RequestMapping("tzckxs")
	public String tzckxs(Model model, Hw hw) {
		hw.setSh(1);
		hw.setSj(1);
		List list = hwService.sp(hw);
		model.addAttribute("list", list);
		return "xs/ckd";
	}
	@RequestMapping("ckidxs")
	public String ckidxs(int id, Model model, HttpSession session) {
		Hw list = hwService.dy(id);
		int jj = list.getNumber();
		session.setAttribute("jj", jj);
		session.setAttribute("gg", list.getMoney());
		model.addAttribute("list", list);
		return "xs/ckl";
	}
	
	@RequestMapping("cklxs")
	public String cklxs(Sh sh, HttpSession session, Hw hw) throws ParseException {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH");
		String time = df.format(System.currentTimeMillis());
		sh.setDate(df.parse(time));
		sh.setSh(0);
		int g = sh.getJg() - sh.getMoney();
		System.out.println(g);
		sh.setLr(sh.getSl() * g);
		String name = (String) session.getAttribute("name");
		sh.setZrr(name);
		shService.ck(sh);
		return "redirect:tzckxs";
	}
	
	
	@RequestMapping("tzzups")
	public String tzzups(Model model,HttpSession session) {
		int  id=(int) session.getAttribute("id");
		User user = userService.findid(id);
		model.addAttribute("user", user);
		return "xs/user";
	}
	
	

	@RequestMapping("upyhs")
	public String upyhs(User user) throws ParseException {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH");
		String time = df.format(System.currentTimeMillis());
		user.setT1(0);
		user.setT2(0);
		user.setDate(df.parse(time));
		userService.userup(user);
		return "redirect:tzlogin";
	}
}
