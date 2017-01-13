package prowiki;

import de.tudarmstadt.ukp.wikipedia.api.Category;

import de.tudarmstadt.ukp.wikipedia.api.DatabaseConfiguration;
import de.tudarmstadt.ukp.wikipedia.api.Page;
import de.tudarmstadt.ukp.wikipedia.api.Title;
import de.tudarmstadt.ukp.wikipedia.api.WikiConstants.Language;
import de.tudarmstadt.ukp.wikipedia.api.Wikipedia;
import de.tudarmstadt.ukp.wikipedia.api.exception.WikiTitleParsingException;

import static de.tudarmstadt.ukp.wikipedia.api.WikiConstants.LF;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.ListModel;
import javax.swing.Painter;

import org.apache.commons.collections.map.StaticBucketMap;
import org.apache.commons.collections4.Put;
import org.omg.CORBA.PUBLIC_MEMBER;

import java.lang.*;
import java.lang.ProcessBuilder.Redirect;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.SQLException;
import prowiki.ConnectDatabase.exactpage;

public class ShowPageInfo {
	public static HashMap<String,Object>usepage = new HashMap<String,Object>();
	public static HashSet<String> nopage = new HashSet<String>();
	public static void main(String[] args) throws Exception {
		// 数据库连接参数配置
		DatabaseConfiguration dbConfig = new DatabaseConfiguration();
		dbConfig.setHost("localhost"); // 主机名
		dbConfig.setDatabase("wikidata"); // 数据库名
		dbConfig.setUser("root"); // 访问数据库的用户名
		dbConfig.setPassword(""); // 访问数据库的密码
		dbConfig.setLanguage(Language.english);
		
		//另一个数据库
		ConnectDatabase cd = new ConnectDatabase();
		exactpage exactpage = cd.new exactpage();
		cd.ConnectMysql();
		
		// 创建Wikipedia处理对象
		Wikipedia wiki = new Wikipedia(dbConfig);
		List<String> messageall = readFileByLines("raw_file.txt");
		int cntmessage = 0;
		int pageflag = 0;
		int pagecnt = 17543;
		nopage.clear();
		usepage.clear();
		
		for (Iterator<String> it = messageall.iterator(); it.hasNext();) {
			++cntmessage;
			System.out.println("运行到  " + cntmessage);
			String[] alltitle = it.next().split(" ");
			
			Map<String, Object> mapres = new HashMap<String, Object>();
		    JSONObject jsonObj = JSONObject.fromObject(mapres);
			JSONObject titlejson = JSONObject.fromObject(mapres);
			List<JSONObject>messagelist = new ArrayList<JSONObject>();
			mapres.clear();
			messagelist.clear();
			pageflag = 0;
			//titlejson = null;
			//messagelist = null;
			
			for (int i = 0; i < alltitle.length; i++) {
				// System.out.print(alltitle[i] + " ");
				String title = alltitle[i];
				if(i > 0){
					int j = i + 1;
					int k = i + 2;
					if(k < alltitle.length){
						String tmptitle = title + " " + alltitle[j] + " " + alltitle[k];
						String tmptitle2 = title + " " + alltitle[j];
						if(nopage.contains(tmptitle) == false && wiki.existsPage(tmptitle) == true){
							title = tmptitle;
							i+=2;
						}
						else if(nopage.contains(tmptitle2) == false && wiki.existsPage(tmptitle2) == true){
							title = tmptitle2;
							i+=1;
						}
						else{
							if(nopage.contains(tmptitle) == false) nopage.add(tmptitle);
							if(nopage.contains(tmptitle2) == false) nopage.add(tmptitle2);
						}
					}
					else if(j < alltitle.length){
						String tmptitle = title + " " + alltitle[j];
						if(nopage.contains(tmptitle) == false && wiki.existsPage(tmptitle) == true){
							title = tmptitle;
							i+=1;
						}
						else if(nopage.contains(tmptitle) == false){
							nopage.add(title);
						}
					}
				}
				System.out.println(title);
				if(nopage.contains(title) == true) continue;
				if(usepage.containsKey(title) == true){
					//System.out.println(usepage.get(title));
					mapres.put(title, usepage.get(title));
					jsonObj = JSONObject.fromObject(mapres);
					messagelist.add(jsonObj);
					continue;
				}
				try {
					Set<Page> allpage = wiki.getPages(title);
					if(allpage.size() > 1 ) continue;
					for (Iterator<Page> pageit = allpage.iterator(); pageit.hasNext();) {
						Page page = pageit.next();
						//Page page = wiki.getPage(title);
						if (page.isDisambiguation() == false && page.isDiscussion() == false) {
							pageflag = 1;
							//System.out.println("pageflag =   &&" + pageflag);
							jsonObj = pageprocess_direct(page);
							//System.out.println(jsonObj);
							if(i == 0){
								titlejson = jsonObj;
							}
							else{
								//System.out.println(jsonObj);
								messagelist.add(jsonObj);
							}
						} else if (page.isDisambiguation() == true && page.isDiscussion() == false) {
							continue;
							//pageprocess_disam(page);
						} else if (page.isDiscussion() == true) {
							System.out.println("discussion: " + title);
							continue;
							//pageprocess_diss(page);
						}

					}
				} catch (Exception e) {
					// TODO: handle exception
					System.out.println("error find page " + title);
					nopage.add(title);
					continue;
				}
			}
			if(pageflag == 0) continue;
			++pagecnt;
			//System.out.print(pagecnt);
			exactpage.setId(pagecnt);
			exactpage.setTitle(alltitle[0]);
			exactpage.setTitleWiki(titlejson);
			String tmp = "";
			for(int tmpi = 1; tmpi < alltitle.length;tmpi++)
				tmp+=alltitle[tmpi]+" ";
			exactpage.setMessage(tmp);
			exactpage.setMessageWiki(messagelist);
			
			//System.out.println(messagelist);
			cd.InsertSql(exactpage);
		}
		// System.out.println(messageall.size());
	}

	private static void pageprocess_diss(Page page) {
		// TODO Auto-generated method stub

	}

	private static void pageprocess_disam(Page page) {
		// TODO Auto-generated method stub

	}

	private static JSONObject pageprocess_direct(Page page) throws WikiTitleParsingException {
		// TODO Auto-generated method stub

		ArrayList<String> listredirect = new ArrayList<String>();
		ArrayList<String> listinlink = new ArrayList<String>();
		ArrayList<String> listoutlink = new ArrayList<String>();
		ArrayList<String> listcategory = new ArrayList<String>();
		try {
			for (String redirect : page.getRedirects()) {
				listredirect.add(new Title(redirect).getPlainTitle());
			}
			//System.out.println("fasdf");
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("error redirect");
		}
		try {
			//System.out.println(page.getNumberOfInlinks());
			for (Page inLinkPage : page.getInlinks()) {
				listinlink.add(inLinkPage.getTitle().toString());
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("error inlink");
		}
		try {
			for (Page outLinkPage : page.getOutlinks()) {
				listoutlink.add(outLinkPage.getTitle().toString());
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("error outlink");
		}
		try {
			for (Category category : page.getCategories()) {
				listcategory.add(category.getTitle().toString());
				// System.out.println(category.getTitle().toString());
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("error cat");
		}
		if (listredirect.size() == 0)
			listredirect.add("null");
		if (listinlink.size() == 0)
			listinlink.add("null");
		if (listoutlink.size() == 0)
			listoutlink.add("null");
		if (listcategory.size() == 0)
			listcategory.add("null");
		
		JSONArray jsonarrayredirect = JSONArray.fromObject(listredirect);
		JSONArray jsonarrayinlink = JSONArray.fromObject(listinlink);
		JSONArray jsonarrayoutlink = JSONArray.fromObject(listoutlink);
		JSONArray jsonarraycategory = JSONArray.fromObject(listcategory);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("redirect", jsonarrayredirect);
		map.put("inlink", jsonarrayinlink);
		map.put("outlink", jsonarrayoutlink);
		map.put("category", jsonarraycategory);
		Map<String, Object> mapres = new HashMap<String, Object>();
		mapres.put(page.getTitle().toString(), map);
		usepage.put(page.getTitle().toString(), map);
		//System.out.println(mapres);
		//System.out.println(usepage);
		JSONObject jsonObj = JSONObject.fromObject(mapres);
		return jsonObj;
		//System.out.println(jsonObj);
	}

	public static List<String> readFileByLines(String fileName) {
		File file = new File(fileName);
		List<String> titleall = new LinkedList<String>();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			int line = 1;

			while ((tempString = reader.readLine()) != null) {
				// System.out.println("line " + line + ": " + tempString);
				line++;
				titleall.add(tempString);
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}
		return titleall;
	}
}