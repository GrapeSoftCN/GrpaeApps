package interfaceApplication;

import java.util.HashMap;

import org.json.simple.JSONObject;

import esayhelper.JSONHelper;
import model.AppModel;

public class appsServer {
	private AppModel model = new AppModel();
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
		return model.resultMessage(0, model.insert(object));
	}

	public String appUpdate(String appid, String appInfo) {
		return model.resultMessage(
				model.update(appid, JSONHelper.string2json(appInfo)),
				"修改apps成功");
	}

	public String appDelete(String appid) {
		return model.resultMessage(model.delete(appid), "删除apps成功");
	}

	public String appDeleteBatch(String ids) {
		return model.resultMessage(model.delete(ids.split(",")), "批量删除apps成功");
	}

	public String appSearch(String appInfo) {
		return model.search(JSONHelper.string2json(appInfo));
	}

	public String appPage(int idx, int pageSize) {
		return model.page(idx, pageSize);
	}

	public String appPageBy(int idx, int pageSize, String appInfo) {
		return model.page(idx, pageSize, JSONHelper.string2json(appInfo));
	}

	public String appUpdateMeta(String appid, String ins) {
		return model.resultMessage(model.setMeta(appid, ins), "设置实例成功");
	}
}