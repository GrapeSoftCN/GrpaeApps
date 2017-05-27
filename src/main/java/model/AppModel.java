package model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import apps.appsProxy;
import esayhelper.DBHelper;
import esayhelper.StringHelper;
import esayhelper.formHelper;
import esayhelper.jGrapeFW_Message;
import jodd.util.ArraysUtil;
import esayhelper.formHelper.formdef;

public class AppModel {
	private static DBHelper appserver;
	private static formHelper _form;
	private JSONObject _obj = new JSONObject();

	static {
		appserver = new DBHelper("localdb", "apps", "id");
		_form = appserver.getChecker();
	}

	public AppModel() {
		_form.putRule("name", formdef.notNull);
	}

	public String insert(JSONObject appInfo) {
		if (!_form.checkRuleEx(appInfo)) {
			return resultMessage(1, ""); // 非空字段验证
		}
		// 判断该系统是否存在
		if (SearchByName(appInfo.get("name").toString()) != null) {
			return resultMessage(2, "");
		}
		String info = appserver.data(appInfo).insertOnce().toString();
		return resultMessage(find(info));

	}

	public int update(String aid, JSONObject appInfo) {
		if (appInfo.containsKey("id")) {
			appInfo.remove("id");
		}
		return appserver.eq("id", aid).data(appInfo).update() != null ? 0 : 99;
	}

	public int delete(String aid) {
		return appserver.eq("id", aid).delete() != null ? 0 : 99;
	}

	public int delete(String[] ids) {
		appserver.or();
		for (int i = 0, len = ids.length; i < len; i++) {
			appserver.eq("id", ids[i]);
		}
		return appserver.deleteAll() == ids.length ? 0 : 99;
	}

	public String search(JSONObject appInfo) {
		for (Object object2 : appInfo.keySet()) {
			appserver.like(object2.toString(), appInfo.get(object2.toString()));
		}
		JSONArray array = appserver.limit(20).select();
		return resultMessage(array);
	}

	public String search(String sysid) {
		JSONArray array = appserver.eq("sysid", sysid).limit(20).select();
		return resultMessage(array);
	}

	// 根据名称查询系统信息
	public JSONObject SearchByName(String name) {
		JSONObject object = appserver.eq("name", name).find();
		return object;
	}

	public JSONObject find(String appssid) {
		return appserver.eq("id", appssid).find();
	}

	@SuppressWarnings("unchecked")
	public String page(int idx, int pageSize) {
		JSONArray array = appserver.page(idx, pageSize);
		JSONObject object = new JSONObject();
		object.put("totalSize",
				(int) Math.ceil((double) appserver.count() / pageSize));
		object.put("currentPage", idx);
		object.put("pageSize", pageSize);
		object.put("data", array);
		return resultMessage(object);
	}

	@SuppressWarnings("unchecked")
	public String page(int idx, int pageSize, JSONObject userInfo) {
		for (Object object2 : userInfo.keySet()) {
			appserver.eq(object2.toString(), userInfo.get(object2.toString()));
		}
		JSONArray array = appserver.dirty().page(idx, pageSize);
		JSONObject object = new JSONObject();
		object.put("totalSize",
				(int) Math.ceil((double) appserver.count() / pageSize));
		object.put("currentPage", idx);
		object.put("pageSize", pageSize);
		object.put("data", array);
		return resultMessage(object);
	}

	@SuppressWarnings("unchecked")
	public int setMeta(String appid, String ins) {
		String values = null;
		// 获取对应apps下的实例id
		JSONObject _obj = appserver.eq("id", appid).find();
		String[] value = _obj.get("meta").toString().split(",");
		if (ins.contains(",")) {
			String[] insid = ins.split(",");
			for (int i = 0; i < insid.length; i++) {
				if (!arrayContainField(value, insid[i])) {
					values = StringHelper
							.join(ArraysUtil.append(value, insid[i]));
				}
			}
		} else {
			if (!arrayContainField(value, ins)) {
				values = StringHelper.join(ArraysUtil.append(value, ins));
			}
		}
		JSONObject obj = new JSONObject();
		obj.put("meta", values);
		return appserver.eq("id", appid).data(obj).update() != null ? 0 : 99;
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

	public boolean arrayContainField(String[] array, String field) {
		return ArraysUtil.contains(array, field);
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
			msg = "必填字段为空!";
			break;
		case 2:
			msg = "该系统已存在!";
			break;
		default:
			msg = "其他操作异常";
			break;
		}
		return jGrapeFW_Message.netMSG(num, msg);
	}
}