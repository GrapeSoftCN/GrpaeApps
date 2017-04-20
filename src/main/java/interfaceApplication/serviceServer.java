package interfaceApplication;

import java.util.HashMap;

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.simple.JSONObject;

import esayhelper.JSONHelper;
import model.ServiceModel;
@SuppressWarnings("unchecked")
public class serviceServer {
	private ServiceModel model = new ServiceModel();
	private JSONObject _obj = new JSONObject();
	private HashMap<String, Object> map = new HashMap<>();
	public serviceServer() {
		map.put("appid", model.getID());
		map.put("serviceName", "");
		map.put("serviceDescription", "");
		map.put("cache", 0);
		map.put("url", "");
		map.put("port", 0);
		map.put("debug", 0);
		map.put("groupid", 0);
		map.put("state", 0);
		map.put("useProtocol", 0);
	}
	public String servicesInsert(String serviceInfo){
		JSONObject object =model.AddMap(map, JSONHelper.string2json(serviceInfo));
		_obj.put("records", model.insert(object));
		return model.resultMessage(0, _obj.toString());
	}
	public String servicesUpdate(String id,String serviceInfo){
		return model.resultMessage(model.update(id,JSONHelper.string2json(serviceInfo)), "修改微服务成功");
	}
	public String servicesDelete(String id){
		return model.resultMessage(model.delete(id), "删除微服务成功");
	}
	public String servicesDeleteBatch(String ids){
		return model.resultMessage(model.delete(ids.split(",")), "删除微服务成功");
	}
	public String servicesSearch(String serviceInfo){
		_obj.put("records", model.search(JSONHelper.string2json(serviceInfo)));
		return StringEscapeUtils.unescapeJava(model.resultMessage(0, _obj.toString()));
	}
//	public String servicesFind(String sid){
//		_obj.put("record", model.search(sid));
//		return StringEscapeUtils.unescapeJava(model.resultMessage(0, _obj.toString()));
//	}
	public String servicesPage(int idx,int pageSize){
		_obj.put("records", model.page(idx, pageSize));
		return StringEscapeUtils.unescapeJava(model.resultMessage(0, _obj.toString()));
	}
	public String servicesPageBy(int idx,int pageSize,String serviceInfo){
		_obj.put("records", model.page(idx, pageSize,JSONHelper.string2json(serviceInfo)));
		return StringEscapeUtils.unescapeJava(model.resultMessage(0, _obj.toString()));
	}
	public String servicesDebug(String id,int num){
		return model.resultMessage(model.setDebug(id, num), "设置模式成功");
	}
	public String servicesState(String id,int num){
		return model.resultMessage(model.setState(id, num), "设置状态成功");
	}
}
