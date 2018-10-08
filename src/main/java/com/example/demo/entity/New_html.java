package com.example.demo.entity;

import java.util.Date;
import com.baomidou.mybatisplus.activerecord.Model;
import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author zwf
 * @since 2018-10-08
 */
public class New_html extends Model<New_html> {

    private static final long serialVersionUID = 1L;

    private Long try_catch_id;

    private Long directory_id;

    private Integer sort;

    private String novel_title;

    private Date crt_date;

    public Long getTry_catch_id() {
        return try_catch_id;
    }

    public void setTry_catch_id(Long try_catch_id) {
        this.try_catch_id = try_catch_id;
    }
    public Long getDirectory_id() {
        return directory_id;
    }

    public void setDirectory_id(Long directory_id) {
        this.directory_id = directory_id;
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
    public Date getCrt_date() {
        return crt_date;
    }

    public void setCrt_date(Date crt_date) {
        this.crt_date = crt_date;
    }

    @Override
    protected Serializable pkVal() {
        return this.try_catch_id;
    }

    @Override
    public String toString() {
        return "New_html{" +
                "try_catch_id=" + try_catch_id +
                ", directory_id=" + directory_id +
                ", sort=" + sort +
                ", novel_title=" + novel_title +
                ", crt_date=" + crt_date +
                "}";
    }
}
