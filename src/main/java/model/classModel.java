package model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import esayhelper.DBHelper;
import esayhelper.formHelper;
import esayhelper.jGrapeFW_Message;
import esayhelper.formHelper.formdef;

public class classModel {
	private static DBHelper appclass;
	private static formHelper form;
	private JSONObject _obj = new JSONObject();

	static {
		appclass = new DBHelper("mysql", "appclass");
		form = appclass.getChecker();
	}

	public classModel() {
		form.putRule("classname", formdef.notNull);
	}

	public String insert(JSONObject appclassInfo) {
		if (appclassInfo.containsKey("id")) {
			appclassInfo.remove("id");
		}
		if (!form.checkRuleEx(appclassInfo)) {
			return resultMessage(1, ""); // 非空字段验证
		}
		// 判断类是否存在
		if (find(appclassInfo.get("classname").toString(),
				appclassInfo.get("sysid").toString()) != null) {
			return resultMessage(2, "");
		}
		String info = appclass.data(appclassInfo).insertOnce().toString();

		return resultMessage(find(info));
	}

	public int update(String aid, JSONObject appclassInfo) {
		if (appclassInfo.containsKey("id")) {
			appclassInfo.remove("id");
		}
		return appclass.eq("id", aid).data(appclassInfo).update() != null ? 0
				: 99;
	}

	public int delete(String aid) {
		return appclass.eq("id", aid).delete() != null ? 0 : 99;
	}

	public int delete(String[] ids) {
		appclass.or();
		for (int i = 0; i < ids.length; i++) {
			appclass.eq("id", ids[i]);
		}
		return appclass.deleteAll() != ids.length ? 0 : 99;
	}

	public String search(JSONObject appclassInfo) {
		for (Object object2 : appclassInfo.keySet()) {
			appclass.eq(object2.toString(),
					appclassInfo.get(object2.toString()));
		}
		JSONArray array = appclass.limit(20).select();
		return resultMessage(array);
	}

	public String search(String sysid) {
		JSONArray array = appclass.eq("sid", sysid).limit(20).select();
		return resultMessage(array);
	}

	public JSONObject find(String clsid) {
		return appclass.eq("id", clsid).find();
	}

	public JSONObject find(String classname, String sysid) {
		return appclass.eq("classname", classname).eq("sysid", sysid).find();
	}

	@SuppressWarnings("unchecked")
	public String page(int idx, int pageSize) {
		JSONArray array = appclass.page(idx, pageSize);
		JSONObject object = new JSONObject();
		object.put("totalSize",
				(int) Math.ceil((double) appclass.count() / pageSize));
		object.put("currentPage", idx);
		object.put("pageSize", pageSize);
		object.put("data", array);
		return resultMessage(object);
	}

	@SuppressWarnings("unchecked")
	public String page(int idx, int pageSize, JSONObject userInfo) {
		for (Object object2 : userInfo.keySet()) {
			appclass.eq(object2.toString(), userInfo.get(object2.toString()));
		}
		JSONArray array = appclass.dirty().page(idx, pageSize);
		JSONObject object = new JSONObject();
		object.put("totalSize",
				(int) Math.ceil((double) appclass.count() / pageSize));
		object.put("currentPage", idx);
		object.put("pageSize", pageSize);
		object.put("data", array);
		return resultMessage(object);
	}

	/**
	 * 将map添加至JSONObject
	 * 
	 * @param map
	 * @param object
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public JSONObject AddMap(HashMap<String, Object> map, JSONObject object) {
		if (map.entrySet() != null) {
			Iterator<Entry<String, Object>> iterator = map.entrySet()
					.iterator();
			while (iterator.hasNext()) {
				Map.Entry<String, Object> entry = (Map.Entry<String, Object>) iterator
						.next();
				if (!object.containsKey(entry.getKey())) {
					object.put(entry.getKey(), entry.getValue());
				}
			}
		}
		return object;
	}

	@SuppressWarnings("unchecked")
	public String resultMessage(JSONObject object) {
		_obj.put("records", object);
		return resultMessage(0, _obj.toString());
	}

	@SuppressWarnings("unchecked")
	public String resultMessage(JSONArray array) {
		_obj.put("records", array);
		return resultMessage(0, _obj.toString());
	}

	public String resultMessage(int num, String message) {
		String msg = "";
		switch (num) {
		case 0:
			msg = message;
			break;
		case 1:
			msg = "必填字段为空";
			break;
		case 2:
			msg = "该类已存在";
			break;
		default:
			msg = "其他操作异常";
			break;
		}
		return jGrapeFW_Message.netMSG(num, msg);
	}
}
