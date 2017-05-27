package model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import esayhelper.DBHelper;
import esayhelper.JSONHelper;
import esayhelper.formHelper;
import esayhelper.jGrapeFW_Message;
import esayhelper.formHelper.formdef;
import interfaceApplication.appsServer;
import jodd.util.ArraysUtil;

public class insModel {
	private static DBHelper ins;
	private static formHelper _form;
	private JSONObject _obj = new JSONObject();

	static {
		ins = new DBHelper("localdb", "ins", "id");
		_form = ins.getChecker();
	}

	public insModel() {
		_form.putRule("configName", formdef.notNull);
	}

	public String insert(JSONObject insInfo) {
		if (!_form.checkRuleEx(insInfo)) {
			return resultMessage(1, ""); // 非空字段验证
		}
		if (find(insInfo.get("sid").toString(),
				insInfo.get("configName").toString(),
				insInfo.get("sysid").toString()) != null) {
			return resultMessage(2, "");
		}
		String info = ins.data(insInfo).insertOnce().toString();
//		String message = new appsServer()
//				.appUpdateMeta(insInfo.get("sysid").toString(), info);
//		int code = 
//		if (JSONHelper.string2json(message).get("message").toString()) {
//			
//		}
		return getServiceName(find(info.toString())).toString();
	}

	public int update(String aid, JSONObject insInfo) {
		if (insInfo.containsKey("id")) {
			insInfo.remove("id");
		}
		return ins.eq("id", aid).data(insInfo).update() != null ? 0 : 99;
	}

	public int delete(String aid) {
		return ins.eq("id", aid).delete() != null ? 0 : 99;
	}

	public int delete(String[] ids) {
		ins.or();
		for (int i = 0; i < ids.length; i++) {
			ins.eq("id", ids[i]);
		}
		return ins.deleteAll() != ids.length ? 0 : 99;
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

	public JSONObject find(String sid, String configName, String sysid) {
		return ins.and().eq("sid", sid).eq("configName", configName)
				.eq("sysid", sysid).find();
	}

	public String search(String sysid) {
		return ins.eq("sysid", sysid).limit(20).select().toString();
	}

	@SuppressWarnings("unchecked")
	public JSONObject page(int idx, int pageSize) {
		JSONArray array = getServiceName(ins.page(idx, pageSize));
		JSONObject object = new JSONObject();
		object.put("totalSize",
				(int) Math.ceil((double) ins.count() / pageSize));
		object.put("currentPage", idx);
		object.put("pageSize", pageSize);
		object.put("data", array);
		return object;
	}

	@SuppressWarnings("unchecked")
	public JSONObject page(int idx, int pageSize, JSONObject userInfo) {
		for (Object object2 : userInfo.keySet()) {
			ins.eq(object2.toString(), userInfo.get(object2.toString()));
		}
		JSONArray array = getServiceName(ins.page(idx, pageSize));
		JSONObject object = new JSONObject();
		object.put("totalSize",
				(int) Math.ceil((double) ins.count() / pageSize));
		object.put("currentPage", idx);
		object.put("pageSize", pageSize);
		object.put("data", array);
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

	@SuppressWarnings("unchecked")
	public JSONArray getServiceName(JSONArray array) {
		JSONArray insInfo = new JSONArray();
		for (int i = 0; i < array.size(); i++) {
			JSONObject object = (JSONObject) array.get(i);
			object.put("configName", JSONHelper
					.string2json(object.get("configName").toString()));
			object.put("servicename",
					new ServiceModel().search(object.get("sid").toString())
							.get("serviceName").toString());
			insInfo.add(object);
		}
		return insInfo;
	}

	@SuppressWarnings("unchecked")
	public JSONObject getServiceName(JSONObject object) {
		JSONObject object2 = new ServiceModel()
				.search(object.get("sid").toString());
		if (object2 != null) {
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
		case 2:
			msg = "该服务下该实例已存在!";
			break;
		default:
			msg = "其他操作异常";
			break;
		}
		return jGrapeFW_Message.netMSG(num, msg);
	}
}
