package interfaceApplication;

import java.util.HashMap;

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.simple.JSONObject;

import esayhelper.JSONHelper;
import model.insModel;
@SuppressWarnings("unchecked")
public class insServer {
	private insModel model = new insModel();
	private JSONObject object = new JSONObject();
	private HashMap<String, Object> map = new HashMap<>();
	public insServer() {
		map.put("configName", "");
		map.put("sysid", 0);
		map.put("sid", 0);
		map.put("desp", "");
	}
	public String insInsert(String insInfo) {
		JSONObject o = model.AddMap(map, JSONHelper.string2json(insInfo));
		object.put("records", JSONHelper.string2json(model.insert(o)));
		return StringEscapeUtils.unescapeJava(model.resultMessage(0, object.toString()));
	}
	public String insUpdate(String insid,String insInfo) {
		return model.resultMessage(model.update(insid, JSONHelper.string2json(insInfo)), "修改实例成功");
	}
	public String insDelete(String insInfo) {
		return model.resultMessage(model.delete(insInfo), "删除实例成功");
	}
	public String insDeleteBatch(String insInfo) {
		return model.resultMessage(model.delete(insInfo.split(",")), "批量删除实例成功");
	}
	public String insSearch(String insInfo) {
		object.put("records", model.search(JSONHelper.string2json(insInfo)));
		return StringEscapeUtils.unescapeJava(model.resultMessage(0, object.toString()));
	}
	public String insSearchBatch(String insInfo) {
		object.put("records", model.search(insInfo.split(",")));
		return StringEscapeUtils.unescapeJava(model.resultMessage(0, object.toString()));
	}
//	public String insSearchByMeta(String insInfo) {
//		return model.resultMessage(model.insert(JSONHelper.string2json(insInfo)), "新增实例成功");
//	}

	public String insPage(int idx,int pageSize) {
		object.put("records", model.page(idx, pageSize));
		return StringEscapeUtils.unescapeJava(model.resultMessage(0, object.toString()));
	}
	public String insPageBy(int idx,int pageSize,String insInfo) {
		object.put("records", model.page(idx, pageSize,JSONHelper.string2json(insInfo)));
		return model.resultMessage(0, object.toString());
	}
}
