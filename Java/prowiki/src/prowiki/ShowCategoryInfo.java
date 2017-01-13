package prowiki;

import de.tudarmstadt.ukp.wikipedia.api.Category;
import de.tudarmstadt.ukp.wikipedia.api.DatabaseConfiguration;
import de.tudarmstadt.ukp.wikipedia.api.Page;
import de.tudarmstadt.ukp.wikipedia.api.WikiConstants.Language;
import de.tudarmstadt.ukp.wikipedia.api.Wikipedia;
import de.tudarmstadt.ukp.wikipedia.api.exception.WikiApiException;

import static de.tudarmstadt.ukp.wikipedia.api.WikiConstants.LF;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class ShowCategoryInfo {
	public static void main(String[] args) throws Exception {
		// 连接数据库的配置
		DatabaseConfiguration dbConfig = new DatabaseConfiguration();
		dbConfig.setHost("localhost");
		dbConfig.setDatabase("wikidata");
		dbConfig.setUser("root");
		dbConfig.setPassword("");
		dbConfig.setLanguage(Language.english);
		// 创建Wikipedia处理对象
		Wikipedia wiki = new Wikipedia(dbConfig);
		List<String> categoryall = readFileByLines("category.txt");
		HashSet<String> categoryres = new HashSet<String>();
		for(int i = 0 ;i < categoryall.size();i++){
			categoryres.add(categoryall.get(i));
		}
		HashSet<String> pageres = new HashSet<String>();
		int flag = 1;
		HashSet<String> cattmp = new HashSet<String>();
		while (flag <= 2) {
			System.out.println(flag);
			cattmp.clear();
			for (Iterator<String> it = categoryall.iterator(); it.hasNext();) {
				String title = it.next();
				try{
					Category cat = wiki.getCategory(title);
					
					for (Category child : cat.getChildren()) {
						cattmp.add(child.getTitle().toString());
					}
					for (Page page : cat.getArticles()) {
						pageres.add(page.getTitle().toString());
					}
				}
				catch (WikiApiException e) {
					System.out.println(title+" not found");
					continue;
				}
				finally {
					
				}
			}
			
			categoryall.clear();
			for (Iterator<String> it = cattmp.iterator(); it.hasNext();) {
				String tmp = it.next();
				categoryall.add(tmp);
				categoryres.add(tmp);
			}
			System.out.println(categoryres.size());
			System.out.println(pageres.size());
			flag++;
		}
		writetofile("categoryuse.txt",categoryres);
		writetofile("pageuse.txt",pageres);
		// String title = "Formal sciences";
		// 创建类对象
	}

	public static void writetofile(String filename,HashSet<String>content) {
		try {
			File file = new File(filename);
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file.getName(),true);
			BufferedWriter bw = new BufferedWriter(fw);
			for (Iterator<String> it = content.iterator(); it.hasNext();) {
				bw.write(it.next());
				bw.write("\n");
			}
			bw.close();
			System.out.println("Done");
		} catch (IOException e) {
			System.err.println("Problem writing to the file statsTest.txt");
		}
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
				} 
				catch (IOException e1) {
				}
			}
		}
		return titleall;
	}
}