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
import jodd.util.ArraysUtil;

public class insModel {
	private static DBHelper ins;
	private static formHelper _form;
	static {
		ins = new DBHelper("localdb", "ins");
		_form = ins.getChecker();
	}

	public insModel() {
		_form.putRule("configName", formdef.notNull);
	}

	public String insert(JSONObject insInfo) {
		if (!_form.checkRuleEx(insInfo)) {
			return resultMessage(1, ""); // 非空字段验证
		}else{
			Object info = ins.data(insInfo).insertOnce();
			return getServiceName(find(info.toString())).toString();
		}
		// return ins.data(insInfo).insertOnce() != null ? 0 : 99;
	}

	public int update(String aid, JSONObject insInfo) {
		// if (insInfo.containsKey("configname")) {
		// if (!_form.checkRuleEx(insInfo)) {
		// return 1; // 非空字段验证
		// }
		// }
		if (insInfo.containsKey("id")) {
			insInfo.remove("id");
		}
		return ins.eq("id", aid).data(insInfo).update() != null ? 0 : 99;
	}

	public int delete(String aid) {
		return ins.eq("id", aid).delete() != null ? 0 : 99;
	}

	public int delete(String[] ids) {
		ins = (DBHelper) ins.or();
		for (int i = 0; i < ids.length; i++) {
			ins.eq("id", ids[i]);
		}
		return ins.delete() != null ? 0 : 99;
	}

	public JSONArray search(JSONObject insInfo) {
		for (Object object2 : insInfo.keySet()) {
			ins.eq(object2.toString(), insInfo.get(object2.toString()));
		}
		return getServiceName(ins.limit(20).select());
	}

	public JSONArray search(String[] meta) {
		ins = (DBHelper) ins.or();
		for (int i = 0; i < meta.length; i++) {
			ins.eq("id", meta[i]);
		}
		return getServiceName(ins.limit(20).select());
	}

	public JSONObject find(String insid) {
		return ins.eq("id", insid).find();
	}

	public String search(String sysid) {
		return ins.eq("sysid", sysid).limit(20).select().toString();
	}

	public JSONObject page(int idx, int pageSize) {
		JSONArray array = getServiceName(ins.page(idx, pageSize));
		@SuppressWarnings("unchecked")
		JSONObject object = new JSONObject() {
			private static final long serialVersionUID = 1L;
			{
				put("totalSize", (int) Math.ceil((double) ins.count() / pageSize));
				put("currentPage", idx);
				put("pageSize", pageSize);
				put("data", array);
			}
		};
		return object;
	}

	public JSONObject page(int idx, int pageSize, JSONObject userInfo) {
		for (Object object2 : userInfo.keySet()) {
			ins.eq(object2.toString(), userInfo.get(object2.toString()));
		}
		JSONArray array = getServiceName(ins.page(idx, pageSize));
		@SuppressWarnings("unchecked")
		JSONObject object = new JSONObject() {
			private static final long serialVersionUID = 1L;
			{
				put("totalSize", (int) Math.ceil((double) ins.count() / pageSize));
				put("currentPage", idx);
				put("pageSize", pageSize);
				put("data", array);
			}
		};
		return object;
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

	public boolean arrayContainField(String[] array, String field) {
		return ArraysUtil.contains(array, field);
	}

	@SuppressWarnings("unchecked")
	public JSONArray getServiceName(JSONArray array) {
		JSONArray insInfo = new JSONArray();
		for (int i = 0; i < array.size(); i++) {
			JSONObject object = (JSONObject) array.get(i);
			object.put("servicename", new ServiceModel().search(object.get("sid").toString())
					.get("serviceName").toString());
			insInfo.add(object);
		}
		return insInfo;
	}
	@SuppressWarnings("unchecked")
	public JSONObject getServiceName(JSONObject object) {
		JSONObject object2 = new ServiceModel().search(object.get("sid").toString());
		if (object2!=null) {
			object.put("servicename", object2.get("serviceName").toString());
		}
		return object;
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
		default:
			msg = "其他操作异常";
			break;
		}
		return jGrapeFW_Message.netMSG(num, msg);
	}
}
