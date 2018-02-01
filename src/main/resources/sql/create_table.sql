-- Create table
create table INS_FILE_SERVER_DETAIL
(
  id             VARCHAR2(50) not null,
  file_name      VARCHAR2(200),
  busi_no        VARCHAR2(50),
  file_no        VARCHAR2(50),
  issue_time     TIMESTAMP(6),
  file_size      VARCHAR2(50),
  is_folder      NUMBER(1),
  parent_id      VARCHAR2(50),
  is_delete      NUMBER(1),
  create_time    TIMESTAMP(6),
  create_user_id VARCHAR2(50),
  modify_time    TIMESTAMP(6),
  modify_user_id VARCHAR2(50)
)
tablespace CMNET
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
-- Add comments to the table
comment on table INS_FILE_SERVER_DETAIL
  is '局数据文件上传明细';
-- Add comments to the columns
comment on column INS_FILE_SERVER_DETAIL.file_name
  is '文件名称';
comment on column INS_FILE_SERVER_DETAIL.busi_no
  is '局数据号';
comment on column INS_FILE_SERVER_DETAIL.file_no
  is '文号';
comment on column INS_FILE_SERVER_DETAIL.issue_time
  is '下发时间';
comment on column INS_FILE_SERVER_DETAIL.file_size
  is '文件大小';
comment on column INS_FILE_SERVER_DETAIL.is_folder
  is '是否是目录1:是，0：否';
comment on column INS_FILE_SERVER_DETAIL.parent_id
  is '父目录id';
comment on column INS_FILE_SERVER_DETAIL.is_delete
  is '是否删除1:是，0：否';
comment on column INS_FILE_SERVER_DETAIL.create_time
  is '上传时间';
comment on column INS_FILE_SERVER_DETAIL.create_user_id
  is '上传人';
comment on column INS_FILE_SERVER_DETAIL.modify_time
  is '修改时间';
comment on column INS_FILE_SERVER_DETAIL.modify_user_id
  is '修改人';

---添加主键id
ALTER TABLE INS_FILE_SERVER_DETAIL ADD CONSTRAINT PK_INS_FILE_SERVER_DETAIL PRIMARY KEY (id);