package com.example.demo.entity;

import java.util.Date;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author zwf
 * @since 2018-09-27
 */
@TableName("directory")
public class Directory extends Model<Directory> {

    private static final long serialVersionUID = 1L;

    @TableId("directory_id")
    private Long directory_id;

    private Long novel_id;

    private Integer sort;

    private String novel_title;

    private String novel_directory;

    private String novel_directory_url;

    private String novel_directory_content;

    private Date crt_date;

    public Long getDirectory_id() {
        return directory_id;
    }

    public void setDirectory_id(Long directory_id) {
        this.directory_id = directory_id;
    }
    public Long getNovel_id() {
        return novel_id;
    }

    public void setNovel_id(Long novel_id) {
        this.novel_id = novel_id;
    }
    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }
    public String getNovel_title() {
        return novel_title;
    }

    public void setNovel_title(String novel_title) {
        this.novel_title = novel_title;
    }
    public String getNovel_directory() {
        return novel_directory;
    }

    public void setNovel_directory(String novel_directory) {
        this.novel_directory = novel_directory;
    }
    public String getNovel_directory_url() {
        return novel_directory_url;
    }

    public void setNovel_directory_url(String novel_directory_url) {
        this.novel_directory_url = novel_directory_url;
    }
    public String getNovel_directory_content() {
        return novel_directory_content;
    }

    public void setNovel_directory_content(String novel_directory_content) {
        this.novel_directory_content = novel_directory_content;
    }
    public Date getCrt_date() {
        return crt_date;
    }

    public void setCrt_date(Date crt_date) {
        this.crt_date = crt_date;
    }

    @Override
    protected Serializable pkVal() {
        return this.directory_id;
    }

    @Override
    public String toString() {
        return "Directory{" +
        "directory_id=" + directory_id +
        ", novel_id=" + novel_id +
        ", sort=" + sort +
        ", novel_title=" + novel_title +
        ", novel_directory=" + novel_directory +
        ", novel_directory_url=" + novel_directory_url +
        ", novel_directory_content=" + novel_directory_content +
        ", crt_date=" + crt_date +
        "}";
    }
}
