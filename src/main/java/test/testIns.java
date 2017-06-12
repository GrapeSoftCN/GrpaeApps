package test;

import interfaceApplication.insServer;
import interfaceApplication.serviceServer;

public class testIns {

	public static void main(String[] args) {
		String insInfo = "{\"serviceName\":\"GrapeMenu\",\"serviceDescription\":\"菜单管理\",\"url\":\"123.57.214.226:801\"}";
		System.out.println(new serviceServer().servicesInsert(insInfo));
//		String insInfo= "{\"sysid\":13}";
//		System.out.println(new insServer().insPageBy(1, 1000, insInfo));
//		System.out.println(new serviceServer().servicesPage(1, 20));
//		System.out.println(new insServer().insDelete("43"));
//		String cname = "{\"cache\":\"redis\",\"other\":[],\"db\":\"mongodb\"}";
//		String string = "{\"configName\":"+cname+",\"sysid\":13,\"sid\":0,\"desp\":\"举报管理实例\"}";
//		System.out.println(new insServer().insInsert(string));
	}

}
