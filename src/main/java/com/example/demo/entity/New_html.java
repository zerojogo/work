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
 * @since 2018-09-29
 */
@TableName("new_html")
public class New_html extends Model<New_html> {

    private static final long serialVersionUID = 1L;

    @TableId("new_html_id")
    private Long new_html_id;

    private Integer sort;

    private String novel_title;

    private String novel_directory;

    private String novel_directory_url;

    private String novel_directory_content;

    private Date crt_date;

    public Long getNew_html_id() {
        return new_html_id;
    }

    public void setNew_html_id(Long new_html_id) {
        this.new_html_id = new_html_id;
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
        return this.new_html_id;
    }

    @Override
    public String toString() {
        return "New_html{" +
        "new_html_id=" + new_html_id +
        ", sort=" + sort +
        ", novel_title=" + novel_title +
        ", novel_directory=" + novel_directory +
        ", novel_directory_url=" + novel_directory_url +
        ", novel_directory_content=" + novel_directory_content +
        ", crt_date=" + crt_date +
        "}";
    }
}
