package interfaceApplication;

import java.util.HashMap;

import org.json.simple.JSONObject;

import esayhelper.JSONHelper;
import model.classModel;

public class classServer {
	private classModel model = new classModel();
	private HashMap<String, Object> map = new HashMap<>();

	public classServer() {
		map.put("classname", "");
		map.put("sid", 0);
	}

	public String classInsert(String classInfo) {
		JSONObject object = model.AddMap(map,
				JSONHelper.string2json(classInfo));
		return model.insert(object);
	}

	public String classUpdate(String id, String classInfo) {
		return model.resultMessage(
				model.update(id, JSONHelper.string2json(classInfo)), "修改类成功");
	}

	public String classDelete(String id) {
		return model.resultMessage(model.delete(id), "删除类成功");
	}

	public String classDeleteBatch(String ids) {
		return model.resultMessage(model.delete(ids.split(",")), "批量删除类成功");
	}

	public String classSearch(String classInfo) {
		return model.search(JSONHelper.string2json(classInfo));
	}

	public String classFind(String sid) {
		return model.search(sid);
	}

	public String classPage(int idx, int pageSize) {
		return model.page(idx, pageSize);
	}

	public String classPageBy(int idx, int pageSize, String classInfo) {
		return model.page(idx, pageSize, JSONHelper.string2json(classInfo));
	}
}
