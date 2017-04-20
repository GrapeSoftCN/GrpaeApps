package interfaceApplication;

import java.util.HashMap;

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.simple.JSONObject;

import esayhelper.JSONHelper;
import model.AppModel;

@SuppressWarnings("unchecked")
public class appsServer {
	private AppModel model = new AppModel();
	private JSONObject _obj = new JSONObject();
	private HashMap<String, Object> map = new HashMap<>();
	public appsServer() {
		map.put("meta", "0");
		map.put("name", "0");
		map.put("desp", "0");
		map.put("uid", "0");
		map.put("domain", "0");
	}

	public String appInsert(String appInfo) {
		JSONObject object = model.AddMap(map, JSONHelper.string2json(appInfo));
		_obj.put("records", StringEscapeUtils.unescapeJava(model.insert(object)));
		return model.resultMessage(0, _obj.toString());
	}

	public String appUpdate(String appid, String appInfo) {
		return model.resultMessage(model.update(appid, JSONHelper.string2json(appInfo)),
				"修改apps成功");
	}

	public String appDelete(String appid) {
		return model.resultMessage(model.delete(appid), "删除apps成功");
	}

	public String appDeleteBatch(String ids) {
		return model.resultMessage(model.delete(ids.split(",")), "批量删除apps成功");
	}

	public String appSearch(String appInfo) {
		_obj.put("records", model.search(JSONHelper.string2json(appInfo)));
		return StringEscapeUtils.unescapeJava(model.resultMessage(0, _obj.toString()));
	}

	public String appPage(int idx, int pageSize) {
		_obj.put("records", model.page(idx, pageSize));
		return StringEscapeUtils.unescapeJava(model.resultMessage(0, _obj.toString()));
	}

	public String appPageBy(int idx, int pageSize, String appInfo) {
		_obj.put("records", model.page(idx, pageSize, JSONHelper.string2json(appInfo)));
		return StringEscapeUtils.unescapeJava(model.resultMessage(0, _obj.toString()));
	}

	public String appUpdateMeta(String appid, String ins) {
		return model.resultMessage(model.setMeta(appid, ins), "设置实例成功");
	}
}