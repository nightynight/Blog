package bloger;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

public class FileParse {
	public static int depth=0;//文件的深度
	public static String menuDepth="";//第几层menu
	public static String getDepth(int depth){
		String string="";
		switch (depth) {
		case 1:	string="firstMenu"; break;
		case 2: string="secondMenu"; break;
		case 3: string="thirdMenu"; break;
		case 4: string="fourthMenu"; break;
		default: string="otherMenu";
			break;
		}
		return string;
	}
	
	/**
	 * 以字符串形式输出的读文件方法
	 * @param file
	 */
	public static String Read(File file) throws IOException {
		if(!file.exists())
			throw new IllegalArgumentException("文件"+file+"不存在.");
		if(!file.isFile())
			throw new IllegalArgumentException(file+"不是文件.");
		
		RandomAccessFile raf=new RandomAccessFile(file, "r");
		raf.seek(0);
		//一次性读取，把文件中的内容都读到字节数组中
		byte[] buf=new byte[(int)raf.length()];
		raf.read(buf);
		raf.close();
		
		//以字符串方式输出
		String string=new String(buf,"utf-8");
		
		return string;
	}
	/**
	 * 往文件写入字符串
	 * @param file,string
	 */
	public static void Write(File file,String string) throws IOException {
		if(!file.exists())
			file.createNewFile();
		else {
			file.delete();
			file.createNewFile();
		}
		if(!file.isFile())
			throw new IllegalArgumentException(file+"不是文件.");
		
		RandomAccessFile raf=new RandomAccessFile(file, "rw");
		byte[] utf=string.getBytes("utf-8");
		raf.write(utf);
	}
	
	//以下方法为获取最新三条blog
	public static List<File> fileList=new ArrayList<File>();
	public static List<File> getLastThreeBlogs(File dir) throws IOException {
		addToList(dir);
		File[] files=fileList.toArray(new File[fileList.size()]);
		range(files);
		List<File> list=new ArrayList<File>();
		try {
			for(int i=files.length-1;i>files.length-4;i--){
				list.add(files[i]);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return list;
	}
	public static void addToList(File dir) throws IOException {
		if(!dir.exists())
			throw new IllegalArgumentException("目录"+dir+"不存在.");
		if(!dir.isDirectory())
			throw new IllegalArgumentException(dir+"不是目录.");
		
		File[] files=dir.listFiles();
		if(files!=null && files.length>0){
			for (File file : files) {
				if(file.isDirectory())
					addToList(file);
				else if(!file.getName().startsWith(".")) {
					fileList.add(file);
				}
			}
		}
	}
	public static void range(File[] files) {
		File temp;
		int cursor=0;
		for(int i=files.length-1;i>=0;i--){
			temp=files[i];
			cursor=i;
			for(int j=i;j>=0;j--){
				if(files[j].lastModified()>temp.lastModified()){
					temp=files[j];
					cursor=j;
				}
			}
			swap(files,i,cursor);
		}
	}
	public static void swap(File[] files,int a,int b) {
		File temp=files[a];
		files[a]=files[b];
		files[b]=temp;
	}
	
	
	
	
	
	//下面开始解析生成html文件
	public static void MakeHtml(String projectPath) throws IOException {
		File file=new File(projectPath+"all-category");
		String result="";
		result+=
			"<html>\n"+
		    "	<head>\n"+
	        "		<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />\n"+
	        "		<title>我的博客</title>\n"+
	        "		<script src=\"./js/jquery-3.0.0.min.js\"></script>"+
	        "		<script src=\"./js/main.js\"></script>\n"+
	        "		<link href=\"./style/main.css\" rel=\"stylesheet\" type=\"text/css\"/>\n"+
		    "	</head>\n"+
	    	"	<body>\n"+
	    	"		<h1>我的Blog</h1>\n"+
	    	"		<div class=\"float-left\" style=\"width: 20%;\">\n"+
    		"			<div id=\"new-article\">"+
			"				<p>最新文章</p>\n"+
			"				<ul>\n";
		
		result=MakeLastBlogList(file, result);

		result+=
			"				</ul>\n"+
    		"			</div>\n"+
    		"			<div id=\"all-category\">\n"+
			"				<p>所有文章分类</p>\n"+
			"				<ul id=\"nav\">\n";
		
		result=FileParse.MakeUl(file,result);
		
		result+=
			"				</ul>\n"+
			"			</div>\n"+
			"		</div>	\n"+
    		"		<div id=\"blog-introduction\" class=\"float-right\" style=\"width:75%;\">\n"+
    		"			<h2>博客介绍</h2>\n";
		File introduction=new File(projectPath+"introduction.html");
		String sIntroduction=Read(introduction);
    	result+=
    		"			"+sIntroduction+"\n";
    	result+=
    		"		</div>\n"+
    		"	</body>\n"+
    		"</html>\n";
    	
    	Write(new File(projectPath+"index.html"), result);
	}
	
	//生成最新文章代码
	public static String MakeLastBlogList(File dir,String result) throws IOException {
		List<File> list=getLastThreeBlogs(dir);
		for(int i=0;i<list.size();i++){
			result+=
					"<li><a href=\"./all-category"+list.get(i).getPath().split("ll-category")[1]+"\">"+list.get(i).getName()+"</a></li>\n";
		}
		
		return result;
	}
	
	//生成所有文章分类代码
	public static String MakeUl(File dir,String result) throws IOException {
		File[] files=dir.listFiles();
		if(files!=null && files.length>0){
			for (File file : files) {
				if(file.isDirectory()){
					depth++;
					menuDepth=getDepth(depth);
					result+=
						"<li class=\"li-"+menuDepth+" dir\">\n"+
	                    "	<div class=\"div-"+menuDepth+"\">\n"+
	                    "		<span class=\"span-"+menuDepth+"\">"+file.getName()+"</span>\n"+
	                    "    	<span class=\"ic-pull\"></span>\n"+
	                    "	</div>\n";
					depth++;
					menuDepth=getDepth(depth);
                    result+="<ul class=\"ul-"+menuDepth+"\">\n";
                    depth--;
					result = MakeUl(file,result);
					result+=
						"	</ul>\n"+
						"</li>\n";
					depth--;
				}
				else if(!file.getName().startsWith(".")) {
					depth++;
					menuDepth=getDepth(depth);
					result+=
						"<li class=\"li-"+menuDepth+"\">\n"+
	                    "	<div class=\"div-"+menuDepth+"\">\n"+
	                    "		<a href=\"./all-category"+file.getPath().split("ll-category")[1]+"\">\n"+
	                    "			<span class=\"span-"+menuDepth+"\">"+file.getName()+"</span>\n"+
	                    "		</a>\n"+
	                    "	</div>\n"+
	                    "</li>\n";
					depth--;
				}
			}
		}
		return result;
	}
}
