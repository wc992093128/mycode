package cn.itcast.travel.domain;

import java.util.List;

/**
 * 分页对象实体类
 */
public class PageBean<T> {
    private int totalCount;//总记录数
    private int totalpage;//总页数
    private int currentPage;//当前页码
    private int pageSize;//每页显示的条数
    private List<T> list; //每页显示的数据集合

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getTotalpage() {
        return totalpage;
    }

    public void setTotalpage(int totalpage) {
        this.totalpage = totalpage;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}
