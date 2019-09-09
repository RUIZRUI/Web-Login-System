create table if not exists `i278_user`(
    `id` int(11) not null auto_increment,
    `username` varchar(100) not null unique default '',
    `email` varchar(100) not null unique default '',
    `password` varchar(100) not null default '',
    `register_time` datetime not null,
    `last_login_time` datetime not null,
    `iconID` int(11) not null default 0,
    primary key(`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 default charset=utf8;



-- 验证码
create table if not exists `i278_code`(
    `email` varchar(100) not null unique default '',
    `code` varchar(10) not null default '',
    `start_time` datetime not null,
    primary key(`email`)
) ENGINE=InnoDB default charset=utf8;