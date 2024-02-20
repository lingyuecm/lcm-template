create table BIZ_USER
(
    USER_ID      bigint auto_increment comment 'The primary key'
        primary key,
    PHONE_NO     varchar(16)   null comment 'The phone number',
    LOGIN_PWD    varchar(128)  null comment 'The encrypted password',
    FIRST_NAME   varchar(32)   null comment 'The first name',
    LAST_NAME    varchar(32)   null comment 'The last name',
    STATUS      int default 1 null comment '0: disabled; 1: enabled',
    CREATED_BY   bigint        null,
    TIME_CREATED datetime      null,
    UPDATED_BY   bigint        null,
    TIME_UPDATED datetime      null
);

create table CONF_ROLE
(
    ROLE_ID      int auto_increment comment 'The primary key'
        primary key,
    ROLE_NAME    varchar(32) null comment 'The role name',
    STATUS      int         null,
    CREATED_BY   bigint      null,
    TIME_CREATED datetime    null,
    UPDATED_BY   bigint      null,
    TIME_UPDATED datetime    null
);

create table CONF_PERMISSION
(
    PERMISSION_ID  int auto_increment comment 'The primary key'
        primary key,
    HTTP_METHOD    varchar(4)   null comment 'The HTTP method (GET, PUT, POST)',
    PERMISSION_URL varchar(512) null comment 'The URL to the API. For path variables, the variable names should be removed, only braces are kept',
    STATUS         int          null,
    CREATED_BY     bigint       null,
    TIME_CREATED   datetime     null,
    UPDATED_BY     bigint       null,
    TIME_UPDATED   datetime     null
)
    comment 'The permissions to the APIs';



create table MAP_USER_ROLE
(
    MAPPING_ID bigint auto_increment comment 'The primary key'
        primary key,
    USER_ID    bigint null comment 'The user ID',
    ROLE_ID    int    null comment 'The role ID'
)
    comment 'The mappings between the users and the roles';



create table MAP_ROLE_PERMISSION
(
    MAPPING_ID    BIGINT auto_increment comment 'The primary key',
    ROLE_ID       int null comment 'The role ID',
    PERMISSION_ID int null comment 'The permission ID',
    constraint MAP_ROLE_PERMISSION_pk
        primary key (MAPPING_ID)
)
    comment 'The mappings between roles and permissions';



