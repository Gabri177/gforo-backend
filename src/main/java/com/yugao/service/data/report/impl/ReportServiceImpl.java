package com.yugao.service.data.report.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yugao.domain.report.Report;
import com.yugao.enums.StatusEnum;
import com.yugao.mapper.report.ReportMapper;
import com.yugao.service.data.report.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final ReportMapper reportMapper;

    @Override
    public List<Report> getAllReports(Integer cuurentPage, Integer pageSize, StatusEnum status) {

        LambdaQueryWrapper<Report> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(status != StatusEnum.NULL, Report::getStatus, status);
        queryWrapper.orderByDesc(Report::getCreateTime);
        Page<Report> page = new Page<>(cuurentPage, pageSize);
        return reportMapper.selectPage(page, queryWrapper).getRecords();
    }

    @Override
    public Long countAllReports(StatusEnum status) {

        LambdaQueryWrapper<Report> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(status != StatusEnum.NULL, Report::getStatus, status);
        return reportMapper.selectCount(queryWrapper);
    }

    @Override
    public Report getReportById(Long id) {

        LambdaQueryWrapper<Report> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.eq(Report::getStatus, StatusEnum.PENDING);
        queryWrapper.eq(Report::getId, id);
        return reportMapper.selectOne(queryWrapper);
    }

    @Override
    public void updateReport(Report report) {

        reportMapper.updateById(report);
    }

    @Override
    public void addReport(Report report) {

        reportMapper.insert(report);
    }

    @Override
    public void deleteReport(Long reportId) {

        LambdaUpdateWrapper<Report> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Report::getId, reportId);
        wrapper.set(Report::getStatus, StatusEnum.DELETED);
        reportMapper.update(null, wrapper);
    }

    @Override
    public void updateReportStatus(Long reportId, StatusEnum status) {

        LambdaUpdateWrapper<Report> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Report::getId, reportId);
        wrapper.set(Report::getStatus, status);
        reportMapper.update(null, wrapper);
    }
}
