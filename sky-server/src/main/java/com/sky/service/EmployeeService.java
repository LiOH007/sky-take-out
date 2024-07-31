package com.sky.service;

import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.result.PageResult;

public interface EmployeeService {

    /**
     * 员工登录
     * @param employeeLoginDTO
     * @return
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

    /**
     * 新增员工
     *
     * @param employeeDTO employee dto
     **/
    void save(EmployeeDTO employeeDTO);

    /**
     * 员工分页查询
     *
     * @param employeePageQueryDTO employee page query dto
     * @return {@link PageResult }
     **/
    PageResult query(EmployeePageQueryDTO employeePageQueryDTO);

    /**
     * 启用/禁用员工
     *
     * @param status status
     * @param id     id
     **/
    void startOrStop(Integer status, Long id);
}
