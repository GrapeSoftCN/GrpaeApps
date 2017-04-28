package model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import esayhelper.DBHelper;
import esayhelper.jGrapeFW_Message;

public class interfaceModel {
	private static DBHelper appinterface;
	// private static formHelper _form;

	static {
		appinterface = new DBHelper("localdb", "classinterface", "id");
		// _form = appinterface.getChecker();
	}

	public String insert(JSONObject interfaceInfo) {
		if (interfaceInfo.containsKey("id")) {
			interfaceInfo.remove("id");
		}
		Object info = appinterface.data(interfaceInfo).insertOnce();
		return find(info.toString()).toString();
	}

	public int update(String aid, JSONObject interfaceInfo) {
		if (interfaceInfo.containsKey("id")) {
			interfaceInfo.remove("id");
		}
		return appinterface.eq("id", aid).data(interfaceInfo).update() != null ? 0 : 99;
	}

	public int delete(String aid) {
		return appinterface.eq("id", aid).delete() != null ? 0 : 99;
	}

	public int delete(String[] ids) {
		appinterface.or();
		for (int i = 0; i < ids.length; i++) {
			appinterface.eq("id", ids[i]);
		}
		return appinterface.deleteAll() != ids.length ? 0 : 99;
	}

	public JSONArray search(JSONObject appinterfaceInfo) {
		for (Object object2 : appinterfaceInfo.keySet()) {
			appinterface.eq(object2.toString(), appinterfaceInfo.get(object2.toString()));
		}
		return appinterface.limit(20).select();
	}

	public JSONArray search(String classid) {
		return appinterface.eq("appclsid", classid).limit(20).select();
	}
	public JSONObject find(String id) {
		return appinterface.eq("id", id).find();
	}

	@SuppressWarnings("unchecked")
	public JSONObject page(int idx, int pageSize) {
		JSONArray array = appinterface.page(idx, pageSize);
		JSONObject object = new JSONObject();
		object.put("totalSize", (int) Math.ceil((double) appinterface.count() / pageSize));
		object.put("currentPage", idx);
		object.put("pageSize", pageSize);
		object.put("data", array);
		return object;
	}

	@SuppressWarnings("unchecked")
	public JSONObject page(int idx, int pageSize, JSONObject userInfo) {
		for (Object object2 : userInfo.keySet()) {
			appinterface.eq(object2.toString(), userInfo.get(object2.toString()));
		}
		JSONArray array = appinterface.page(idx, pageSize);
		JSONObject object = new JSONObject();
		object.put("totalSize", (int) Math.ceil((double) appinterface.count() / pageSize));
		object.put("currentPage", idx);
		object.put("pageSize", pageSize);
		object.put("data", array);
		return object;
	}
	/**
	 * 将map添加至JSONObject
	 * @param map
	 * @param object
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public JSONObject AddMap(HashMap<String, Object> map,JSONObject object) {
		if (map.entrySet()!=null) {
			Iterator<Entry<String, Object>> iterator = map.entrySet().iterator();
			while (iterator.hasNext()) {
				Map.Entry<String, Object> entry = (Map.Entry<String, Object>) iterator.next();
				if (!object.containsKey(entry.getKey())) {
					object.put(entry.getKey(), entry.getValue());
				}
			}
		}
		return object;
	}
	public String resultMessage(int num, String message) {
		String msg = "";
		switch (num) {
		case 0:
			msg = message;
			break;
		default:
			msg = "其他操作异常";
			break;
		}
		return jGrapeFW_Message.netMSG(num, msg);
	}
}
