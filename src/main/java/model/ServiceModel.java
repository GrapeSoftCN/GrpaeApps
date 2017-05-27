package model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import esayhelper.DBHelper;
import esayhelper.formHelper;
import esayhelper.jGrapeFW_Message;
import esayhelper.formHelper.formdef;

public class ServiceModel {
	private static DBHelper service;
	private static formHelper _form;
	private JSONObject _obj = new JSONObject();

	static {
		service = new DBHelper("localdb", "services", "id");
		_form = service.getChecker();
	}

	public ServiceModel() {
		_form.putRule("serviceName", formdef.notNull);
	}

	public String insert(JSONObject serviceInfo) {
		if (!_form.checkRuleEx(serviceInfo)) {
			return resultMessage(1, ""); // 非空字段验证
		}
		String info = service.data(serviceInfo).insertOnce().toString();
		return resultMessage(search(info));
	}

	public int update(String aid, JSONObject serviceInfo) {
		if (serviceInfo.containsKey("id")) {
			serviceInfo.remove("id");
		}
		if (serviceInfo.containsKey("ctime")) {
			serviceInfo.remove("ctime");
		}
		return service.eq("id", aid).data(serviceInfo).update() != null ? 0
				: 99;
	}

	public int delete(String aid) {
		return service.eq("id", aid).delete() != null ? 0 : 99;
	}

	public int delete(String[] ids) {
		service.or();
		for (int i = 0; i < ids.length; i++) {
			service.eq("id", ids[i]);
		}
		return service.deleteAll() != ids.length ? 0 : 99;
	}

	public String search(JSONObject serviceInfo) {
		for (Object object2 : serviceInfo.keySet()) {
			service.eq(object2.toString(), serviceInfo.get(object2.toString()));
		}
		JSONArray array = service.limit(20).select();
		return resultMessage(array);
	}

	public JSONObject search(String sid) {
		return service.eq("id", sid).find();
	}

	@SuppressWarnings("unchecked")
	public String page(int idx, int pageSize) {
		JSONArray array = service.page(idx, pageSize);
		JSONObject object = new JSONObject();
		object.put("totalSize",
				(int) Math.ceil((double) service.count() / pageSize));
		object.put("currentPage", idx);
		object.put("pageSize", pageSize);
		object.put("data", array);
		return resultMessage(object);
	}

	@SuppressWarnings("unchecked")
	public String page(int idx, int pageSize, JSONObject userInfo) {
		for (Object object2 : userInfo.keySet()) {
			service.eq(object2.toString(), userInfo.get(object2.toString()));
		}
		JSONArray array = service.dirty().page(idx, pageSize);
		JSONObject object = new JSONObject();
		object.put("totalSize",
				(int) Math.ceil((double) service.count() / pageSize));
		object.put("currentPage", idx);
		object.put("pageSize", pageSize);
		object.put("data", array);
		return resultMessage(object);
	}

	@SuppressWarnings("unchecked")
	public int setDebug(String id, int num) {
		JSONObject obj = new JSONObject();
		obj.put("debug", num);
		return service.eq("id", id).data(obj).update() != null ? 0 : 99;
	}

	@SuppressWarnings("unchecked")
	public int setState(String id, int num) {
		JSONObject obj = new JSONObject();
		obj.put("state", num);
		return service.eq("id", id).data(obj).update() != null ? 0 : 99;
	}

	public String getID() {
		String str = UUID.randomUUID().toString();
		return str.replace("-", "");
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
			msg = "必填项为空";
			break;
		default:
			msg = "其他操作异常";
			break;
		}
		return jGrapeFW_Message.netMSG(num, msg);
	}
}
