package interfaceApplication;

import java.util.HashMap;

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.simple.JSONObject;

import esayhelper.JSONHelper;
import model.classModel;

@SuppressWarnings("unchecked")
public class classServer {
	private classModel model = new classModel();
	private JSONObject _obj = new JSONObject();
	private HashMap<String, Object> map = new HashMap<>();
	public classServer() {
		map.put("classname", "");
		map.put("sid", 0);
	}
	public String classInsert(String classInfo) {
		JSONObject object = model.AddMap(map, JSONHelper.string2json(classInfo));
		_obj.put("records", StringEscapeUtils.unescapeJava(model.insert(object)));
		return model.resultMessage(0, _obj.toString());
	}

	public String classUpdate(String id, String classInfo) {
		return model.resultMessage(model.update(id, JSONHelper.string2json(classInfo)),
				"修改类成功");
	}

	public String classDelete(String id) {
		return model.resultMessage(model.delete(id), "删除类成功");
	}

	public String classDeleteBatch(String ids) {
		return model.resultMessage(model.delete(ids.split(",")), "批量删除类成功");
	}

	public String classSearch(String classInfo) {
		_obj.put("records", model.search(JSONHelper.string2json(classInfo)));
		return StringEscapeUtils.unescapeJava(model.resultMessage(0, _obj.toString()));
	}

	public String classFind(String sid) {
		_obj.put("records", model.search(sid));
		return StringEscapeUtils.unescapeJava(model.resultMessage(0, _obj.toString()));
	}

	public String classPage(int idx, int pageSize) {
		_obj.put("records", model.page(idx, pageSize));
		return StringEscapeUtils.unescapeJava(model.resultMessage(0, _obj.toString()));
	}

	public String classPageBy(int idx, int pageSize, String classInfo) {
		_obj.put("records", model.page(idx, pageSize, JSONHelper.string2json(classInfo)));
		return StringEscapeUtils.unescapeJava(model.resultMessage(0, _obj.toString()));
	}
}
