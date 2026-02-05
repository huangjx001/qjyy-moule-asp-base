-- 定义态建模服务数据库初始化脚本
-- 注意：请按实际库名执行

CREATE TABLE IF NOT EXISTS process_route (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    product_id BIGINT DEFAULT NULL COMMENT '产品ID',
    route_code VARCHAR(64) NOT NULL COMMENT '路线编码',
    route_name VARCHAR(128) NOT NULL COMMENT '路线名称',
    product_code VARCHAR(64) DEFAULT NULL COMMENT '产品编码',
    product_name VARCHAR(128) DEFAULT NULL COMMENT '产品名称',
    dosage_form VARCHAR(64) DEFAULT NULL COMMENT '剂型',
    remark VARCHAR(255) DEFAULT NULL COMMENT '备注',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_process_route_code (route_code),
    KEY idx_process_route_product (product_id)
) COMMENT='工艺路线';

CREATE TABLE IF NOT EXISTS process_route_version (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    route_id BIGINT NOT NULL COMMENT '路线ID',
    version_code VARCHAR(64) NOT NULL COMMENT '版本号',
    version_name VARCHAR(128) NOT NULL COMMENT '版本名称',
    status VARCHAR(32) NOT NULL COMMENT '状态:DRAFT/RELEASED',
    enabled TINYINT NOT NULL DEFAULT 1 COMMENT '是否启用',
    remark VARCHAR(255) DEFAULT NULL COMMENT '备注',
    released_by VARCHAR(64) DEFAULT NULL COMMENT '发布人',
    released_at DATETIME DEFAULT NULL COMMENT '发布时间',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    KEY idx_route_version_route (route_id),
    UNIQUE KEY uk_route_version_code (route_id, version_code)
) COMMENT='工艺路线版本';

CREATE TABLE IF NOT EXISTS product_base (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    product_code VARCHAR(64) NOT NULL COMMENT '产品编码',
    product_name VARCHAR(128) NOT NULL COMMENT '产品名称',
    product_type VARCHAR(64) DEFAULT NULL COMMENT '产品类型',
    dosage_form VARCHAR(64) DEFAULT NULL COMMENT '剂型',
    enabled TINYINT NOT NULL DEFAULT 1 COMMENT '是否启用',
    remark VARCHAR(255) DEFAULT NULL COMMENT '备注',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_product_base_code (product_code)
) COMMENT='产品基础数据';

CREATE TABLE IF NOT EXISTS process_base (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    process_code VARCHAR(64) NOT NULL COMMENT '工序编码',
    process_name VARCHAR(128) NOT NULL COMMENT '工序名称',
    process_type VARCHAR(64) DEFAULT NULL COMMENT '工序类型',
    default_duration_minutes INT DEFAULT NULL COMMENT '默认时长(分钟)',
    critical_flag TINYINT DEFAULT 0 COMMENT '是否关键工序',
    enabled TINYINT NOT NULL DEFAULT 1 COMMENT '是否启用',
    remark VARCHAR(255) DEFAULT NULL COMMENT '备注',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_process_base_code (process_code)
) COMMENT='工序基础数据';

CREATE TABLE IF NOT EXISTS process_node (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    route_version_id BIGINT NOT NULL COMMENT '版本ID',
    process_base_id BIGINT DEFAULT NULL COMMENT '工序基础数据ID',
    node_name VARCHAR(128) NOT NULL COMMENT '工序名称',
    critical_flag TINYINT DEFAULT 0 COMMENT '是否关键工序',
    duration_minutes INT DEFAULT NULL COMMENT '时长(分钟)',
    position_x DECIMAL(10,2) DEFAULT NULL COMMENT '画布X坐标',
    position_y DECIMAL(10,2) DEFAULT NULL COMMENT '画布Y坐标',
    status VARCHAR(32) DEFAULT NULL COMMENT '状态',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    KEY idx_process_node_version (route_version_id),
    KEY idx_process_node_base (process_base_id)
) COMMENT='工序节点';

CREATE TABLE IF NOT EXISTS process_edge (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    route_version_id BIGINT NOT NULL COMMENT '版本ID',
    from_node_id BIGINT NOT NULL COMMENT '前置工序ID',
    to_node_id BIGINT NOT NULL COMMENT '后置工序ID',
    dependency_type VARCHAR(8) NOT NULL COMMENT '依赖类型:FS/SS/FF/SF',
    dependency_strength VARCHAR(8) NOT NULL COMMENT '依赖强度:HARD/SOFT',
    lag_minutes INT DEFAULT 0 COMMENT '滞后分钟',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    KEY idx_process_edge_version (route_version_id),
    KEY idx_process_edge_from (from_node_id),
    KEY idx_process_edge_to (to_node_id)
) COMMENT='工序依赖边';

CREATE TABLE IF NOT EXISTS resource_base (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    resource_code VARCHAR(64) NOT NULL COMMENT '资源编码',
    resource_name VARCHAR(128) NOT NULL COMMENT '资源名称',
    resource_type VARCHAR(64) DEFAULT NULL COMMENT '资源类型',
    capacity_desc VARCHAR(255) DEFAULT NULL COMMENT '能力/规格描述',
    default_setup_minutes INT DEFAULT 0 COMMENT '默认清场/换线时间(分钟)',
    enabled TINYINT NOT NULL DEFAULT 1 COMMENT '是否启用',
    remark VARCHAR(255) DEFAULT NULL COMMENT '备注',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_resource_base_code (resource_code)
) COMMENT='资源基础数据';

CREATE TABLE IF NOT EXISTS resource_room (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    route_version_id BIGINT NOT NULL COMMENT '版本ID',
    process_node_id BIGINT NOT NULL COMMENT '工序ID',
    resource_base_id BIGINT DEFAULT NULL COMMENT '资源基础数据ID',
    resource_name VARCHAR(128) NOT NULL COMMENT '资源名称',
    priority INT DEFAULT 0 COMMENT '优先级',
    setup_minutes INT DEFAULT 0 COMMENT '清场/换线时间(分钟)',
    enabled TINYINT NOT NULL DEFAULT 1 COMMENT '是否启用',
    status VARCHAR(32) DEFAULT NULL COMMENT '状态',
    remark VARCHAR(255) DEFAULT NULL COMMENT '备注',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    KEY idx_resource_room_version (route_version_id),
    KEY idx_resource_room_process (process_node_id),
    KEY idx_resource_room_base (resource_base_id)
) COMMENT='工序资源(房间)';

CREATE TABLE IF NOT EXISTS step_base (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    step_code VARCHAR(64) NOT NULL COMMENT '工步编码',
    step_name VARCHAR(128) NOT NULL COMMENT '工步名称',
    step_type VARCHAR(64) DEFAULT NULL COMMENT '工步类型',
    default_duration_minutes INT DEFAULT NULL COMMENT '默认时长(分钟)',
    qc_flag TINYINT DEFAULT 0 COMMENT '是否QC点',
    enabled TINYINT NOT NULL DEFAULT 1 COMMENT '是否启用',
    remark VARCHAR(255) DEFAULT NULL COMMENT '备注',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_step_base_code (step_code)
) COMMENT='工步基础数据';

CREATE TABLE IF NOT EXISTS step_node (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    route_version_id BIGINT NOT NULL COMMENT '版本ID',
    resource_room_id BIGINT NOT NULL COMMENT '资源ID',
    step_base_id BIGINT DEFAULT NULL COMMENT '工步基础数据ID',
    node_name VARCHAR(128) NOT NULL COMMENT '工步名称',
    qc_flag TINYINT DEFAULT 0 COMMENT '是否QC点',
    duration_minutes INT DEFAULT NULL COMMENT '时长(分钟)',
    position_x DECIMAL(10,2) DEFAULT NULL COMMENT '画布X坐标',
    position_y DECIMAL(10,2) DEFAULT NULL COMMENT '画布Y坐标',
    status VARCHAR(32) DEFAULT NULL COMMENT '状态',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    KEY idx_step_node_version (route_version_id),
    KEY idx_step_node_resource (resource_room_id),
    KEY idx_step_node_base (step_base_id)
) COMMENT='工步节点';

CREATE TABLE IF NOT EXISTS step_edge (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    route_version_id BIGINT NOT NULL COMMENT '版本ID',
    resource_room_id BIGINT NOT NULL COMMENT '资源ID',
    from_node_id BIGINT NOT NULL COMMENT '前置工步ID',
    to_node_id BIGINT NOT NULL COMMENT '后置工步ID',
    dependency_type VARCHAR(8) NOT NULL COMMENT '依赖类型:FS/SS/FF/SF',
    dependency_strength VARCHAR(8) NOT NULL COMMENT '依赖强度:HARD/SOFT',
    lag_minutes INT DEFAULT 0 COMMENT '滞后分钟',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    KEY idx_step_edge_version (route_version_id),
    KEY idx_step_edge_resource (resource_room_id)
) COMMENT='工步依赖边';

CREATE TABLE IF NOT EXISTS device (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    device_code VARCHAR(64) NOT NULL COMMENT '设备编码',
    device_name VARCHAR(128) NOT NULL COMMENT '设备名称',
    device_type VARCHAR(64) DEFAULT NULL COMMENT '设备类型',
    movable_flag TINYINT DEFAULT 0 COMMENT '是否可移动',
    status VARCHAR(32) DEFAULT NULL COMMENT '状态',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_device_code (device_code)
) COMMENT='设备主数据';

CREATE TABLE IF NOT EXISTS device_mount (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    route_version_id BIGINT NOT NULL COMMENT '版本ID',
    resource_room_id BIGINT NOT NULL COMMENT '资源ID',
    step_node_id BIGINT DEFAULT NULL COMMENT '工步ID',
    device_id BIGINT NOT NULL COMMENT '设备ID',
    mount_type VARCHAR(16) NOT NULL COMMENT '挂载来源:RESOURCE/STEP',
    remark VARCHAR(255) DEFAULT NULL COMMENT '备注',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    KEY idx_device_mount_version (route_version_id),
    KEY idx_device_mount_resource (resource_room_id),
    KEY idx_device_mount_step (step_node_id),
    KEY idx_device_mount_device (device_id)
) COMMENT='设备挂载';
