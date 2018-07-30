package com.boco.henan.ftpwebsite.controller;

import com.boco.henan.ftpwebsite.entity.Node;
import com.boco.henan.ftpwebsite.exception.ValidationException;
import com.boco.henan.ftpwebsite.util.FileUtil;
import com.boco.henan.ftpwebsite.util.RedisClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
@Controller
@RequestMapping("/fileUpload")
public class UploadController {

	private Logger LOGGER= LoggerFactory.getLogger(UploadController.class);

   @Autowired
   private RedisClient<Node> redisClient;
   
	@GetMapping("/myUpload/{parentId}")
	public ModelAndView uploadFile(@PathVariable("parentId") String parentId) {
		ModelAndView mav = new ModelAndView("upload");
		String filepath=null;
		if(redisClient.containsKey(parentId)){
			Node node=redisClient.get(parentId);
			filepath=node.getNodePath();
		}
		mav.addObject("parentId", parentId);
		mav.addObject("filepath", filepath);

		return mav;
	}
	
	@PostMapping("/upload")
	@ResponseBody //文件上传
	public Map<String, String> upload(MultipartFile file,@RequestParam String parentId)
			throws Exception {
		Map<String, String> returnMap = new HashMap<>();
		if(file==null){
			return returnMap;
		}
		String fileName =  file.getOriginalFilename();
		String  size="0";
		if (file.isEmpty()) {

		}else {
			if (redisClient.containsKey(parentId)) {
				Node node = redisClient.get(parentId);
				String filepath = node.getNodePath() + File.separator + fileName;
				/*File destFile = new File(filepath, fileName);
				FileUtil.createNewFile(filepath, true);
				file.transferTo(destFile);*/
				byte[] bytes = file.getBytes();
				Path path = Paths.get(filepath);
				if(Files.exists(path)){
                    returnMap.put("status", "0");
                    returnMap.put("message", "目录下包含同名文件"+fileName+",上传文件失败");
                    return returnMap;
					//throw  new ValidationException("目录"+path.toString()+"下包含同名文件"+fileName+",上传文件失败");
				}
				if (Files.notExists(path.getParent())){
					Files.createDirectories(path.getParent());
				}
				Files.write(path, bytes);
				size=FileUtil.getFileSize(filepath);
			}
		}
		returnMap.put("status", "1");
		returnMap.put("size", size);
		returnMap.put("fileName", fileName);
		return returnMap;
	}


	/*
	@RequestMapping(value = "/insertFile", method = RequestMethod.POST)
	@ResponseBody  //@RequestBody @Valid FileServerDetail file
	public String insertFile( String dateSelecttime,  String busino,String fileno,String filesize,String filename ,String parentId,String filepath)
			throws IOException {
		 String result=null;
		Map<String, String> returnMap = new HashMap<>();//
		String ID=UUID.randomUUID().toString();
		if(filename!=null){
		  Long count=uploadMapper.findFileName(filename);
		  if(count>0){
			  result="1";
		  }else{
			  result="0";
			  uploadMapper.insertFileDetail(ID,filename,dateSelecttime,busino,fileno,filesize,parentId,filepath);
		  }
		}
		   return result;
	}*/
	 

	    /**
	     *所有报文文件下载@PathVariable("filepath") String filepath, @PathVariable("filename") String filename
	     */
	    @RequestMapping(value = "/alldownload/{id}",method = RequestMethod.GET)///{filepath}/{filename}
	    public void allDownload(@PathVariable("id") String id,HttpServletResponse response) {
	        response.reset();
	        String filename=null;
	        String filepath=null;
	        if(redisClient.containsKey(id)){
	        	Node node=redisClient.get(id);
	        	 filename=node.getName();
	        	 filepath=node.getNodePath();
			}
			LOGGER.info("下载文件开始，文件名称为:{}，文件路径为：{}",filename,filepath);
			response.setContentType("application/x-msdownload");
	        
	        //取工单详细信息
	      String downAd = filepath;
	      System.out.println("downAd:-----------"+downAd);
	        InputStream in = null;
	        try {
	          //  String fileName = downAd.substring(downAd.lastIndexOf(File.separator)+1);
	      	 String fileName =filename;
	      	 System.out.println("filename====="+fileName);
	        //    response.setHeader("Content-Disposition", "attachment; filename=" + new String(fileName.getBytes("utf-8"),"ISO8859-1"));
	        //////////////////////////ces
	            File f=new File(downAd);
	            if(f.isDirectory()){
	            	 logzipprocess( downAd);
	            	 downAd=downAd+".zip";//D:\ftp\管理规定及其他\config-instances.zip
	            	 fileName=fileName+".zip";
	            	 System.out.println("filename====="+fileName);
	            }
	            response.setHeader("Content-Disposition", "attachment; filename=" + new String(fileName.getBytes("utf-8"),"ISO8859-1"));
	            System.out.println(downAd+"**********************");//D:\ftp\管理规定及其他\2016
	       //////////////////////     ces
	            
	            in = new FileInputStream(downAd);
	            FileCopyUtils.copy(in, response.getOutputStream());
	        
	        }catch (UnsupportedEncodingException e) {
	        	e.printStackTrace();
	        } catch (Exception e) {
	        	e.printStackTrace();
	        } finally {
	            if (in != null) {
	                try {
	                    in.close();
	                } catch (IOException e) {
	                }
	            }
	        }
	    }
	    
	   // @RequestMapping(value = "/alldownload/{id}",method = RequestMethod.GET)
	    public void  testDownload(@PathVariable("id") String id,HttpServletResponse resp) throws IOException{ 
	    
	     
	        String filename=null;
	        String filepath=null;
	        if(redisClient.containsKey(id)){
	        	Node node=redisClient.get(id);
	        	 filename=node.getName();
	        	 filepath=node.getNodePath();
	        			System.out.println(filename+"***********"+filepath);
	        }
	    
		     String downAd = filepath;
		    // downAd=downAd;
		     File file = new File(downAd);
		     if(file.isDirectory()){
            	 logzipprocess( downAd);
            	 downAd=downAd+".zip";//D:\ftp\管理规定及其他\config-instances.zip
            	   filename=new File(filename+".zip").getName();
            	    System.out.println("filename---------------"+filename);
            }
		     System.out.println("downAd---------------"+downAd);
		  
		   
	        resp.setHeader("content-type", "application/octet-stream");
		     resp.setContentType("application/octet-stream");
		 //    resp.setHeader("Content-Disposition", "attachment;filename=" +filename);
		     resp.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", new String(filename.getBytes("GBK"), "ISO8859-1")));
		      
		     
		     
		     HttpHeaders headers = new HttpHeaders();
		        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
		    //    headers.add("Content-Disposition", "attachment; filename=" + filename);
		        headers.add("Content-Disposition", String.format("attachment; filename=\"%s\"", new String(filename.getBytes("GBK"), "ISO8859-1")));
		        

		        headers.add("Pragma", "no-cache");
		        headers.add("Expires", "0");
//		         return ResponseEntity
//		                .ok()
//		                .headers(headers)
//		                .contentLength(file.length())
//		                .contentType(MediaType.parseMediaType("application/octet-stream"))
//		                .body(new InputStreamResource(new FileInputStream(file)));
	     //new InputStreamResource(is));
	     //new FileInputStream(new File(filepath));
	     
//	     byte[] buff=new byte[1024];
//	     BufferedInputStream bis =null;
//	     OutputStream os=null;
//	     try{
//	          os = resp.getOutputStream();
//	          bis = new BufferedInputStream(new FileInputStream(file));
//	          int i = bis.read(buff);
//	          while(i!=-1){
//	        	  os.write(buff, 0, buff.length);
//	        	  os.flush();
//	        	  i = bis.read(buff);
//	            }
//	     }catch(IOException e){
//	    	 e.printStackTrace();
//	     }finally{
//	    	 if(bis!=null){
//	    		 bis.close();
//	    	 }
//	    	 
//	     }
	    	 
	     }
	    
	    public static void main(String[] args) {
			File f=new File("D:\\hhhh.txt");
			System.out.println(f.getName());
		}
		public String logzipprocess(String filepath){
			boolean zip=false;
			String zipFileName = "";
			try {
				String setName="";
				String fileName="";
				String inputFilePath = filepath;
			
					//核查日志_工单ID_操作时间
					zipFileName =filepath;
					 fileName = filepath.substring(filepath.lastIndexOf(File.separator));
					//fileName="ggee";
				
				File inputFile = new File(inputFilePath);
				System.out.println("压缩中...");

				fileToZip(inputFilePath, inputFilePath, fileName);
			
				System.out.println("压缩完成");
			} catch (Exception e) {
				e.printStackTrace();
			}
			return zipFileName;
		}
		
		public static boolean fileToZip(String sourceFilePath,String zipFilePath,String fileName){
			boolean flag = false;
			File sourceFile = new File(sourceFilePath);
			FileInputStream fis = null;
			BufferedInputStream bis = null;
			FileOutputStream fos = null;
			ZipOutputStream zos = null;

			if(sourceFile.exists() == false){
				System.out.println("待压缩的文件目录："+sourceFilePath+"不存在.");
			}else{
				try {
					File parentFile=new File(zipFilePath);
					if (!parentFile.exists()){ parentFile.mkdirs(); }
			//		File zipFile = new File(zipFilePath + "/" + fileName +".zip");
					File zipFile = new File(parentFile.getParent() + "/" + fileName +".zip");
					if(zipFile.exists()){
						System.out.println(zipFilePath + "目录下存在名字为:" + fileName +".zip" +"打包文件.");
					}else{

						File[] sourceFiles = sourceFile.listFiles();
						if(null == sourceFiles || sourceFiles.length<1){
							System.out.println("待压缩的文件目录：" + sourceFilePath + "里面不存在文件，无需压缩.");
						}else{
							fos = new FileOutputStream(zipFile);
							zos = new ZipOutputStream(new BufferedOutputStream(fos));
							byte[] bufs = new byte[1024*10];
							for(int i=0;i<sourceFiles.length;i++){
								//创建ZIP实体，并添加进压缩包
								if(sourceFiles[i].getName().indexOf(".zip")>-1){
									continue;
								}
								ZipEntry zipEntry = new ZipEntry(sourceFiles[i].getName());
								zos.putNextEntry(zipEntry);
								//读取待压缩的文件并写进压缩包里
								fis = new FileInputStream(sourceFiles[i]);
								bis = new BufferedInputStream(fis, 1024*10);
								int read = 0;
								while((read=bis.read(bufs, 0, 1024*10)) != -1){
									zos.write(bufs,0,read);
								}
							}
							flag = true;
						}
					}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
					throw new RuntimeException(e);
				} catch (IOException e) {
					e.printStackTrace();
					throw new RuntimeException(e);
				} finally{
					//关闭流
					try {
						if(null != bis) bis.close();
						if(null != zos) zos.close();
					} catch (IOException e) {
						e.printStackTrace();
						throw new RuntimeException(e);
					}
				}
			}
			return flag;
		}
}
