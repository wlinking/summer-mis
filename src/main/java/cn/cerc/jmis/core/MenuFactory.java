package cn.cerc.jmis.core;

import java.io.File;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class MenuFactory {
	// private static final Logger log = Logger.getLogger(MenuFactory.class);
	private static final String menuFile = "app-menus.xml";
	private static final Map<String, MenuItem> menus = new LinkedHashMap<>();

	static {
		try {
			menus.clear();
			String filepath = MenuFactory.class.getClassLoader().getResource("").toURI().getPath() + menuFile;
			File f = new File(filepath);
			SAXReader reader = new SAXReader();
			Document doc = reader.read(f);

			Element body = doc.getRootElement().element("body");
			if (body == null)
				throw new RuntimeException(menuFile + " 格式不正确！");

			for (Iterator<?> j = body.elementIterator("item"); j.hasNext();) {
				Element item = (Element) j.next();

				if (!"true".equals(item.attributeValue("delete"))) {
					MenuItem menuItem = new MenuItem();
					menuItem.setId(item.attributeValue("code"));
					menuItem.setCaption(item.attributeValue("name"));
					if (item.attributeValue("security") != null)
						menuItem.setSecurity("true".equals(item.attributeValue("security")));
					else
						menuItem.setSecurity(true);
					menuItem.setHide("true".equals(item.attributeValue("hide")));
					menuItem.setFolder("true".equals(item.attributeValue("folder")));
					menuItem.setCustom("true".equals(item.attributeValue("custom")));
					menuItem.setProccode(item.attributeValue("proccode"));
					menuItem.setParent(menuItem.getHide() ? "hidden" : item.attributeValue("parent"));

					Element remark = item.element("remark");
					menuItem.setDescribe(remark != null ? remark.getText() : "");

					menuItem.setWin(true);
					Element web = item.element("web");
					if (web != null) {
						menuItem.setClazz(web.attributeValue("class"));
						menuItem.setFormNo(web.attributeValue("pageno"));
						menuItem.setImage(web.attributeValue("image"));
						menuItem.setProcess(web.attributeValue("process"));
						menuItem.setWeb(!"none".equals(menuItem.getProcess()));
						menuItem.setWin(!"release".equals(menuItem.getProcess()));
					}

					Element access = item.element("access");
					menuItem.setVersions(access.attributeValue("verlist"));
					menus.put(menuItem.getId(), menuItem);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Map<String, MenuItem> getItems() {
		return menus;
	}

	public static MenuItem get(String beanID) {
		return menus.get(beanID);
	}
}