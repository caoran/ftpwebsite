package com.boco.henan.ftpwebsite.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.boco.henan.ftpwebsite.entity.FileServerDetail;

@Mapper
public interface UploadMapper {//file_name,busi_no,File_No,Issue_Time,File_Size,is_folder,Parent_Id,file_path,create_time
	
	//@Insert("insert into INS_FILE_SERVER_DETAIL(ID) values(#{ID,jdbcType=BIGINT})")
	@Insert("insert into INS_FILE_SERVER_DETAIL"
			+ " (id,file_name,Issue_Time,busi_no,file_no,file_size,is_folder,parent_id,file_path,is_delete,create_time)"
			+ "values(#{ID},#{filename}, TO_DATE(#{dateSelecttime},'YYYY-MM-DD HH24:MI:SS') ,#{busino},#{fileno},#{filesize},'0',#{parentId} ,#{filepath},'0',systimestamp)")
	void insertFileDetail(@Param("ID") String ID,@Param("filename") String filename,@Param("dateSelecttime") String dateSelecttime,
			@Param("busino") String busino,@Param("fileno") String fileno,
			@Param("filesize")String filesize,@Param("parentId") String parentId,@Param("filepath") String filepath);
	
	
	
	@Select("select count(file_name) from INS_FILE_SERVER_DETAIL where file_name= #{filename} ")
	Long findFileName(@Param("filename") String filename);//查询文件名是否存在
}
