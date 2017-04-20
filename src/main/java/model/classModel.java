package model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import esayhelper.DBHelper;
import esayhelper.jGrapeFW_Message;

public class classModel {
	private static DBHelper appclass;

	static {
		appclass = new DBHelper("localdb", "appclass", "id");
	}

	public String insert(JSONObject appclassInfo) {
		if (appclassInfo.containsKey("id")) {
			appclassInfo.remove("id");
		}
			Object info = appclass.data(appclassInfo).insertOnce();
			return find(info.toString()).toString();
	}

	public int update(String aid, JSONObject appclassInfo) {
		if (appclassInfo.containsKey("id")) {
			appclassInfo.remove("id");
		}
		return appclass.eq("id", aid).data(appclassInfo).update() != null ? 0 : 99;
	}

	public int delete(String aid) {
		return appclass.eq("id", aid).delete() != null ? 0 : 99;
	}

	public int delete(String[] ids) {
		appclass = (DBHelper) appclass.or();
		for (int i = 0; i < ids.length; i++) {
			appclass.eq("id", ids[i]);
		}
		return appclass.delete() != null ? 0 : 99;
	}

	public JSONArray search(JSONObject appclassInfo) {
		for (Object object2 : appclassInfo.keySet()) {
			appclass.eq(object2.toString(), appclassInfo.get(object2.toString()));
		}
		return appclass.select();
	}
	public JSONArray search(String sysid) {
		return appclass.eq("sid", sysid).select();
	}
	public JSONObject find(String clsid) {
		return appclass.eq("id", clsid).find();
	}

	public JSONObject page(int idx, int pageSize) {
		JSONArray array = appclass.page(idx, pageSize);
		@SuppressWarnings("unchecked")
		JSONObject object = new JSONObject() {
			private static final long serialVersionUID = 1L;
			{
				put("totalSize", (int) Math.ceil((double) appclass.count() / pageSize));
				put("currentPage", idx);
				put("pageSize", pageSize);
				put("data", array);
			}
		};
		return object;
	}

	public JSONObject page(int idx, int pageSize, JSONObject userInfo) {
		for (Object object2 : userInfo.keySet()) {
			appclass.eq(object2.toString(), userInfo.get(object2.toString()));
		}
		JSONArray array = appclass.page(idx, pageSize);
		@SuppressWarnings("unchecked")
		JSONObject object = new JSONObject() {
			private static final long serialVersionUID = 1L;
			{
				put("totalSize", (int) Math.ceil((double) appclass.count() / pageSize));
				put("currentPage", idx);
				put("pageSize", pageSize);
				put("data", array);
			}
		};
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
