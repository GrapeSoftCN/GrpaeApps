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
	private JSONObject _obj = new JSONObject();
	// private static formHelper _form;

	static {
		appinterface = new DBHelper("localdb", "classinterface", "id");
		// _form = appinterface.getChecker();
	}

	public String insert(JSONObject interfaceInfo) {
		String info = appinterface.data(interfaceInfo).insertOnce().toString();
		return resultMessage(find(info));
	}

	public int update(String aid, JSONObject interfaceInfo) {
		if (interfaceInfo.containsKey("id")) {
			interfaceInfo.remove("id");
		}
		return appinterface.eq("id", aid).data(interfaceInfo).update() != null
				? 0 : 99;
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

	public String search(JSONObject appinterfaceInfo) {
		for (Object object2 : appinterfaceInfo.keySet()) {
			appinterface.eq(object2.toString(),
					appinterfaceInfo.get(object2.toString()));
		}
		JSONArray array = appinterface.limit(20).select();
		return resultMessage(array);
	}

	public String search(String classid) {
		JSONArray array = appinterface.eq("appclsid", classid).limit(20)
				.select();
		return resultMessage(array);
	}

	public JSONObject find(String id) {
		return appinterface.eq("id", id).find();
	}

	@SuppressWarnings("unchecked")
	public String page(int idx, int pageSize) {
		JSONArray array = appinterface.page(idx, pageSize);
		JSONObject object = new JSONObject();
		object.put("totalSize",
				(int) Math.ceil((double) appinterface.count() / pageSize));
		object.put("currentPage", idx);
		object.put("pageSize", pageSize);
		object.put("data", array);
		return resultMessage(object);
	}

	@SuppressWarnings("unchecked")
	public String page(int idx, int pageSize, JSONObject userInfo) {
		for (Object object2 : userInfo.keySet()) {
			appinterface.eq(object2.toString(),
					userInfo.get(object2.toString()));
		}
		JSONArray array = appinterface.dirty().page(idx, pageSize);
		JSONObject object = new JSONObject();
		object.put("totalSize",
				(int) Math.ceil((double) appinterface.count() / pageSize));
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
	private String resultMessage(JSONObject object) {
		_obj.put("records", object);
		return resultMessage(0, _obj.toString());
	}

	@SuppressWarnings("unchecked")
	private String resultMessage(JSONArray array) {
		_obj.put("records", array);
		return resultMessage(0, _obj.toString());
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
