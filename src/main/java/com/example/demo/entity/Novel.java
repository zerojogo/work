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
 * @since 2018-10-09
 */
public class Novel extends Model<Novel> {

    private static final long serialVersionUID = 1L;

    private Long novel_id;

    private String type_name;

    private String type_url;

    private String novel_title;

    private String novel_name;

    private String novel_url;

    private String novel_intro;

    private Date crt_date;

    public Long getNovel_id() {
        return novel_id;
    }

    public void setNovel_id(Long novel_id) {
        this.novel_id = novel_id;
    }
    public String getType_name() {
        return type_name;
    }

    public void setType_name(String type_name) {
        this.type_name = type_name;
    }
    public String getType_url() {
        return type_url;
    }

    public void setType_url(String type_url) {
        this.type_url = type_url;
    }
    public String getNovel_title() {
        return novel_title;
    }

    public void setNovel_title(String novel_title) {
        this.novel_title = novel_title;
    }
    public String getNovel_name() {
        return novel_name;
    }

    public void setNovel_name(String novel_name) {
        this.novel_name = novel_name;
    }
    public String getNovel_url() {
        return novel_url;
    }

    public void setNovel_url(String novel_url) {
        this.novel_url = novel_url;
    }
    public String getNovel_intro() {
        return novel_intro;
    }

    public void setNovel_intro(String novel_intro) {
        this.novel_intro = novel_intro;
    }
    public Date getCrt_date() {
        return crt_date;
    }

    public void setCrt_date(Date crt_date) {
        this.crt_date = crt_date;
    }

    @Override
    protected Serializable pkVal() {
        return this.novel_id;
    }

    @Override
    public String toString() {
        return "Novel{" +
                "novel_id=" + novel_id +
                ", type_name=" + type_name +
                ", type_url=" + type_url +
                ", novel_title=" + novel_title +
                ", novel_name=" + novel_name +
                ", novel_url=" + novel_url +
                ", novel_intro=" + novel_intro +
                ", crt_date=" + crt_date +
                "}";
    }
}
