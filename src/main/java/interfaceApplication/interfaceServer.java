package interfaceApplication;

import java.util.HashMap;

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.simple.JSONObject;

import esayhelper.JSONHelper;
import model.interfaceModel;

@SuppressWarnings("unchecked")
public class interfaceServer {
	private interfaceModel model = new interfaceModel();
	private JSONObject _obj = new JSONObject();
	private HashMap<String, Object> map = new HashMap<>();
	public interfaceServer() {
		map.put("interface", "");
		map.put("readtime", 0);
		map.put("desp", "");
		map.put("appclsid", 0);
	}
	public String interfaceInsert(String interfaceInfo) {
		JSONObject object =model.AddMap(map, JSONHelper.string2json(interfaceInfo));
		_obj.put("records",StringEscapeUtils.unescapeJava(model.insert(object)));
		return model.resultMessage(0,_obj.toString());
	}

	public String interfaceUpdate(String id, String interfaceInfo) {
		return model.resultMessage(model.update(id, JSONHelper.string2json(interfaceInfo)),
				"修改接口成功");
	}

	public String interfaceDelete(String id) {
		return model.resultMessage(model.delete(id), "删除接口成功");
	}

	public String interfaceDeleteBatch(String ids) {
		return model.resultMessage(model.delete(ids.split(",")), "删除接口成功");
	}

	public String interfaceSearch(String interfaceInfo) {
		_obj.put("records", model.search(JSONHelper.string2json(interfaceInfo)));
		return StringEscapeUtils.unescapeJava(model.resultMessage(0, _obj.toString()));
	}

	public String interfaceFind(String clsid) {
		_obj.put("records", model.search(clsid));
		return StringEscapeUtils.unescapeJava(model.resultMessage(0, _obj.toString()));
	}

	public String interfacePage(int idx, int pageSize) {
		_obj.put("records", model.page(idx, pageSize));
		return StringEscapeUtils.unescapeJava(model.resultMessage(0, _obj.toString()));
	}

	public String interfacePageBy(int idx, int pageSize, String interfaceInfo) {
		_obj.put("records", model.page(idx, pageSize, JSONHelper.string2json(interfaceInfo)));
		return StringEscapeUtils.unescapeJava(model.resultMessage(0, _obj.toString()));
	}
}
